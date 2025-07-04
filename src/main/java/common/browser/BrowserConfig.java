package common.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BrowserConfig {

    public static String getBrowser(){
        return System.getProperty("browser", "chrome").toLowerCase();
    }

    public static Boolean isHeadless(){
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }

    /**
     * Emulates the camera and video streaming
     */
    public static BrowserType.LaunchOptions getLaunchOptionsWithFakeMediaStream(Boolean isHeadless) {
        return new BrowserType.LaunchOptions()
                .setHeadless(isHeadless)
                .setArgs(List.of(
                        "--use-fake-device-for-media-stream",
                        "--use-fake-ui-for-media-stream",
                        "--no-sandbox")
                );
    }


    public static Browser.NewContextOptions createContextOptions(OverridableBrowserConfiguration configuration) {
        return new Browser.NewContextOptions()
                .setViewportSize(configuration.getViewportSize())
                .setPermissions(configuration.getPermissions())
                .setLocale(configuration.getLocale())
                .setIsMobile(configuration.isMobile())
                .setIgnoreHTTPSErrors(true);

    }

    public static List<String> getChromeDefaultPermissions() {
        List<String> permissions = new ArrayList<>();
        boolean flag = Boolean.parseBoolean(System.getProperty("allowNotifications", "true"));
        if (flag) permissions.add("notifications");
        flag = Boolean.parseBoolean(System.getProperty("allowCamera", "true"));
        if (flag) permissions.add("camera");
        flag = Boolean.parseBoolean(System.getProperty("allowMicrophone", "true"));
        if (flag) permissions.add("microphone");
        return permissions;
    }


    private static String getAbsolutePath(String file) {
        return Paths.get(file).toAbsolutePath().toString();
    }
}
