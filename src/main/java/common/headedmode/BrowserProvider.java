package common.headedmode;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import common.browser.BrowserConfig;
import common.browser.OverridableBrowserConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrowserProvider implements Provider<Browser> {

    @Inject
    private Playwright playwright;
    private final String browser;
    private final Boolean headlessMode;

    @Inject
    public BrowserProvider(@Named("configuration") OverridableBrowserConfiguration configuration){
        this.browser = configuration.getBrowser();
        this.headlessMode = configuration.isHeadless();
        log.debug("Browser Provider setting : Browser {} headlessMode {}", browser, headlessMode);
    }

    @Override
    public Browser get() {
        BrowserType browserType;
        BrowserType.LaunchOptions launchOptions = BrowserConfig.getLaunchOptionsWithFakeMediaStream(headlessMode);

        switch (browser.toLowerCase()) {
            case "firefox": {
                browserType = playwright.firefox();
                break;
            }
            case "chrome": {
                browserType = playwright.chromium();
                launchOptions.setChannel("chrome")
                        .setHeadless(headlessMode);
                break;
            }
            default:
                browserType = playwright.chromium();
        }
        log.debug("Launching browser {}", launchOptions.args.stream().toList());
        return browserType.launch(launchOptions);
    }

}
