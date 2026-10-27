package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.PageHub;

public class AdminAddNewUserTest extends BaseTest {

    private PageHub pages;

    @BeforeMethod
    @Override
    public void setUp() {
        super.setUp();
        pages = new PageHub(driver);
    }

    @Test(groups = {"admin", "regression"})
    public void testAdminPanelValidationsAndUserCreation() {

        // Smart URL Gate: If the browser is logged out, complete the login details sequence first
        if (!driver.getCurrentUrl().contains("/dashboard") && !driver.getCurrentUrl().contains("/pim") && !driver.getCurrentUrl().contains("/admin")) {
            pages.loginPage().loginDetails(
                    utils.ConfigReader.get("app.username"),
                    utils.ConfigReader.get("app.password"));
            pages.pimAddEmployee().waitForUrlContains("/dashboard");
        }

        // 1. Verify that the Admin user form blocks completely blank submissions
        boolean blankValidationPassed = pages.adminPage().isMandatoryFieldsBlankValidationPassed();
        Assert.assertTrue(
                blankValidationPassed, 
                "Mandatory empty field validation did not trigger errors on the admin form.");

        // 2. Define the exact targets created during your PIM script sequence
        String employeeFirstName = "John";
        String employeeLastName = "Doe";

        // 3. Complete the role assignment workflow (Types "John", handles options natively via keys, and saves)
        String generatedUsername = pages.adminPage().assignUserRoleToEmployee(employeeFirstName, employeeLastName);
        
        Assert.assertNotNull(generatedUsername, "Generated admin username should not be null.");
        Assert.assertFalse(generatedUsername.trim().isEmpty(), "Generated admin username should not be empty.");

        // 4. Log out cleanly after the Admin assignment flow is finished
        pages.dashboardPage().logout();
        
        Assert.assertTrue(
                pages.loginPage().isLoginPageDisplayed(), 
                "Application failed to return back to the login page viewport context after logging out.");

        System.out.println("Admin User Assignment suite executed successfully for Username: " + generatedUsername);
    }
}
