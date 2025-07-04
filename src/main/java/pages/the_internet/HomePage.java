package pages.the_internet;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import common.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomePage extends BasePage {

    private static final String AUTH_FORM_LINK_TEXT = "Form Authentication";
    private static final String JAVASCRIPT_ALERTS_LINK_TEXT = "Javascript Alerts";

    public HomePage(Page page) {
        super(page);
    }

    @Step("Click on Form Authentication link")
    public FormAuthenticationPage clickOnFormAuthentication() {
        log.debug("Clicking on Form Authentication link");
        page.getByText(AUTH_FORM_LINK_TEXT).click();
        return createWebPageInstance(FormAuthenticationPage.class);
    }

    @Step("Click on Javascript Alerts link")
    public JavascriptAlertsPage clickOnJavascriptAlerts() {
        log.info("Clicking on Javascript Alerts link");
        page.getByText(JAVASCRIPT_ALERTS_LINK_TEXT).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.getByText(JAVASCRIPT_ALERTS_LINK_TEXT).click();
        return createWebPageInstance(JavascriptAlertsPage.class);
    }
}
