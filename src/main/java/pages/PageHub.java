package pages;

import org.openqa.selenium.WebDriver;

public class PageHub {
    private final WebDriver driver;
    private LoginPage loginPage;
    private PimAddEmployee pimAddEmployee; // Renamed for naming consistency
    private AdminAddNewUser adminPage;
    private DashboardPage dashboardPage;

    public PageHub(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage loginPage() {
        if (loginPage == null) loginPage = new LoginPage(driver);
        return loginPage;
    }

    public PimAddEmployee pimAddEmployee() { // Matches the test code
        if (pimAddEmployee == null) pimAddEmployee = new PimAddEmployee(driver);
        return pimAddEmployee;
    }

    public AdminAddNewUser adminPage() {
        if (adminPage == null) adminPage = new AdminAddNewUser(driver);
        return adminPage;
    }

    public DashboardPage dashboardPage() {
        if (dashboardPage == null) dashboardPage = new DashboardPage(driver);
        return dashboardPage;
    }
}
