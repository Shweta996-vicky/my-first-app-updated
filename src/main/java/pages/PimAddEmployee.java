package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import base.BasePage;
import utils.LocatorLoader;

public class PimAddEmployee extends BasePage {

    private final By menuPim = locator("menu.pim");
    private final By addBtn = locator("pim.addBtn");
    private final By firstNameField = locator("pim.firstName");
    private final By lastNameField = locator("pim.lastName");
    private final By employeeIdField = locator("pim.employeeId");
    private final By saveBtn = locator("pim.saveBtn");
    private final By successToast = locator("pim.successToast");
    private final By employeeListTab = locator("pim.employeeListTab");
    private final By resetBtn = locator("pim.resetBtn");
    private final By searchEmpIdField = locator("pim.searchEmpId");
    private final By searchBtn = locator("pim.searchBtn");
    private final By detailsSaveBtn = locator("pim.detailsSaveBtn");

    private final By licenseNoField = locator("pim.licenseNoField");
    private final By firstNameRequired = locator("pim.firstNameRequiredError");
    private final By lastNameRequired = locator("pim.lastNameRequiredError");

    private final By maritalDropdown = locator("pim.maritalDropdown");
    private final By optionMarried = locator("pim.maritalOptionMarried");
    private final By genderMaleRadio = locator("pim.genderMaleRadio");
    private final By maritalSelected = locator("pim.maritalSelected");
    private final By genderMaleInput = locator("pim.genderMaleInput");
    private final By personalSaveBtn = locator("pim.personalDetailsSaveBtn");

    private final By dateOfBirthField = By.xpath("//label[text()='Date of Birth']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By idCollisionSpanError = By.xpath("//span[contains(@class,'oxd-input-field-error-message') and text()='Employee Id already exists']");
    private final By deleteIconFromRow = locator("pim.deleteIconFromRow");
    private final By confirmDeleteBtn = locator("pim.confirmDeleteBtn");
    private final By noRecordsFoundText = locator("pim.noRecordsFoundText");
    private final By formLoaderOverlay = By.className("oxd-form-loader");

    private final WebDriverWait explicitWait;

    public PimAddEmployee(WebDriver driver) {
        super(driver);
        explicitWait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private By locator(String key) {
        return By.xpath(LocatorLoader.getValue(key));
    }

    private By employeeCell(String employeeId) {
        return By.xpath("//div[@role='row' and contains(., '" + employeeId + "')]");
    }

    private By employeeCheckbox(String employeeId) {
        return By.xpath("//div[@role='row' and contains(., '" + employeeId + "')]//div[@role='cell']//input[@type='checkbox']/following-sibling::span");
    }

    public void navigateToPIM() {
        clickElement(menuPim);
        wait.until(ExpectedConditions.urlContains("/pim/"));
    }

    public void openAddEmployeeForm() {
        clickElement(addBtn);
    }

    private void clearWithKeyboard(By locator) {
        WebElement element = explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        String os = System.getProperty("os.name").toLowerCase();
        Keys modifier = os.contains("mac") ? Keys.COMMAND : Keys.CONTROL;
        element.sendKeys(Keys.chord(modifier, "a"));
        element.sendKeys(Keys.BACK_SPACE);
    }

    public void clickSaveOnAddEmployee() {
        clickElement(saveBtn);
    }

    public boolean isRequiredFieldsErrorDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOfElementLocated(formLoaderOverlay));
        } catch (Exception e) {
        }
        return isElementDisplayed(firstNameRequired) && isElementDisplayed(lastNameRequired);
    }

    public String getGeneratedEmployeeId() {
        WebElement idField = explicitWait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdField));
        return idField.getAttribute("value").trim();
    }

    public void enterEmployeeNames(String firstName, String lastName) {
        safeEnterText(firstNameField, firstName);
        safeEnterText(lastNameField, lastName);
    }

    public String saveEmployeeWithCollisionHandling() {
        WebElement idField = explicitWait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdField));
        String activeId = idField.getAttribute("value").trim();
        clickElement(saveBtn);
        try {
            boolean hasCollision = new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.visibilityOfElementLocated(idCollisionSpanError))
                    .isDisplayed();
            if (hasCollision) {
                long incrementedId = Long.parseLong(activeId) + 1;
                activeId = String.valueOf(incrementedId);
                clearWithKeyboard(employeeIdField);
                safeEnterText(employeeIdField, activeId);
                clickElement(saveBtn);
            }
        } catch (TimeoutException e) {
        }
        explicitWait.until(ExpectedConditions.urlContains("viewPersonalDetails"));
        return activeId;
    }

    public void clickSaveOnPersonalDetailsPage() {
        clickSaveButton(detailsSaveBtn);
        waitForSuccessToast();
    }

    public void clickSaveOnPersonalDetailsWithoutToastCheck() {
        clickSaveButton(detailsSaveBtn);
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOfElementLocated(formLoaderOverlay));
        } catch (Exception e) {
        }
    }

    public void navigateToEmployeeList() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOfElementLocated(formLoaderOverlay));
        } catch (Exception e) {
        }
        // Fixed syntax: Uses zero-indexed arguments array mapping to click cleanly via JavaScript
        WebElement tabElement = explicitWait.until(ExpectedConditions.elementToBeClickable(employeeListTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tabElement);
    }

    public void searchByEmployeeId(String employeeId) {
        try { clickElement(resetBtn); } catch (TimeoutException e) {}
        safeEnterText(searchEmpIdField, employeeId);
        clickElement(searchBtn);
    }

    public void clickOnMatchingEmployee(String employeeId) {
        WebElement cell = explicitWait.until(ExpectedConditions.elementToBeClickable(employeeCell(employeeId)));
        cell.click();
        explicitWait.until(ExpectedConditions.urlContains("viewPersonalDetails"));
    }

    public void updateDemographicsAndIdentifiers(String specialCharLicense) {
        explicitWait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(licenseNoField));

        clickElement(maritalDropdown);
        clickElement(optionMarried);
        clickElement(genderMaleRadio);

        clearWithKeyboard(licenseNoField);
        safeEnterText(licenseNoField, specialCharLicense);

        String formattedTodayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        clearWithKeyboard(dateOfBirthField);
        safeEnterText(dateOfBirthField, formattedTodayDate);

        clickSaveButton(personalSaveBtn);
        waitForSuccessToast();
    }

    public String getMaritalStatus() {
        return getElementText(maritalSelected).trim();
    }

    public boolean isMaleSelected() {
        return driver.findElement(genderMaleInput).isSelected();
    }

    public void selectEmployeeCheckbox(String employeeId) {
        WebElement checkbox = explicitWait.until(ExpectedConditions.elementToBeClickable(employeeCheckbox(employeeId)));
        checkbox.click();
    }

    public void deleteSelectedEmployee() {
        clickElement(deleteIconFromRow);
        By textConfirmBtnXpath = By.xpath("//button[contains(.,'Yes, Delete')]");
        WebElement targetConfirmBtn = explicitWait.until(ExpectedConditions.presenceOfElementLocated(textConfirmBtnXpath));
        // Fixed syntax: Uses zero-indexed arguments array mapping to execute click cleanly
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetConfirmBtn);
        waitForSuccessToast();
    }

    public boolean isRecordDeletedSuccessfully() {
        return isElementDisplayed(noRecordsFoundText);
    }

    private void clickSaveButton(By buttonLocator) {
        WebElement button = explicitWait.until(ExpectedConditions.presenceOfElementLocated(buttonLocator));
        // Fixed syntax: Uses zero-indexed arguments array mapping to handle center layout scroll
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);
        clickElement(buttonLocator);
    }

    private void waitForSuccessToast() {
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(successToast));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(successToast));
    }
}
