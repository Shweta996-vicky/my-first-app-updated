package base;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private static final long BEFORE_CLICK_MS = 400;
    private static final long AFTER_CLICK_MS = 600;
    private static final long BEFORE_TYPING_MS = 300;
    private static final long BETWEEN_CHARACTERS_MS = 50;
    private static final long AFTER_TYPING_MS = 400;
    private static final long AFTER_CHECK_MS = 200;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public static ChromeOptions getChromeOptionsSetup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        return options;
    }

    private void visualDelay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Visual delay interrupted", e);
        }
    }

    public void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        visualDelay(BEFORE_CLICK_MS);
        try {
            element.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException | org.openqa.selenium.StaleElementReferenceException e) {
            try { Thread.sleep(500); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            WebElement fallbackElement = wait.until(ExpectedConditions.elementToBeClickable(locator));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", fallbackElement);
        }
        visualDelay(AFTER_CLICK_MS);
    }

    public void safeEnterText(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        
        String os = System.getProperty("os.name").toLowerCase();
        org.openqa.selenium.Keys modifier = os.contains("mac") ? org.openqa.selenium.Keys.COMMAND : org.openqa.selenium.Keys.CONTROL;
        
        element.sendKeys(org.openqa.selenium.Keys.chord(modifier, "a"));
        element.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        element.clear();
        
        visualDelay(BEFORE_TYPING_MS);
        if (text != null && !text.isEmpty()) {
            for (char character : text.toCharArray()) {
                element.sendKeys(String.valueOf(character));
                visualDelay(BETWEEN_CHARACTERS_MS);
            }
        }
        visualDelay(AFTER_TYPING_MS);
    }

    public String getElementText(By locator) {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
        visualDelay(AFTER_CHECK_MS);
        return text;
    }

    public boolean isElementDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementEnabled(By locator) {
        boolean enabled = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isEnabled();
        visualDelay(AFTER_CHECK_MS);
        return enabled;
    }

    public void waitForUrlContains(String expectedUrl) {
        wait.until(ExpectedConditions.urlContains(expectedUrl));
    }
}
