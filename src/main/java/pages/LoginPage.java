package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import base.BasePage;
import utils.LocatorLoader;

public class LoginPage extends BasePage {

    private final By usernameField = By.xpath(LocatorLoader.getValue("login.username"));
    private final By passwordField = By.xpath(LocatorLoader.getValue("login.password"));
    private final By submitButton = By.xpath(LocatorLoader.getValue("login.submitBtn"));
    private final By errorAlert = By.xpath(LocatorLoader.getValue("login.errorAlert"));
    private final By usernameRequired = By.xpath(LocatorLoader.getValue("login.usernameRequiredError"));
    private final By passwordRequired = By.xpath(LocatorLoader.getValue("login.passwordRequiredError"));

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loginDetails(String username, String password) {
        safeEnterText(usernameField, username);
        safeEnterText(passwordField, password);
        clickElement(submitButton);
    }

    public boolean isLoginPageDisplayed() {
        return isElementDisplayed(submitButton);
    }

    public String getUsernameRequiredMessage() {
        return getElementText(usernameRequired).trim();
    }

    public String getPasswordRequiredMessage() {
        return getElementText(passwordRequired).trim();
    }

    public String getErrorMessage() {
        return getElementText(errorAlert).trim();
    }
}
