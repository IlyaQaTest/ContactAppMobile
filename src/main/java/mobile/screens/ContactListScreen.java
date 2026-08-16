package mobile.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import mobile.helpers.Direction;
import mobile.helpers.SwipeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Represents the Contact List screen in the mobile application.
 * Provides methods for managing contacts, refreshing the list, and performing swipe actions.
 */
public class ContactListScreen extends BaseScreen implements SwipeUtils {

    private static final Logger logger = LoggerFactory.getLogger(ContactListScreen.class);

    public ContactListScreen(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='More options']")
    private WebElement moreOptions;

    @AndroidFindBy(xpath = "//*[@text='Contact list']")
    private WebElement title;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/add_contact_btn")
    private WebElement addContactBtn;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/rowContainer")
    private List<WebElement> contactRows;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement btnYes;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/emptyTxt")
    private WebElement noContacts;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.sheygam.contactapp:id/title' and @text='Logout']")
    private WebElement btnLogout;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.sheygam.contactapp:id/title' and @text='Date picker']")
    private WebElement datePicker;

    @Step("Click on contact name: {nameText}")
    public void clickRowName(String nameText) {
        logger.info("Clicking contact with name: {}", nameText);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement contact = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@text='" + nameText + "']")
        ));
        click(contact);
    }

    @Step("Click 'Date Picker'")
    public void clickDatePicker() {
        logger.info("Clicking 'Date Picker' option");
        click(datePicker);
    }

    @Step("Click 'Logout' button")
    public void clickBtnLogout() {
        logger.info("Clicking 'Logout' button");
        click(btnLogout);
    }

    @Step("Click 'More Options' menu")
    public void clickMoreOptions() {
        logger.info("Opening 'More Options' menu");
        click(moreOptions);
    }

    @Step("Click '+' button to add contact")
    public void clickBtnPlus() {
        logger.info("Clicking 'Add Contact' button");
        click(addContactBtn);
    }

    @Step("Check if '+' button is present")
    public boolean isBtnPlusPresent() {
        return isElementPresent(addContactBtn, 15);
    }

    @Step("Check if Contact List screen is displayed")
    public boolean isContactListDisplayed() {
        logger.info("Checking if Contact List screen is displayed (title locator)");
        boolean titlePresent = isElementPresent(title, 15);
        if (titlePresent) {
            logger.info("Contact List title found");
            return true;
        }
        logger.info("Title not found, checking for Add Contact button as fallback");
        boolean addBtnPresent = isElementPresent(addContactBtn, 5);
        logger.info("Add Contact button present: {}", addBtnPresent);
        return addBtnPresent;
    }

    @Step("Check if contact list is empty")
    public boolean isContactListEmpty() {
        return contactRows.isEmpty();
    }

    @Step("Validate text '{expectedText}' in Contact List screen after registration")
    public boolean validateTextInContactListScreenAfterRegistration(String expectedText, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@text='" + expectedText + "']")
            ));
            return element.isDisplayed();
        } catch (TimeoutException e) {
            logger.warn("Text '{}' not found on Contact List screen: {}", expectedText, e.getMessage());
            return false;
        }
    }

    /**
     * Verifies presence of dynamic Toast messages safely.
     * Uses direct android.widget.Toast class lookup to catch short-lived system toasts.
     *
     * @param toastText        expected substring in the Toast notification
     * @param timeoutInSeconds maximum wait time in seconds
     * @return true if the Toast message was detected in DOM, false otherwise
     */
    @Step("Verify Toast message containing '{toastText}' is present")
    public boolean isToastMessagePresent(String toastText, int timeoutInSeconds) {
        logger.debug("Waiting for toast message containing text: {}", toastText);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.Toast[contains(@text, '" + toastText + "')]")
            ));
            logger.info("Toast message containing '{}' was successfully found", toastText);
            return true;
        } catch (TimeoutException e) {
            logger.warn("Toast message containing '{}' was not displayed within {} seconds", toastText, timeoutInSeconds);
            return false;
        }
    }

    public boolean isTextInMessageContactWasAddedPresent(String text, int time) {
        return isToastMessagePresent(text, time);
    }

    public boolean isTextInMessageContactWasUpdatedPresent(String text, int time) {
        return isToastMessagePresent(text, time);
    }

    @Step("Delete first contact from list")
    public void deleteFirstContact() {
        logger.info("Deleting first contact (swiping LEFT to RIGHT)");
        waitForContactListNotEmpty();
        swipeInsideElement(driver, contactRows.get(0), Direction.RIGHT);
        click(btnYes);
    }

    @Step("Delete middle contact from list")
    public void deleteContactMiddle() {
        waitForContactListNotEmpty();
        int middleIndex = contactRows.size() / 2;
        logger.info("Deleting middle contact at index {} (swiping LEFT to RIGHT)", middleIndex);
        swipeInsideElement(driver, contactRows.get(middleIndex), Direction.RIGHT);
        click(btnYes);
    }

    @Step("Delete last contact from list")
    public void deleteLastContact() {
        waitForContactListNotEmpty();
        logger.info("Deleting last contact (swiping LEFT to RIGHT)");
        WebElement last = contactRows.get(contactRows.size() - 1);
        swipeInsideElement(driver, last, Direction.RIGHT);
        click(btnYes);
    }

    @Step("Edit first contact")
    public void editFirstContact() {
        logger.info("Editing first contact (swiping RIGHT to LEFT)");
        waitForContactListNotEmpty();
        swipeInsideElement(driver, contactRows.get(0), Direction.LEFT);
    }

    public void waitForContactListNotEmpty() {
        logger.debug("Waiting for contact list to be populated");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(d -> !contactRows.isEmpty());
    }

    @Step("Get contact name at index {index}")
    public String getContactName(int index) {
        logger.info("Getting contact name at index {}", index);
        waitForContactListNotEmpty();
        By contactNameLocator = By.id("com.sheygam.contactapp:id/rowName");
        List<WebElement> contacts = driver.findElements(contactNameLocator);
        return contacts.get(index).getText();
    }

    public WebElement getContact(int index) {
        logger.info("Getting contact WebElement at index {}", index);
        waitForContactListNotEmpty();
        return contactRows.get(index);
    }
}