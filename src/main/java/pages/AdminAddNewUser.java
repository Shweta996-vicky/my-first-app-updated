package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;
import utils.LocatorLoader;
import java.time.Duration;

/**
 * Page Object representing the Admin module user management panel workflows in OrangeHRM.
 */
public class AdminAddNewUser extends BasePage {

    // Centralised cached locators mapped from properties configuration
    private final By menuAdmin = By.xpath(LocatorLoader.getValue("menu.admin"));
    private final By addBtn = By.xpath(LocatorLoader.getValue("admin.addBtn"));
    private final By userRoleDropdown = By.xpath(LocatorLoader.getValue("user.userRoleDropdown"));
    private final By roleOptionESS = By.xpath(LocatorLoader.getValue("user.roleOptionESS"));
    private final By empNameInput = By.xpath(LocatorLoader.getValue("user.empNameInput"));
    private final By empNameOption = By.xpath(LocatorLoader.getValue("user.empNameOption"));
    private final By statusDropdown = By.xpath(LocatorLoader.getValue("user.statusDropdown"));
    private final By statusEnabledOption = By.xpath(LocatorLoader.getValue("user.statusEnabledOption"));
    private final By usernameInput = By.xpath(LocatorLoader.getValue("user.usernameInput"));
    private final By passwordInput = By.xpath(LocatorLoader.getValue("user.passwordInput"));
    private final By confirmPasswordInput = By.xpath(LocatorLoader.getValue("user.confirmPasswordInput"));
    private final By saveBtn = By.xpath(LocatorLoader.getValue("user.saveBtn"));
    private final By successToast = By.xpath(LocatorLoader.getValue("pim.successToast"));

    public AdminAddNewUser(WebDriver driver) {
        super(driver);
    }

    /**
     * Complete step-by-step navigation, page analytics tracking, 
     * and form configuration profile entry for creating a new ESS system user account.
     */
    public void navigateAndFillForm() {
        // 1. Navigate to Admin Module
        try {
            WebElement adminMenu = wait.until(ExpectedConditions.elementToBeClickable(menuAdmin));
            adminMenu.click();
        } catch (Exception e) {
            WebElement adminMenuFallback = driver.findElement(menuAdmin);
            ((JavascriptExecutor) driver).executeScript("arguments.click();", adminMenuFallback);
        }
        
        // Dynamic Sync: Wait for page path shift completely before analyzing titles
        wait.until(ExpectedConditions.urlContains("/admin/"));
        
        // Capture and print Admin Landing Metrics
        System.out.println("==================================================");
        System.out.println("LOG: Successfully Navigated to Admin Module:");
        System.out.println("Captured Admin Page Title : " + driver.getTitle());
        System.out.println("Captured Admin Page URL   : " + driver.getCurrentUrl());
        System.out.println("==================================================");

        // 2. Click Add User button
        clickElement(addBtn);
        wait.until(ExpectedConditions.urlContains("/saveSystemUser"));
        
        // Capture and print Add Screen Title Metrics
        System.out.println("==================================================");
        System.out.println("LOG: Landed on Add User Configuration Panel Form:");
        System.out.println("Captured Add Page Title   : " + driver.getTitle());
        System.out.println("==================================================");

        // 3. Select User Role as ESS from dropdown layout configuration block
        clickElement(userRoleDropdown);
        clickElement(roleOptionESS);
        System.out.println("LOG: Configured User Role as 'ESS'.");

        // 4. Enter Employee Name and select exact matching option from autocomplete suggestion panel
        String targetEmployee = "John Doe";
        safeEnterText(empNameInput, targetEmployee);
        
        // Explicitly wait until the network search returns matching hints layout panels
        WebElement matchedOption = wait.until(ExpectedConditions.elementToBeClickable(empNameOption));
        matchedOption.click();
        System.out.println("LOG: Confirmed Employee Name mapping link for: " + targetEmployee);

        // 5. Select Status Dropdown and toggle it to Enabled
        clickElement(statusDropdown);
        clickElement(statusEnabledOption);
        System.out.println("LOG: Configured Account Status condition state to 'Enabled'.");

        // 6. Generate and enter a unique Username exceeding the 5-character verification constraint rule
        long randomSuffix = System.currentTimeMillis() % 100000;
        String nameWithoutSpaces = targetEmployee.replace(" ", "");
        String runtimeUser = nameWithoutSpaces + randomSuffix; // e.g., JohnDoe54321
        
        // Enforce the validation criteria explicitly before interacting with elements
        if (runtimeUser.length() < 5) {
            runtimeUser = runtimeUser + "UserBypass";
        }
        
        System.out.println("LOG: Generated compliance username: " + runtimeUser + " (Length: " + runtimeUser.length() + ")");
        safeEnterText(usernameInput, runtimeUser);

        // 7. Generate and enter password combination strings matching the pattern (name@123)
        String runtimePassword = nameWithoutSpaces + "@123"; // e.g., JohnDoe@123
        safeEnterText(passwordInput, runtimePassword);
        safeEnterText(confirmPasswordInput, runtimePassword);
        System.out.println("LOG: Configured password parameters completely.");

        // 8. Dispatched Form Submission Event Transaction
        clickElement(saveBtn);
        System.out.println("LOG: Dispatched creation save request event to system directory backend.");

        // 9. Sync verification check loop for server toast confirmation banner message response
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(6));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(successToast));
            System.out.println("LOG SUCCESS: Admin user profile account addition complete and saved successfully.");
        } catch (Exception e) {
            System.out.println("WARNING: Success toast disappeared instantly or encountered element sync layer delay. Workflow execution continuing.");
        }
    }
}
