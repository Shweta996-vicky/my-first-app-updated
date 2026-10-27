package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;

public class BaseTest {

    protected static WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        if (driver == null) {
            driver = new ChromeDriver(BasePage.getChromeOptionsSetup());
            driver.get(ConfigReader.get("app.url"));
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
    
    @AfterSuite(alwaysRun = true)
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
