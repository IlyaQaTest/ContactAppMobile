package mobile.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the "Update Contact" screen in the mobile application.
 * Provides methods to modify contact details and confirm updates.
 */
public class UpdateContactScreen extends BaseScreen {

    private static final Logger logger = LoggerFactory.getLogger(UpdateContactScreen.class);

    public UpdateContactScreen(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(id = "com.sheygam.contactapp:id/updateBtn")
    private WebElement btnUpdate;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/inputName")
    private WebElement inputName;

    /**
     * Clears the name field before entering a new value.
     */
    @Step("Clear contact name field")
    public void clearName() {
        logger.info("Clearing contact name field");
        click(inputName);
        inputName.clear();
    }

    /**
     * Types a new name into the contact name field and hides the soft keyboard.
     *
     * @param name the new contact name
     */
    @Step("Type new contact name: {name}")
    public void typeName(String name) {
        logger.info("Typing new contact name: {}", name);
        type(inputName, name);
        hideKeyboardSafely();
    }

    /**
     * Clicks the "Update" button to save changes.
     */
    @Step("Click 'Update' button")
    public void clickUpdateBtn() {
        logger.info("Clicking 'Update' button to save contact changes");
        hideKeyboardSafely();
        click(btnUpdate);
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