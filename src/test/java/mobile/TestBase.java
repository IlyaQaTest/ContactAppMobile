package mobile;


import io.appium.java_client.android.AndroidDriver;
import mobile.config.AppiumConfig;
import mobile.screens.ContactListScreen;
import mobile.screens.LoginRegistrationScreen;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Base class for all mobile tests.
 * Initializes Appium driver and screen objects.
 */
public class TestBase {

    protected static final Logger logger = LoggerFactory.getLogger(TestBase.class);

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected LoginRegistrationScreen loginRegistrationScreen;
    protected ContactListScreen contactListScreen;

    @BeforeMethod
    public void setup() {
        logger.info("Initializing Appium driver...");

        // Default config file for local and CI runs
        String configFile = System.getProperty("configFile", "pixel.properties");

        try {
            driver = AppiumConfig.createAppiumDriver(configFile);
        } catch (Exception e) {
            logger.error("Failed to initialize Appium driver: {}", e.getMessage());
            throw e;
        }

        // Increased timeout for slow CI emulator
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Initialize screen objects
        loginRegistrationScreen = new LoginRegistrationScreen(driver);
        contactListScreen = new ContactListScreen(driver);

        logger.info("Appium driver initialized successfully.");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing Appium session...");
            try {
                driver.quit();
            } catch (Exception e) {
                logger.warn("Error during driver.quit(): {}", e.getMessage());
            } finally {
                driver = null;
            }
        }
    }

    /**
     * Waits for an alert, returns its text, and closes it.
     */
    public String getAlertTextAndClose() {
        logger.debug("Waiting for alert to appear...");
        Alert alert = new WebDriverWait(driver, Duration.ofSeconds(25))
                .until(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        alert.accept();
        logger.info("Alert closed. Text: {}", text);
        return text;
    }

    /**
     * Checks if a Toast message containing the given text is present.
     */
    public boolean isToastPresent(String toastText, int timeout) {
        try {
            logger.info("Waiting for Toast message containing: '{}'", toastText);
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            WebElement toastElement = customWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@text,'" + toastText + "')]")
            ));
            return toastElement != null;
        } catch (Exception e) {
            logger.warn("Toast message '{}' not found within {} seconds", toastText, timeout);
            return false;
        }
    }
}