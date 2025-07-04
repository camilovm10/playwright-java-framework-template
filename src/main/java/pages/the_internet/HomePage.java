package pages.the_internet;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import common.BasePage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomePage extends BasePage {

    private static final String AUTH_FORM_LINK_TEXT = "Form Authentication";

    public HomePage(Page page) {
        super(page);
    }

    public FormAuthenticationPage clickOnFormAuthentication() {
        page.getByText(AUTH_FORM_LINK_TEXT).click();
        return createWebPageInstance(FormAuthenticationPage.class);
    }
}
