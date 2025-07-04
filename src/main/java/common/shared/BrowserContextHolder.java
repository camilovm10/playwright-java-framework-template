package common.shared;

import com.google.inject.Inject;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.Tracing.StopOptions;
import common.browser.BrowserConfig;
import common.browser.OverridableBrowserConfiguration;
import common.utils.PageUtils;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static common.utils.TimeOuts.TWO_MINUTE;

/**
 * Manages a pool of Playwright {@link BrowserContext}s within a single {@link Browser} instance.  Ensures thread-safe
 * modifications by using {@link CopyOnWriteArrayList}.  Tracing and sensible defaults are applied eagerly.
 */
@Slf4j
public class BrowserContextHolder {

    private final List<Context> contexts = new CopyOnWriteArrayList<>();
    private final Browser browser;
    private final OverridableBrowserConfiguration configuration;

    /* ------------------------------------------------------------------------------------------------------------ */
    /*  Context wrapper                                                                                             */
    /* ------------------------------------------------------------------------------------------------------------ */

    @Getter
    @AllArgsConstructor
    public static class Context {
        @Setter
        private String name;
        private BrowserContext context;

        public boolean hasAllTabsEmpty() {
            return context.pages().stream().allMatch(PageUtils::isTabEmpty);
        }

        public Page getNewPage() {
            return context.newPage();
        }

        public Page getNewPageOrEmptyOne() {
            return context.pages().stream()
                    .filter(PageUtils::isTabEmpty)
                    .findFirst()
                    .orElseGet(() -> {
                        log.trace("No empty page found in context: '{}', creating a new one", name);
                        return getNewPage();
                    });
        }
    }

    /* ------------------------------------------------------------------------------------------------------------ */
    /*  Constructors                                                                                                */
    /* ------------------------------------------------------------------------------------------------------------ */

    @Inject
    public BrowserContextHolder(Browser browser) {
        this(browser, null);
    }

    public BrowserContextHolder(Browser browser, OverridableBrowserConfiguration configuration) {
        this.browser       = browser;
        this.configuration = configuration;
        createContext("Default context");
    }

    /* ------------------------------------------------------------------------------------------------------------ */
    /*  Public API                                                                                                  */
    /* ------------------------------------------------------------------------------------------------------------ */

    public Context createContext(String name) {
        log.trace("Creating new context: {}", name);
        BrowserContext bContext = buildBrowserContext();
        Context wrapper = new Context(name, bContext);
        contexts.add(wrapper);
        return wrapper;
    }

    public Context getLastContext() {
        if (contexts.isEmpty()) {
            throw new RuntimeException("No context found");
        }
        return contexts.get(contexts.size() - 1);
    }

    public Context getDefault() {
        return contexts.get(0);
    }

    public void stopTracing() {
        contexts.forEach(this::stopTracing);
    }

    public void close() {
        contexts.forEach(c -> Try.run(c.getContext()::close).onFailure(e -> log.warn("Error closing context", e)));
        log.trace("Closed all contexts");
    }

    /* ------------------------------------------------------------------------------------------------------------ */
    /*  Private helpers                                                                                             */
    /* ------------------------------------------------------------------------------------------------------------ */
    private BrowserContext buildBrowserContext() {
        BrowserContext ctx = browser.newContext(BrowserConfig.createContextOptions(configuration));
        applyDefaults(ctx);
        return ctx;
    }

    private void applyDefaults(BrowserContext bContext) {
        bContext.setDefaultTimeout(TWO_MINUTE.getValue());
        bContext.setDefaultNavigationTimeout(TWO_MINUTE.getValue());
        bContext.tracing().start(new Tracing.StartOptions().setSnapshots(true));
        log.trace("Applied default BrowserContext settings");
    }

    private void stopTracing(Context wrapper) {
        Path tracePath = Paths.get("build/reports/traces/%s_%d.zip".formatted(wrapper.getName(), System.currentTimeMillis()));
        wrapper.getContext().tracing().stop(new StopOptions().setPath(tracePath));
        log.trace("Stopped trace for context: {}", wrapper.getName());
    }
}