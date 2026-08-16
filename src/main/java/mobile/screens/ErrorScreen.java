package mobile.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the error or crash screen in the mobile application.
 * Provides methods to validate error messages and detect app crash states.
 */
public class ErrorScreen extends BaseScreen {

    private static final Logger logger = LoggerFactory.getLogger(ErrorScreen.class);

    public ErrorScreen(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(id = "android:id/message")
    private WebElement textError;

    @AndroidFindBy(id = "android:id/aerr_restart")
    private WebElement crashScreenBtn;

    @AndroidFindBy(id = "android:id/title_template")
    private WebElement appStop;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement btnErrorOk;

    /**
     * Validates that the specified text is present in the error message.
     *
     * @param text expected error text
     * @param time timeout in seconds
     * @return true if the text is found, false otherwise
     */
    @Step("Validate error message text contains: '{text}'")
    public boolean validateTextInError(String text, int time) {
        logger.info("Validating error message text: '{}'", text);
        int effectiveTimeout = Math.max(time, 10);
        return isTextInElementPresent(textError, text, effectiveTimeout);
    }

    /**
     * Validates that the specified text is present in the crash screen message.
     *
     * @param text expected crash message text
     * @param time timeout in seconds
     * @return true if the text is found, false otherwise
     */
    @Step("Validate crash screen text contains: '{text}'")
    public boolean validateTextInCrashScreen(String text, int time) {
        logger.info("Validating crash screen text: '{}'", text);
        int effectiveTimeout = Math.max(time, 10);
        return isTextInElementPresent(crashScreenBtn, text, effectiveTimeout);
    }

    /**
     * Checks if the error message dialog is displayed.
     *
     * @return true if the error dialog is visible, false otherwise
     */
    @Step("Check if error dialog is displayed")
    public boolean isErrorDisplayed() {
        logger.info("Checking if error dialog is displayed");
        return isElementPresent(textError, 10);
    }

    /**
     * Checks if the "App has stopped" dialog is displayed.
     *
     * @return true if the dialog is visible, false otherwise
     */
    @Step("Check if 'App has stopped' dialog is displayed")
    public boolean isAppStopDisplayed() {
        logger.info("Checking if 'App has stopped' dialog is displayed");
        return isElementPresent(appStop, 15);
    }

    /**
     * Clicks the OK button on the error dialog.
     */
    @Step("Click 'OK' button on error dialog")
    public void clickBtnErrorOk() {
        logger.info("Clicking OK button on error dialog");
        click(btnErrorOk);
    }
}