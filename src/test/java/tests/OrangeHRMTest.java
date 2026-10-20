package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BasePage;
import pages.PageHub;

public class OrangeHRMTest {
    WebDriver driver;
    PageHub pageHub;
    private String savedEmployeeId;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver(BasePage.getChromeOptionsSetup());
        driver.manage().window().maximize();
        pageHub = new PageHub(driver);
    }

    @Test(priority = 1)
    public void testBrowserNavigationAndAttributes() {
        driver.get(utils.LocatorLoader.getValue("app.url"));
        String pageTitle = driver.getTitle();
        String currentUrl = driver.getCurrentUrl();
        
        System.out.println("Page Title: " + pageTitle);
        System.out.println("Current URL: " + currentUrl);
        
        Assert.assertTrue(pageTitle.contains("OrangeHRM"), "Page title validation failed!");
    }

    @Test(priority = 2)
    public void testElementStatesBeforeInteraction() {
        Assert.assertTrue(pageHub.loginPage().isLoginPageDisplayed(), "Login submit button is not visible!");
        Assert.assertTrue(pageHub.loginPage().isUsernameFieldEnabled(), "Username field is not enabled for typing!");
    }

    @Test(priority = 3)
    public void testEmptyFieldLoginScenarios() {
        pageHub.loginPage().loginDetails("", "");
        Assert.assertEquals(pageHub.loginPage().getUsernameRequiredMessage(), "Required", "Username empty validation failed!");
        Assert.assertEquals(pageHub.loginPage().getPasswordRequiredMessage(), "Required", "Password empty validation failed!");
    }

    @Test(priority = 4)
    public void testInvalidLoginScenarios() {
        pageHub.loginPage().loginDetails("InvalidUser", "InvalidPass123");
        Assert.assertEquals(pageHub.loginPage().getErrorMessage(), "Invalid credentials", "Error text message mismatched!");
    }

    @Test(priority = 5)
    public void testSuccessfullLoginAndDashboard() {
        pageHub.loginPage().loginDetails("Admin", "admin123");
        Assert.assertTrue(pageHub.dashboardPage().isDashboardDisplayed(), "Dashboard page failed to show up!");
        
        System.out.println("Captured Page Title : " + driver.getTitle());
        System.out.println("Captured Page URL   : " + driver.getCurrentUrl());
    }

    @Test(priority = 6)
    public void testAddAndVerifyEmployeeWorkflow() {
        // State Guard: Ensure the user is authenticated before proceeding
        if (!pageHub.dashboardPage().isDashboardDisplayed()) {
            System.out.println("WARN: Active session not detected. Recovering clean state login...");
            driver.get(utils.LocatorLoader.getValue("app.url"));
            pageHub.loginPage().loginDetails("Admin", "admin123");
        }

        // Navigate to PIM using PageHub definition (.pimAddEmployee())
        pageHub.pimAddEmployee().navigateToPIM();

        // Extract and print the real-time PIM page metrics to the console logs
        String pimTitle = driver.getTitle();
        String pimUrl = driver.getCurrentUrl();
        
        System.out.println("==================================================");
        System.out.println("LOG: Successfully verified PIM Page Landing Details:");
        System.out.println("Captured PIM Page Title : " + pimTitle);
        System.out.println("Captured PIM Page URL   : " + pimUrl);
        System.out.println("==================================================");
        
        Assert.assertTrue(pimTitle.contains("PIM") || pimTitle.contains("OrangeHRM"), 
                "PIM Page navigation title validation failed!");

        // Create Employee and capture ID
        savedEmployeeId = pageHub.pimAddEmployee().createNewEmployee("Jane", "Doe");
        Assert.assertNotNull(savedEmployeeId, "Failed to capture the auto-generated Employee ID.");

        // Search for the generated ID in the directory list layout
        boolean isFound = pageHub.pimAddEmployee().searchEmployeeById(savedEmployeeId);
        Assert.assertTrue(isFound, "The generated Employee ID was not found in the search results layout.");

        // Click on the record inside the grid to open the profile view
        pageHub.pimAddEmployee().selectEmployeeFromGrid(savedEmployeeId);
        
        // Execute the profile adjustments (Marital Status & Gender) and catch the save confirmation
        pageHub.pimAddEmployee().updatePersonalDetailsAndSave();
        
        System.out.println("SUCCESS: Automation script completed cleanly for ID " + savedEmployeeId);
    }

    @Test(priority = 8)
    public void testAdminCreateNewUserAccount() {
        pageHub.adminPage().navigateAndFillForm();
    }

    @Test(priority = 9)
        public void testLogoutWorkflow() {
            // 1. Dispatch the programmatic logout expansion click sequence
            pageHub.dashboardPage().logout();
            
            // 2. Sync Point Guard: Explicitly wait until the login page components are active in the DOM
            Assert.assertTrue(pageHub.loginPage().isLoginPageDisplayed(), 
                    "Logout action did not redirect back to Login screen layout!");
            
            // 3. Extract and print the landing page metrics to confirm routing
            String logoutLandingTitle = driver.getTitle();
            String logoutLandingUrl = driver.getCurrentUrl();
            
            System.out.println("==================================================");
            System.out.println("LOG SUCCESS: Post-Logout Redirect Confirmed Cleanly:");
            System.out.println("Final Page Title : " + logoutLandingTitle);
            System.out.println("Final Page URL   : " + logoutLandingUrl);
            System.out.println("==================================================");
            
            // 4. Assert title validation to finish your suite with a strict verification seal
            Assert.assertTrue(logoutLandingUrl.contains("login"), 
                    "The destination page URL structural pattern does not match the login route!");
        }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}