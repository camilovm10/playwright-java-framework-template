package common.browser;

import com.microsoft.playwright.options.ViewportSize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Slf4j
public class OverridableBrowserConfiguration {

    // READ THE ENVIRONMENT VARIABLES BY DEFAULT

    @Builder.Default
    private String browser = BrowserConfig.getBrowser();
    @Builder.Default
    private boolean isHeadless = BrowserConfig.isHeadless();

    // Any instances of this class we'll have the system/default value by default already set up

    @Builder.Default
    private List<String> permissions = setUpDefaultPermissions();
    @Builder.Default
    private ViewportSize viewportSize = setUpDefaultViewPorts();

    @Builder.Default
    private boolean isMobile = false;

    private String locale;

    /**
     * By default, it will set the view port of the machine, next
     * will check if in the system property 'viewPort' was set and
     * this value will be used instead then.
     */
    private static ViewportSize setUpDefaultViewPorts() {
        var viewPortConfig = new ViewPortConfig();
        // MACHINE CONFIG
         var viewSize = viewPortConfig.getViewPortOfMachine();

        // SYSTEM VARIABLE
        if (viewPortConfig.isCustomViewPortEnable()) {
            viewSize = viewPortConfig.getCustomViewPortEnable();
        }
        return viewSize;
    }

    private static List<String> setUpDefaultPermissions() {
        var permissions = BrowserConfig.getBrowser().equalsIgnoreCase("chrome") ?
                BrowserConfig.getChromeDefaultPermissions() : new ArrayList<String>();
        log.trace("Browser Permissions: {}", permissions);
        return permissions;

    }

    @Override
    public String toString() {
        return String.format("Browser : %s ,Headless mode :  %s, View port  : %s , Permissions : %s",browser, isHeadless, toStringViewPort(), getPermissions());
    }

    private String toStringViewPort() {
        return String.format("width : %s height : %s ", viewportSize.width, viewportSize.height);
    }
}
