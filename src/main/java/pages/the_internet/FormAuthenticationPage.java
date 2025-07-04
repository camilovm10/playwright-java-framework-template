package pages.the_internet;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import common.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FormAuthenticationPage extends BasePage {

    public FormAuthenticationPage(Page page) {
        super(page);
    }

    @Step("Fill authentication form with username: {username} and password: {password}")
    public FormAuthenticationPage fillAuthenticationForm(String username, String password) {
        log.warn("Filling authentication form ");
        page.locator("#username").fill(username);
        page.locator("#password").fill(password);
        page.locator("button").click();
        return this;
    }

    @Step("Get success message after authentication")
    public String getSuccessMessage() {
        Locator successMsg = page.locator("#flash");
        return successMsg.textContent().trim();
    }
}
