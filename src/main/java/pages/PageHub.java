package pages;

import org.openqa.selenium.WebDriver;

public class PageHub {

    private final WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private PimAddEmployee pimAddEmployee;
    private AdminAddNewUser adminPage;

    public PageHub(WebDriver driver) {
        this.driver = driver;
    }

    // Initializes and returns the LoginPage instance if null, otherwise returns the existing instance
    public LoginPage loginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }

    // Initializes and returns the DashboardPage instance if null, otherwise returns the existing instance
    public DashboardPage dashboardPage() {
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage(driver);
        }
        return dashboardPage;
    }

    // Initializes and returns the PimAddEmployee instance if null, otherwise returns the existing instance
    public PimAddEmployee pimAddEmployee() {
        if (pimAddEmployee == null) {
            pimAddEmployee = new PimAddEmployee(driver);
        }
        return pimAddEmployee;
    }

    // Initializes and returns the AdminAddNewUser instance if null, otherwise returns the existing instance
    public AdminAddNewUser adminPage() {
        if (adminPage == null) {
            adminPage = new AdminAddNewUser(driver);
        }
        return adminPage;
    }
}
