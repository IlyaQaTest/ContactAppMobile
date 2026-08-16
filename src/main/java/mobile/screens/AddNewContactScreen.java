package mobile.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import mobile.model.Contact;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the "Add New Contact" screen in the mobile application.
 * Provides methods to fill in contact details and submit the form.
 */
public class AddNewContactScreen extends BaseScreen {

    private static final Logger logger = LoggerFactory.getLogger(AddNewContactScreen.class);

    public AddNewContactScreen(AppiumDriver driver) {
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

    @AndroidFindBy(id = "com.sheygam.contactapp:id/createBtn")
    private WebElement btnCreate;

    /**
     * Fills the contact form with data from the provided Contact object.
     * Hides the software keyboard before accessing elements at the bottom of the screen.
     *
     * @param contact the contact data to input
     */
    @Step("Fill Add Contact form with details for: {contact.name}")
    public void typeContactForm(Contact contact) {
        logger.info("Typing contact form for: {}", contact.getName());
        type(inputName, contact.getName());
        type(inputLastName, contact.getLastName());
        type(inputEmail, contact.getEmail());
        type(inputPhone, contact.getPhone());
        type(inputAddress, contact.getAddress());

        // Hide soft keyboard to make inputDescription visible
        hideKeyboardSafely();

        type(inputDescription, contact.getDescription());
    }

    /**
     * Clicks the "Create" button to submit the form.
     */
    @Step("Click 'Create' button to save new contact")
    public void clickBtnCreate() {
        logger.info("Clicking 'Create' button to add new contact");
        hideKeyboardSafely();
        click(btnCreate);
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