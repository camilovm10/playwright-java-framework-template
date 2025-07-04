package common.browser;

import com.microsoft.playwright.options.ViewportSize;
import lombok.extern.slf4j.Slf4j;
import java.awt.*;

@Slf4j
public class ViewPortConfig {

    // Regex examples : 1600x1200 or 1000x600 or 1600 x 900 or 1920 x 1080
    public static final String REGEX_FOR_VIEWPORT_FORMAT = "\\d+x\\d+";

    public boolean isCustomViewPortEnable() {
        boolean isEnable = System.getProperty("viewPort", "").matches(REGEX_FOR_VIEWPORT_FORMAT);
        log.trace("The custom view port is enable {}", isEnable);
        return isEnable;
    }

    public ViewportSize getCustomViewPortEnable() {
        int width = Integer.parseInt(System.getProperty("viewPort").split("x")[0]);
        int height = Integer.parseInt(System.getProperty("viewPort").split("x")[0]);
        log.trace("Custom view port configuration width : {} height : {} ", width, height);
        return new ViewportSize(width, height);
    }

    public ViewportSize getViewPortOfMachine() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        log.trace("Machine view port configuration width : {} height : {} ", width, height);
        return new ViewportSize(width, height);
    }
}
