package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.PageHub;
import utils.ConfigReader;

public class LoginTest extends BaseTest {

    private PageHub pages;

    @BeforeMethod
    @Override
    public void setUp() {
        super.setUp();
        pages = new PageHub(driver);
    }

    @Test(groups = {"login", "regression"})
    public void testComprehensiveLoginValidationFlow() {
        if (driver.getCurrentUrl().contains("/dashboard")) {
            pages.dashboardPage().logout();
        }

        pages.loginPage().loginDetails("", "");
        Assert.assertEquals(pages.loginPage().getUsernameRequiredMessage(), "Required");
        Assert.assertEquals(pages.loginPage().getPasswordRequiredMessage(), "Required");

        pages.loginPage().loginDetails("", "admin123");
        Assert.assertEquals(pages.loginPage().getUsernameRequiredMessage(), "Required");

        pages.loginPage().loginDetails("Admin", "");
        Assert.assertEquals(pages.loginPage().getPasswordRequiredMessage(), "Required");

        pages.loginPage().loginDetails("InvalidUser", "admin123");
        Assert.assertEquals(pages.loginPage().getErrorMessage(), "Invalid credentials");

        pages.loginPage().loginDetails("Admin", "WrongPassword123");
        Assert.assertEquals(pages.loginPage().getErrorMessage(), "Invalid credentials");

        pages.loginPage().loginDetails("InvalidUser", "WrongPassword123");
        Assert.assertEquals(pages.loginPage().getErrorMessage(), "Invalid credentials");

        pages.loginPage().loginDetails(
                ConfigReader.get("app.username"),
                ConfigReader.get("app.password"));

        pages.pimAddEmployee().waitForUrlContains("/dashboard");
        System.out.println("Login validation test completed successfully.");
    }
}
