package pages;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import base.BasePage;
import utils.ConfigReader;
import utils.LocatorLoader;

public class AdminAddNewUser extends BasePage {

    private final By menuAdmin = locator("menu.admin");
    private final By addBtn = locator("admin.addBtn");
    private final By roleDropdown = locator("user.userRoleDropdown");
    private final By employeeNameInput = locator("user.empNameInput");
    private final By statusDropdown = locator("user.statusDropdown");
    private final By statusEnabled = locator("user.statusEnabledOption");
    private final By usernameInput = locator("user.usernameInput");
    private final By passwordInput = locator("user.passwordInput");
    private final By confirmPasswordInput = locator("user.confirmPasswordInput");
    private final By saveBtn = locator("user.saveBtn");
    private final By successToast = locator("pim.successToast");
    private final By usernameRequired = locator("user.usernameRequiredError");
    private final By fieldErrorSubtext = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");
    private final By dropdownLoaderSpinner = By.xpath("//div[contains(@class, 'oxd-autocomplete-text-input--loading')]");

    private final By autocompleteOptionWrapper = By.xpath("//div[@role='listbox' or contains(@class, 'oxd-autocomplete-dropdown')]");

    public AdminAddNewUser(WebDriver driver) {
        super(driver);
    }

    private By locator(String key) {
        return By.xpath(LocatorLoader.getValue(key));
    }

    public void navigateToAddUser() {
        clickElement(menuAdmin);
        wait.until(ExpectedConditions.urlContains("/admin/"));
        clickElement(addBtn);
        wait.until(ExpectedConditions.urlContains("/saveSystemUser"));
    }

    // Types the first name, waits for the query spinner, and uses keyboard events to select option 1 cleanly
    private void selectEmployeeFromAutocomplete(String employeeFirstName) {
        WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameInput));
        
        safeEnterText(employeeNameInput, employeeFirstName);
        
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.invisibilityOfElementLocated(dropdownLoaderSpinner));
        } catch (TimeoutException e) {
        }
        
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        wait.until(ExpectedConditions.visibilityOfElementLocated(autocompleteOptionWrapper));
        
        inputField.sendKeys(Keys.DOWN);
        try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        inputField.sendKeys(Keys.ENTER);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public boolean isMandatoryFieldsBlankValidationPassed() {
        navigateToAddUser();
        clickElement(saveBtn);
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(fieldErrorSubtext)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String assignUserRoleToEmployee(String employeeFirstName, String employeeLastName) {
        clickElement(roleDropdown);
        By roleOptionEss = By.xpath("//div[@role='option']//span[text()='ESS']");
        clickElement(roleOptionEss);

        selectEmployeeFromAutocomplete(employeeFirstName);

        clickElement(statusDropdown);
        clickElement(statusEnabled);

        String uniqueUsername = employeeFirstName.replaceAll("[^a-zA-Z0-9]", "")
                + ThreadLocalRandom.current().nextInt(100000, 999999);

        String systemPassword = ConfigReader.get("test.admin.password");
        safeEnterText(passwordInput, systemPassword);

        safeEnterText(usernameInput, "");
        safeEnterText(confirmPasswordInput, systemPassword);
        clickElement(saveBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameRequired));

        safeEnterText(usernameInput, uniqueUsername);
        safeEnterText(confirmPasswordInput, systemPassword + "Mismatch");
        clickElement(saveBtn);

        safeEnterText(confirmPasswordInput, systemPassword);
        clickSaveButton(saveBtn);
        
        return uniqueUsername;
    }

    // Fixed Method: Correctly maps zero-indexed arguments arrays to execute scroll and click actions safely
    private void clickSaveButton(By buttonLocator) {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(buttonLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);
        try {
            button.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(successToast));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(successToast));
    }
}
