package common;

import com.google.common.collect.Iterables;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.TimeoutError;
import common.browser.OverridableBrowserConfiguration;
import common.exceptions.NavigationException;
import common.headedmode.PlaywrightHeadedDockerModule;
import common.shared.BrowserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class BaseTest {

    protected ThreadLocal<Injector> injector;
    private OverridableBrowserConfiguration headConfiguration = OverridableBrowserConfiguration.builder().build();
    public static final String UNABLE_TO_NAVIGATE_TO_THE_PAGE_MESSAGE = "Unable to navigate to the page ";

    public BaseTest() {
        injector = new ThreadLocal<>();
    }

    @BeforeMethod(alwaysRun = true)
    public void launchApplication() {
        injector.set(Guice.createInjector(getPlaywrightModule()));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTrace() {
        closeFrameworkProviders();
    }

    public void setCustomMode(OverridableBrowserConfiguration configuration) {
        headConfiguration = configuration;
        log.trace("Set custom mode with {}", headConfiguration);
    }

    protected AbstractModule getPlaywrightModule() {
        log.trace("Taking headed mode for the given test");
        return new PlaywrightHeadedDockerModule(headConfiguration);
    }

    public Page createNewTab() {
        return injector.get()
                .getInstance(BrowserContextHolder.class)
                .getDefault()
                .getNewPageOrEmptyOne();
    }

    public Page getLastTabOpen() {
        return Iterables.getLast(injector.get()
                .getInstance(BrowserContextHolder.class)
                .getDefault()
                .getContext()
                .pages());
    }

    protected <T extends BasePage> T webPageInstance(Class<T> tClass) {
        try {
            var page = injector.get().getInstance(Page.class);
            return tClass.getDeclaredConstructor(Page.class).newInstance(page);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException ex) {
            throw new NavigationException("Creation of WebPage instance failed", ex);
        }
    }

    protected <T extends BasePage> T webPageInstance(Class<T> tClass, String url) {
        try {
            log.trace("Web page instance with url {}", url);
            var webPage = webPageInstance(tClass);
            webPage.getPage().navigate(url);
            return webPage;
        } catch (TimeoutError error) {
            throw new NavigationException(UNABLE_TO_NAVIGATE_TO_THE_PAGE_MESSAGE + url, error);
        }
    }

    protected <T extends BasePage> T webPageInstanceInNewTab(Class<T> tClass, String url) {
        try {
            log.trace("Web page instance with url {}", url);
            var page = createNewTab();
            page.navigate(url);
            return tClass.getDeclaredConstructor(Page.class).newInstance(page);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException ex) {
            throw new NavigationException(UNABLE_TO_NAVIGATE_TO_THE_PAGE_MESSAGE, ex);
        } catch (TimeoutError error) {
            throw new NavigationException(UNABLE_TO_NAVIGATE_TO_THE_PAGE_MESSAGE + url, error);
        }
    }

    protected void closeFrameworkProviders() {
        log.trace("Closing playwright instance and browser context");
        injector.get().getInstance(BrowserContextHolder.class).close();
        injector.get().getInstance(Browser.class).close();
        injector.get().getInstance(Playwright.class).close();
    }

}
