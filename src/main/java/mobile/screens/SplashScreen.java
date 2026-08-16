package mobile.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Represents the splash screen of the mobile application.
 * Provides a method to validate the displayed app version.
 */
public class SplashScreen extends BaseScreen {

    private static final Logger logger = LoggerFactory.getLogger(SplashScreen.class);

    public SplashScreen(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Dynamically validates that the specified version text is displayed on the splash screen.
     *
     * @param text expected version text (e.g. "1.0.0")
     * @param time timeout in seconds
     * @return true if the version text is present, false otherwise
     */
    public boolean validateVersionApp(String text, int time) {
        String versionToFind = text != null ? text.trim() : "";
        logger.info("Validating app version text: '{}'", versionToFind);

        // Ensure minimum timeout for slow CI environments
        int effectiveTimeout = Math.max(time, 15);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(effectiveTimeout));
            WebElement versionElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            AppiumBy.xpath("//*[contains(@text, '" + versionToFind + "')]")
                    )
            );
            return versionElement.isDisplayed();
        } catch (TimeoutException e) {
            logger.warn("Version text '{}' not found on splash screen within {} seconds", versionToFind, effectiveTimeout);
            return false;
        }
    }
}