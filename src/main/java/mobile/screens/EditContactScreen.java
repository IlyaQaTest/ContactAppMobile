package mobile.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import mobile.model.Contact;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Represents the "Edit Contact" screen in the mobile application.
 * Provides methods to update existing contact details.
 */
public class EditContactScreen extends BaseScreen {

    private static final Logger logger = LoggerFactory.getLogger(EditContactScreen.class);

    public EditContactScreen(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(id = "com.sheygam.contactapp:id/inputName")
    private WebElement inputName;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/inputLastName")
    private WebElement inputLastName;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/inputEmail")
    private WebElement inputEmail;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/inputPhone")
    private WebElement inputPhone;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/inputAddress")
    private WebElement inputAddress;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/inputDesc")
    private WebElement inputDescription;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/updateBtn")
    private WebElement btnUpdate;

    /**
     * Fills the edit contact form with updated data.
     * Hides the software keyboard before accessing elements at the bottom of the screen.
     *
     * @param contact the contact object containing new details
     */
    @Step("Fill Edit Contact form with name: {contact.name}")
    public void typeEditContactForm(Contact contact) {
        logger.info("Editing contact form for: {}", contact.getName());
        type(inputName, contact.getName());
        type(inputLastName, contact.getLastName());
        type(inputEmail, contact.getEmail());
        type(inputPhone, contact.getPhone());
        type(inputAddress, contact.getAddress());

        // Hide soft keyboard to expose inputDescription
        hideKeyboardSafely();

        type(inputDescription, contact.getDescription());
    }

    /**
     * Clicks the "Update" button to save changes.
     */
    @Step("Click 'Update' button")
    public void clickBtnUpdate() {
        logger.info("Clicking 'Update' button to save contact changes");
        hideKeyboardSafely();
        click(btnUpdate);
    }

    @Step("Click on Name input field")
    public void clickFieldInputName() {
        click(inputName);
    }

    @Step("Check if error message '{expectedText}' is displayed on screen")
    public boolean isErrorMessageDisplayed(String expectedText) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement errorElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(AppiumBy.id("android:id/message"))
            );
            String actualText = errorElement.getText();
            logger.info("Error message displayed: {}", actualText);
            return actualText.toLowerCase().contains(expectedText.toLowerCase());
        } catch (TimeoutException e) {
            logger.warn("Error message not found or not visible: {}", e.getMessage());
            return false;
        }
    }

    @Step("Check if Edit Contact screen is still displayed")
    public boolean isEditScreenDisplayed() {
        return isElementPresent(btnUpdate, 15);
    }

    @Step("Clear the Name field on Edit Contact screen")
    public void clearName() {
        logger.info("Clearing Name field");
        click(inputName);
        inputName.clear();
    }

    /**
     * Safely hides the soft keyboard if it is currently visible.
     */
    private void hideKeyboardSafely() {
        try {
            if (driver instanceof HidesKeyboard) {
                ((HidesKeyboard) driver).hideKeyboard();
                logger.debug("Soft keyboard hidden successfully");
            }
        } catch (Exception e) {
            logger.debug("Soft keyboard was not open or could not be hidden: {}", e.getMessage());
        }
    }
}