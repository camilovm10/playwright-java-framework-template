package the_internet;

import common.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.the_internet.HomePage;

public class VerifyDropdownTest extends BaseTest {

    @Test
    public void authenticationTest() {
        HomePage homePage = webPageInstance(HomePage.class, "https://the-internet.herokuapp.com/");
        var authFormPage = homePage
                .clickOnFormAuthentication()
                .fillAuthenticationForm("tomsmith", "SuperSecretPassword!");

        String successMessage = authFormPage.getSuccessMessage();
        Assert.assertEquals(successMessage, "You logged into a secure area!\n" +
                "            ×");
    }
}
