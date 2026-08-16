package mobile;

import io.qameta.allure.Step;
import mobile.model.User;
import mobile.screens.ContactListScreen;
import mobile.screens.LoginRegistrationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static mobile.config.PropertiesReader.getProperty;


/**
 * Mobile tests for user login functionality.
 * Includes positive and negative scenarios for authentication.
 */
//@Test(enabled = true)
public class LoginTests extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(LoginTests.class);
    private static final String APP_PACKAGE = "com.sheygam.contactapp";

    @BeforeMethod
    @Step("Open login/registration screen before each test")
    public void openAuthScreen() {
        logger.info("Opening Login/Registration screen...");
        loginRegistrationScreen = new LoginRegistrationScreen(driver);
    }

    @AfterMethod(alwaysRun = true)
    @Step("Reset application state after each test")
    public void postCondition() {
        if (driver != null) {
            try {
                logger.info("Restarting application in postCondition...");
                driver.terminateApp(APP_PACKAGE);
                driver.activateApp(APP_PACKAGE);

                // CI emulator needs time to stabilize after restart
                Thread.sleep(2000);

                logger.info("Application restarted successfully.");
            } catch (Exception e) {
                logger.warn("Failed to restart app in postCondition: {}", e.getMessage());
            }
        }
    }

    @Test(enabled = true, description = "Positive test: Successful login and transition to contact list screen")
    @Step("Login with valid credentials and verify contact list is displayed")
    public void loginPositiveTest() {
        logger.info("Starting test: loginPositiveTest");

        User user = new User(
                getProperty("base.properties", "login"),
                getProperty("base.properties", "password")
        );

        loginRegistrationScreen.typeLoginRegistrationForm(user);
        loginRegistrationScreen.clickBtnLogin();

        contactListScreen = new ContactListScreen(driver);

        boolean isLoaded = contactListScreen.isContactListDisplayed();
        logger.info("Contact list screen displayed: {}", isLoaded);

        Assert.assertTrue(isLoaded, "Contact List screen should be displayed after login");
    }

    @Test(enabled = true, description = "Positive test: Verify '+' button is visible after login")
    @Step("Login and check if '+' button is present on contact list screen")
    public void loginPositiveBtnPlusTest() {
        logger.info("Starting test: loginPositiveBtnPlusTest");

        User user = new User(
                getProperty("base.properties", "login"),
                getProperty("base.properties", "password")
        );

        loginRegistrationScreen.typeLoginRegistrationForm(user);
        loginRegistrationScreen.clickBtnLogin();

        contactListScreen = new ContactListScreen(driver);

        Assert.assertTrue(contactListScreen.isBtnPlusPresent(),
                "Plus button should be visible after login");
    }

//    @Test(enabled = false, description = "Negative test: Empty password")
//    @Step("Attempt login with empty password and verify error message")
//    public void loginNegativeEmptyPasswordTest() {
//        logger.info("Starting test: loginNegativeEmptyPasswordTest");
//
//        User user = new User(getProperty("base.properties", "login"), "");
//
//        loginRegistrationScreen.typeLoginRegistrationForm(user);
//        loginRegistrationScreen.clickBtnLogin();
//
//        Assert.assertTrue(new ErrorScreen(driver)
//                        .validateTextInError("Login or Password incorrect", 15),
//                "Error message not displayed for empty password");
//    }
//
//    @Test(enabled = false, description = "Negative test: Empty login field")
//    @Step("Attempt login with empty login and verify error message")
//    public void loginNegativeEmptyLoginTest() {
//        logger.info("Starting test: loginNegativeEmptyLoginTest");
//
//        User user = new User("", getProperty("base.properties", "password"));
//
//        loginRegistrationScreen.typeLoginRegistrationForm(user);
//        loginRegistrationScreen.clickBtnLogin();
//
//        Assert.assertTrue(new ErrorScreen(driver)
//                        .validateTextInError("Login or Password incorrect", 15),
//                "Error message not displayed for empty login");
//    }
//
//    @Test(enabled = false, description = "Negative test: Empty fields")
//    @Step("Attempt login with empty fields and verify error message")
//    public void loginNegativeEmptyFieldsTest() {
//        logger.info("Starting test: loginNegativeEmptyFieldsTest");
//
//        loginRegistrationScreen.clickBtnLogin();
//
//        Assert.assertTrue(new ErrorScreen(driver)
//                        .validateTextInError("Login or Password incorrect", 15),
//                "Error message not displayed for empty fields");
//    }
//
//    @Test(enabled = false, description = "Negative test: Wrong email with space")
//    @Step("Attempt login with email containing space and verify error message")
//    public void loginNegativeWrongEmailWithSpaceTest() {
//        logger.info("Starting test: loginNegativeWrongEmailWithSpaceTest");
//
//        User user = new User(" ", getProperty("base.properties", "password"));
//
//        loginRegistrationScreen.typeLoginRegistrationForm(user);
//        loginRegistrationScreen.clickBtnLogin();
//
//        Assert.assertTrue(new ErrorScreen(driver)
//                        .validateTextInError("Login or Password incorrect", 15),
//                "Error message not displayed for invalid email format");
//    }
}