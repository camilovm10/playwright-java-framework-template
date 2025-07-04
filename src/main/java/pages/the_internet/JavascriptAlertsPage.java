package pages.the_internet;

import com.microsoft.playwright.Dialog;
import com.microsoft.playwright.Page;
import common.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class JavascriptAlertsPage extends BasePage {

    private static final String JAVASCRIPT_ALERTS_LINK_TEXT = "Javascript Alerts";

    public JavascriptAlertsPage(Page page) {
        super(page);
    }

    @Step("Click on Javascript Alerts link")
    public void handleJavascriptAlerts() {
        log.warn("Handling Javascript Alerts on page: {}", JAVASCRIPT_ALERTS_LINK_TEXT);
        IntStream.rangeClosed(1, 3).forEach(i -> {

            page.locator(String.format("#content ul li:nth-child(%s) button", i)).click();
            page.onDialog(Dialog::accept);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
