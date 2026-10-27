package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import base.BasePage;
import utils.LocatorLoader;

public class DashboardPage extends BasePage {

    private final By dashboardHeader = By.xpath(LocatorLoader.getValue("dashboard.header"));
    private final By profileDropdown = By.xpath(LocatorLoader.getValue("dashboard.profileDropdown"));
    private final By logoutLink = By.xpath(LocatorLoader.getValue("dashboard.logoutLink"));
    private final By loginUsername = By.xpath(LocatorLoader.getValue("login.username"));

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    // Checks if the dashboard page header is visible
    public boolean isDashboardDisplayed() {
        return isElementDisplayed(dashboardHeader);
    }

    // Opens the profile dropdown menu, clicks logout, and verifies the login page loads
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(profileDropdown)).click();
        
        WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        logout.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(loginUsername));
    }
}
