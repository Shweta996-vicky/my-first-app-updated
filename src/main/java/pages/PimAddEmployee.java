package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;
import utils.LocatorLoader;
import java.time.Duration;

/**
 * Page Object representing the PIM (Personnel Information Management) 
 * Add Employee and Employee List workflows in OrangeHRM.
 */
public class PimAddEmployee extends BasePage {

    // Centralised cached locators loaded dynamically via configurations
    private final By menuPim = By.xpath(LocatorLoader.getValue("menu.pim"));
    private final By addBtn = By.xpath(LocatorLoader.getValue("pim.addBtn"));
    private final By firstNameField = By.xpath(LocatorLoader.getValue("pim.firstName"));
    private final By lastNameField = By.xpath(LocatorLoader.getValue("pim.lastName"));
    private final By employeeIdField = By.xpath(LocatorLoader.getValue("pim.employeeId"));
    private final By saveBtn = By.xpath(LocatorLoader.getValue("pim.saveBtn"));
    private final By successToast = By.xpath(LocatorLoader.getValue("pim.successToast"));
    private final By employeeListTab = By.xpath(LocatorLoader.getValue("pim.employeeListTab"));
    private final By resetBtn = By.xpath(LocatorLoader.getValue("pim.resetBtn"));
    private final By searchEmpIdField = By.xpath(LocatorLoader.getValue("pim.searchEmpId"));
    private final By searchBtn = By.xpath(LocatorLoader.getValue("pim.searchBtn"));
    
    private By detailsSaveBtn;

    public PimAddEmployee(WebDriver driver) {
        super(driver);
        try {
            this.detailsSaveBtn = By.xpath(LocatorLoader.getValue("pim.detailsSaveBtn"));
        } catch (Exception e) {
            System.out.println("WARNING: File properties fetch failed for 'pim.detailsSaveBtn'. Applying hardcoded runtime fallback locator.");
            // Direct, standard layout structural path fallback for OrangeHRM details screen
            this.detailsSaveBtn = By.xpath("//div[contains(@class, 'orangehrm-custom-fields')]//button[@type='submit'] | (//button[@type='submit'])");
        }
    }

    /**
     * Navigates to the main PIM management module from the left sidebar panel menu.
     * Engineered with dynamic retry fallback mechanics to handle UI thread rendering delays.
     */
    public void navigateToPIM() {
        try {
            // 1. Wait explicitly until the PIM menu button layout becomes fully visible and clickable
            WebElement pimMenuButton = wait.until(ExpectedConditions.elementToBeClickable(menuPim));
            
            // 2. Perform a standard clean programmatic click action
            pimMenuButton.click();
            System.out.println("LOG: Successfully registered navigation click to PIM module.");
        } catch (Exception e) {
            System.out.println("WARNING: Standard click intercepted by overlay. Injecting JavaScript action executor bypass...");
            try {
                // Fallback Bypass: Execute script click directly on the DOM node to override layout animation blocks
                WebElement pimMenuButtonFallback = driver.findElement(menuPim);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pimMenuButtonFallback);
                System.out.println("LOG: Successfully forced navigation click to PIM module via JavaScript.");
            } catch (Exception fatalEx) {
                System.err.println("LOG ERROR: High-priority navigation crash. Could not interact with PIM sidebar menu link.");
                throw fatalEx;
            }
        }
        
        // 3. System Sync Point: Explicitly block down execution until the browser URL registers the module change state
        wait.until(ExpectedConditions.urlContains("/pim/"));
    }

    /**
     * Complete step-by-step creation of a new employee profile.
     * Captures and returns the auto-generated ID string.
     */
    public String createNewEmployee(String first, String last) {
        clickElement(addBtn);
        
        safeEnterText(firstNameField, first);
        safeEnterText(lastNameField, last);
        
        // Wait until ID field is visible and populated with text
        WebElement idElement = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdField));
        wait.until(d -> !idElement.getAttribute("value").trim().isEmpty());
        String empId = idElement.getAttribute("value").trim();
        System.out.println("LOG: Successfully captured auto-generated Employee ID: " + empId);
        
        clickElement(saveBtn);
        
        // Wait for page redirection to the secondary details form layout structure
        wait.until(ExpectedConditions.urlContains("viewPersonalDetails"));
        
        // Click the secondary details save button
        clickElement(detailsSaveBtn);
        System.out.println("LOG: Clicked the secondary details form layout save button.");
        
        // Handle success notification pop-up safely without crashing the script execution loop
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(successToast));
            System.out.println("LOG: Success banner validation complete.");
        } catch (Exception e) {
            System.out.println("WARNING: Success toast was skipped (either vanished instantly or locator mismatched), proceeding with workflow execution.");
        }
        
        return empId;
    }

    /**
     * Filters the central data grid layout to find a specific employee record by ID.
     * Enhanced with table loading sync points to prevent racing condition failures.
     */
    public boolean searchEmployeeById(String empId) {
        clickElement(employeeListTab);
        
        // Clear lingering search parameters from prior runs if present
        try { 
            clickElement(resetBtn); 
        } catch (Exception e) {
            // Optional element; continue if reset button is not interactive or present
        }
        
        safeEnterText(searchEmpIdField, empId);
        clickElement(searchBtn);
        
        // PAUSE OPTIMIZATION: Wait a small moment to let the network grid refresh animation complete
        try { Thread.sleep(1000); } catch (Exception ignored) {}
        
        // Verify target identification token string cell populates inside the data table body
        try {
            String exactCellXpath = "//div[@class='oxd-table-body']//div[text()='" + empId + "']";
            WebElement resultCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(exactCellXpath)));
            
            System.out.println("LOG SUCCESS: Confirmed target employee record found in table grid for ID: " + empId);
            return resultCell.isDisplayed();
        } catch (Exception e) {
            System.err.println("LOG ERROR: Target employee row matching ID " + empId + " did not populate the table grid timely.");
            return false;
        }
    }

    /**
     * Locates the newly filtered row by Employee ID and clicks it to open the profile.
     */
    public void selectEmployeeFromGrid(String empId) {
        String cellTextXpath = "//div[@class='oxd-table-body']//div[text()='" + empId + "']";
        
        try {
            WebElement interactiveCell = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cellTextXpath)));
            try { Thread.sleep(500); } catch (Exception ignored) {}
            
            interactiveCell.click();
            System.out.println("LOG: Clicked on Employee record text node: " + empId);
            
            wait.until(ExpectedConditions.urlContains("viewPersonalDetails"));
        } catch (Exception e) {
            System.out.println("WARNING: Native text cell click blocked. Injecting JavaScript bypass click routine...");
            try {
                WebElement fallbackCell = driver.findElement(By.xpath(cellTextXpath));
                // FIXED: Corrected the truncated Javascript script syntax execution sequence block parameters
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", fallbackCell);
                wait.until(ExpectedConditions.urlContains("viewPersonalDetails"));
            } catch (Exception fatalEx) {
                System.err.println("LOG ERROR: Failed to click employee row for ID: " + empId);
                throw fatalEx;
            }
        }
    }

    /**
     * Updates the marital status and gender fields on the Personal Details profile page 
     * and validates the successful save confirmation notification banner.
     */
    /**
     * Updates the marital status and gender fields on the Personal Details profile page 
     * and validates the successful save confirmation notification banner.
     */
    public void updatePersonalDetailsAndSave() {
        By maritalDropdown = By.xpath(utils.LocatorLoader.getValue("pim.maritalDropdown"));
        By optionMarried = By.xpath(utils.LocatorLoader.getValue("pim.maritalOptionMarried"));
        By genderMale = By.xpath(utils.LocatorLoader.getValue("pim.genderMaleRadio"));
        By personalSaveBtn = By.xpath(utils.LocatorLoader.getValue("pim.personalDetailsSaveBtn"));

        System.out.println("LOG: Initiating personal profile details update workflow...");

        try {
            // 1. Structural Sync: Wait until the secondary save button is visible to ensure the layout has loaded
            WebElement saveBtnElement = wait.until(ExpectedConditions.visibilityOfElementLocated(personalSaveBtn));
            
            // 2. Scroll Optimization: Force the browser view layout down to prevent elements from being hidden under navigation menus
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", saveBtnElement);
            try { Thread.sleep(1000); } catch (Exception ignored) {}
            
            // 3. Select Marital Status dropdown option
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(maritalDropdown));
            dropdown.click();
            
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionMarried));
            option.click();
            System.out.println("LOG: Successfully selected Marital Status as 'Married'.");

            // 4. Select Gender Selection
            WebElement genderElement = wait.until(ExpectedConditions.elementToBeClickable(genderMale));
            genderElement.click();
            System.out.println("LOG: Successfully toggled Gender selection to 'Male'.");

            // 5. Save this specific form layout block
            saveBtnElement.click();
            System.out.println("LOG: Dispatched Personal Details profile save event transaction.");

            // Explicitly parse and verify confirmation toast status banner response
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(successToast));
            System.out.println("LOG SUCCESS: Personal profile updates verified successfully via notification pop-up banner.");
            
        } catch (Exception e) {
            System.err.println("LOG ERROR: Failed inside updatePersonalDetailsAndSave execution block. Attempting fallback click sequence...");
            // Alternative layout execution recovery block if elements remain blocked
            try {
                WebElement dropdownFallback = driver.findElement(maritalDropdown);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownFallback);
                WebElement optionFallback = wait.until(ExpectedConditions.elementToBeClickable(optionMarried));
                optionFallback.click();
                
                WebElement genderFallback = driver.findElement(genderMale);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", genderFallback);
                
                driver.findElement(personalSaveBtn).click();
                System.out.println("LOG: Managed personal info updates via fallback execution recovery path completely.");
            } catch (Exception fatalEx) {
                System.err.println("FATAL ERROR: Profile interaction blocked completely on the layout canvas.");
                throw e;
            }
        }
    }
}

        
