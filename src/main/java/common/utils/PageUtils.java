package common.utils;

import com.microsoft.playwright.Page;
import java.net.MalformedURLException;
import java.net.URL;

public class PageUtils {

    public static boolean isTabEmpty(Page page) {
        try {
            new URL(page.url());
            return false;
        } catch (MalformedURLException e) {
            return true;
        }
    }
}
