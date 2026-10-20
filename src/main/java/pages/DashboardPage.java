package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import base.BasePage;
import utils.LocatorLoader;

/**
 * Page Object representing the central landing Dashboard screen
 * and the main user session termination features.
 */
public class DashboardPage extends BasePage {

    // Centralised cached locators mapped from properties config
    private final By dashboardHeader = By.xpath(LocatorLoader.getValue("dashboard.header"));
    private final By profileDropdown = By.xpath(LocatorLoader.getValue("dashboard.profileDropdown"));
    private final By logoutLink = By.xpath(LocatorLoader.getValue("dashboard.logoutLink"));

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Confirms the core dashboard framework components have loaded cleanly on-screen.
     */
    public boolean isDashboardDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader)).isDisplayed();
        } catch (Exception e) {
            System.out.println("LOG: Dashboard main header element was not detected.");
            return false;
        }
    }

    /**
     * Executes the formal user session logout sequence.
     */
    public void logout() {
        try {
            // 1. Click user profile image menu to expand the layout links panel
            wait.until(ExpectedConditions.elementToBeClickable(profileDropdown)).click();
            
            // 2. Click the explicit logout reference link block
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
            System.out.println("LOG: Programmatic logout routine dispatched completely.");
        } catch (Exception e) {
            System.err.println("LOG ERROR: Failed to execute logout sequence components completely.");
            throw e;
        }
    }
}
