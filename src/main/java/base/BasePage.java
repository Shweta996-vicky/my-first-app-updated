package base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Restores the browser configuration setup profile required by the test class initialization step.
     */
    public static ChromeOptions getChromeOptionsSetup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        // You can add arguments like "--headless" here later if you run via CI servers
        return options;
    }

    /**
     * Pauses execution for a specified number of milliseconds to visually slow down the run.
     * Adjusted to keep the automation deliberately at a slower pace for analysis.
     */
    protected void visualDelay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Shared custom click method updated with distinct pace stabilizers.
     */
    public void clickElement(By locator) {
        // Wait until target is ready in the DOM
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        
        // 1. Long pause BEFORE clicking so you can see exactly where the cursor is going
        visualDelay(1500); 
        
        element.click();
        
        // 2. Pause AFTER clicking so you can watch the application's page change transition
        visualDelay(1000); 
    }

    /**
     * Shared custom text entry method updated with typing speed limiters.
     */
    public void safeEnterText(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        
        // 1. Pause before starting to interact with the input field box
        visualDelay(1000); 
        
        // 2. Character-by-character slow typing animation to watch the input populate realistically
        for (char ch : text.toCharArray()) {
            element.sendKeys(String.valueOf(ch));
            visualDelay(150); // 150ms delay between every single letter typed
        }
        
        // 3. Pause after typing completion to review the final string layout values
        visualDelay(1000); 
    }
}
