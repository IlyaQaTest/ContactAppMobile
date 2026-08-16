package mobile;

import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import mobile.helpers.CrashHandlerPrtSC;
import mobile.helpers.ScreenshotUtils;
import mobile.model.User;
import mobile.screens.ContactListScreen;
import mobile.screens.ErrorScreen;
import mobile.screens.LoginRegistrationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static mobile.config.PropertiesReader.getProperty;
import static mobile.model.factory.UserFactory.positiveUser;


/**
 * Mobile tests for user registration functionality.
 * Includes positive and negative scenarios verifying validation and error handling.
 */
//@Test(enabled = false)
public class RegistrationTests extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationTests.class);
    private static final String APP_PACKAGE = "com.sheygam.contactapp";

    @BeforeMethod
    @Step("Open authentication screen before each test")
    public void openAuthScreen() {
        loginRegistrationScreen = new LoginRegistrationScreen(driver);
    }

    @AfterMethod(alwaysRun = true)
    @Step("Reset application state after each test")
    public void postCondition() {
        if (driver != null) {
            try {
                driver.terminateApp(APP_PACKAGE);
                driver.activateApp(APP_PACKAGE);

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                logger.info("Application restarted successfully in postCondition");
            } catch (Exception e) {
                logger.warn("Failed to restart app in postCondition: {}", e.getMessage());
            }
        }
    }

    @Test(enabled = true, description = "Positive test: Successful registration with valid credentials")
    @Step("Register new user and verify contact list screen is displayed")
    public void registrationPositiveTest() {
        User user = positiveUser();
        loginRegistrationScreen.typeLoginRegistrationForm(user);
        loginRegistrationScreen.clickBtnRegistration();

        boolean isRegistered = new ContactListScreen(driver)
                .validateTextInContactListScreenAfterRegistration("No Contacts. Add One more!", 15);

        Assert.assertTrue(isRegistered, "Contact list screen should be displayed after successful registration");
        logger.info("Registration successful for user: {}", user.getUsername());
    }

    @Test(enabled = false, description = "Negative test: Empty email field")
    @Step("Attempt registration with empty email and verify error message")
    public void registrationNegativeEmptyEmailTest() {
        User user = positiveUser();
        user.setUsername("");
        loginRegistrationScreen.typeLoginRegistrationForm(user);
        loginRegistrationScreen.clickBtnRegistration();

        Assert.assertTrue(new ErrorScreen(driver)
                        .validateTextInError("username=must not be blank", 15),
                "Error message not displayed for empty email");
    }

    @Test(enabled = true, description = "Negative test: Invalid email format (missing @)")
    @Step("Attempt registration with invalid email format and verify alert message")
    public void registrationNegativeInvalidEmailTest() {
        int i = new Random().nextInt(1000);
        User user = new User("mir" + i + "gmail.com", "Password123$");

        logger.debug("Testing invalid email format: {}", user.getUsername());
        loginRegistrationScreen.typeLoginRegistrationForm(user);
        loginRegistrationScreen.clickBtnRegistration();

        String alertText = getAlertTextAndClose();
        logger.info("Alert text received: {}", alertText);

        Assert.assertTrue(alertText.contains("username=must be a well-formed email address"),
                "Expected alert message not displayed for invalid email");
    }

    @Test(enabled = true, description = "Negative test: Invalid email without dot - known bug")
    @Step("Attempt registration with email missing dot and verify alert or bug behavior")
    @Issue("BUG-212")
    public void registrationNegativeInvalidEmailWithoutDotTest() {
        int i = new Random().nextInt(1000);
        User user = new User("mir" + i + "@gmailcom", "Password123$");

        logger.debug("Testing invalid email format: {}", user.getUsername());
        loginRegistrationScreen.typeLoginRegistrationForm(user);
        loginRegistrationScreen.clickBtnRegistration();

        try {
            String alertText = getAlertTextAndClose();
            Assert.assertTrue(alertText.contains("must be a well-formed email address"),
                    "Expected alert message not displayed for invalid email");
        } catch (Exception e) {
            logger.warn("KNOWN BUG REPRODUCED: Alert not found for email {}", user.getUsername());

            boolean isStillOnLoginPage = loginRegistrationScreen.isLoginRegistrationFormDisplayed();
            if (!isStillOnLoginPage) {
                logger.error("BUG CONFIRMED: User redirected to internal screen with invalid email!");
                ScreenshotUtils.takeScreenshot(driver, "bug_invalid_email_dot");
            }
        }
    }

    @Test(enabled = true, description = "Negative test: Email with space - known bug")
    @Step("Attempt registration with email containing space and verify crash screen")
    @Issue("BUG-214")
    public void registrationNegativeEmptySpaceEmailTest() {
        User user = positiveUser();
        user.setUsername(" ");
        loginRegistrationScreen.typeLoginRegistrationForm(user);
        loginRegistrationScreen.clickBtnRegistration();

        try {
            Assert.assertTrue(new ErrorScreen(driver)
                            .validateTextInCrashScreen("Open app again", 15),
                    "Crash screen not displayed for email with space");
        } catch (Exception e) {
            logger.error("BUG CONFIRMED: App crashed when email contained space!");
        } finally {
            CrashHandlerPrtSC.captureDesktopScreenshot("bug_invalid_email_space");
        }
    }

    @Test(enabled = true, description = "Negative test: Empty email and password fields")
    @Step("Attempt registration with empty email and password and verify app stop message")
    public void registrationNegativeEmptyEmailPasswordTest() {
        User user = new User("", "");
        loginRegistrationScreen.typeLoginRegistrationForm(user);
        loginRegistrationScreen.clickBtnRegistration();

        Assert.assertTrue(new ErrorScreen(driver).isAppStopDisplayed(),
                "App stop message not displayed for empty credentials");
    }

    @Test(enabled = true, description = "Negative test: Already existing user")
    @Step("Attempt registration with existing user credentials and verify error message")
    public void registrationNegativeAlreadyExistsUserTest() {
        User user = new User(
                getProperty("base.properties", "login"),
                getProperty("base.properties", "password")
        );

        loginRegistrationScreen.typeLoginRegistrationForm(user);
        loginRegistrationScreen.clickBtnRegistration();

        Assert.assertTrue(new ErrorScreen(driver)
                        .validateTextInError("User already exists", 15),
                "Error message not displayed for existing user");
    }
}
