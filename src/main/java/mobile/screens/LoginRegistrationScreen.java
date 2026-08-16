package mobile.screens;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.pagefactory.AndroidFindBy;
import mobile.model.User;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the login and registration screen in the mobile application.
 * Provides methods to enter user credentials and perform authentication actions.
 */
public class LoginRegistrationScreen extends BaseScreen {

    private static final Logger logger = LoggerFactory.getLogger(LoginRegistrationScreen.class);

    public LoginRegistrationScreen(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(id = "com.sheygam.contactapp:id/inputEmail")
    private WebElement inputEmail;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/inputPassword")
    private WebElement inputPassword;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/regBtn")
    private WebElement btnRegistration;

    @AndroidFindBy(id = "com.sheygam.contactapp:id/loginBtn")
    private WebElement btnLogin;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Authentication']")
    private WebElement authTitle;

    /**
     * Checks if the authentication title is displayed on the screen.
     *
     * @return true if the title is visible, false otherwise
     */
    public boolean isTextAuthenticationDisplayed() {
        return isElementPresent(authTitle, 15);
    }

    /**
     * Fills the login or registration form with user credentials.
     *
     * @param user user object containing username and password
     */
    public void typeLoginRegistrationForm(User user) {
        logger.info("Entering user credentials for: {}", user.getUsername());
        type(inputEmail, user.getUsername());
        type(inputPassword, user.getPassword());
        hideSoftKeyboard();
    }

    /**
     * Clicks the registration button to create a new account.
     */
    public void clickBtnRegistration() {
        logger.info("Clicking 'Registration' button");
        hideSoftKeyboard();
        click(btnRegistration);
    }

    /**
     * Checks if the login/registration form is displayed.
     *
     * @return true if the form is visible, false otherwise
     */
    public boolean isLoginRegistrationFormDisplayed() {
        return isElementPresent(inputEmail, 15);
    }

    /**
     * Clicks the login button to authenticate the user.
     */
    public void clickBtnLogin() {
        logger.info("Clicking 'Login' button");
        hideSoftKeyboard();
        click(btnLogin);
    }

    /**
     * Safely hides the soft keyboard if it is currently visible.
     */
    private void hideSoftKeyboard() {
        try {
            if (driver instanceof HidesKeyboard) {
                ((HidesKeyboard) driver).hideKeyboard();
            }
        } catch (Exception e) {
            logger.debug("Soft keyboard was not open or could not be hidden: {}", e.getMessage());
        }
    }
}