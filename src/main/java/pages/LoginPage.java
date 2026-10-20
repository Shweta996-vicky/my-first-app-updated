package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import base.BasePage;
import utils.LocatorLoader;

/**
 * Page Object capturing authentication interactions, safe validation 
 * field cleanup mechanisms, and dynamic error state text parsing hooks.
 */
public class LoginPage extends BasePage {

    // Centralised cached locators loaded via properties mapping config
    private final By usernameField = By.xpath(LocatorLoader.getValue("login.username"));
    private final By passwordField = By.xpath(LocatorLoader.getValue("login.password"));
    private final By submitBtn = By.xpath(LocatorLoader.getValue("login.submitBtn"));
    private final By errorAlert = By.xpath(LocatorLoader.getValue("login.errorAlert"));
    private final By usernameRequiredError = By.xpath(LocatorLoader.getValue("login.usernameRequiredError"));
    private final By passwordRequiredError = By.xpath(LocatorLoader.getValue("login.passwordRequiredError"));

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Clears input elements cleanly and enters new target authentication parameters.
     */
    public void loginDetails(String username, String password) {
        // Process Username Field securely
        WebElement userElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        clearInputFieldSafely(userElement);
        if (!username.isEmpty()) {
            userElement.sendKeys(username);
        }

        // Process Password Field securely
        WebElement passElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        clearInputFieldSafely(passElement);
        if (!password.isEmpty()) {
            passElement.sendKeys(password);
        }

        // Execute form submission
        wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
    }

    /**
     * Fail-safe method to completely wipe out any lingering text in an input box.
     */
    private void clearInputFieldSafely(WebElement element) {
        element.clear(); // Standard Selenium clear
        
        // Secondary fallback to guarantee input field cleanup on dynamic JavaScript views
        if (!element.getAttribute("value").isEmpty()) {
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            if (!element.getAttribute("value").isEmpty()) {
                element.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
            }
        }
    }

    public boolean isLoginPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(submitBtn)).isDisplayed();
    }
    
    public boolean isUsernameFieldEnabled() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isEnabled();
    }

    /**
     * Extracts validation message label text for missing username field constraints.
     */
    public String getUsernameRequiredMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameRequiredError)).getText().trim();
    }

    /**
     * Extracts validation message label text for missing password field constraints.
     */
    public String getPasswordRequiredMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordRequiredError)).getText().trim();
    }

    /**
     * Extracts dynamic toast/alert feedback block strings during bad authentication entries.
     */
    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert)).getText().trim();
    }
}
