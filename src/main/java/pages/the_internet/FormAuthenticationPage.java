package pages.the_internet;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import common.BasePage;

public class FormAuthenticationPage extends BasePage {

    public FormAuthenticationPage(Page page) {
        super(page);
    }

    public FormAuthenticationPage fillAuthenticationForm(String username, String password) {
        page.locator("#username").fill(username);
        page.locator("#password").fill(password);
        page.locator("button").click();
        return this;
    }

    public String getSuccessMessage() {
        Locator successMsg = page.locator("#flash");
        return successMsg.textContent().trim();
    }
}
