package common.headedmode;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.microsoft.playwright.Browser;
import common.browser.OverridableBrowserConfiguration;
import common.shared.BrowserContextHolder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrowserContextProvider implements Provider<BrowserContextHolder>{

    private final OverridableBrowserConfiguration configuration;
    @Inject
    private Browser browser;


    @Inject
    public BrowserContextProvider(@Named("configuration") OverridableBrowserConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    public BrowserContextHolder get() {
        log.trace("Browser context provider for Docker module");
        return new BrowserContextHolder(browser, configuration);
    }

}
