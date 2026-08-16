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
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Base class for all mobile tests.
 * Initializes Appium driver and screen objects.
 */
public class TestBase {

    protected static final Logger logger = LoggerFactory.getLogger(TestBase.class);
    protected static final String ADB = System.getProperty("adb.path", "adb");

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected LoginRegistrationScreen loginRegistrationScreen;
    protected ContactListScreen contactListScreen;

    @BeforeMethod
    public void setup() {
        ensureEmulatorReadyOrSkip("emulator-5554", 60);
        logger.info("Initializing Appium driver...");

        // Default config file for local and CI runs
        String configFile = System.getProperty("configFile", "pixel.properties");

        try {
            driver = AppiumConfig.createAppiumDriver(configFile);
        } catch (Exception e) {
            logger.error("Failed to initialize Appium driver: {}", e.getMessage());
            throw e;
        }

        ensureDriverInitializedOrSkip();

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

    // --------------------
    // ADB utility methods
    // --------------------

    protected void ensureDriverInitializedOrSkip() {
        if (driver == null) {
            logger.error("Appium driver is null — skipping test to avoid NPE. Ensure emulator and Appium are ready.");
            throw new SkipException("Appium driver not initialized");
        }
    }

    protected String runAdb(String... args) {
        List<String> cmd = new ArrayList<>();
        cmd.add(ADB);
        cmd.addAll(Arrays.asList(args));
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        try {
            Process p = pb.start();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (InputStream is = p.getInputStream()) {
                byte[] buf = new byte[8192];
                int r;
                while ((r = is.read(buf)) != -1) {
                    baos.write(buf, 0, r);
                }
            }
            p.waitFor(30, TimeUnit.SECONDS);
            return baos.toString(StandardCharsets.UTF_8).trim();
        } catch (IOException | InterruptedException e) {
            logger.warn("ADB command failed: {} ; exception: {}", String.join(" ", cmd), e.toString());
            return "";
        }
    }

    protected boolean waitForEmulatorBoot(String emulatorSerial, int timeoutSec) {
        logger.info("Waiting for emulator {} to become available (timeout {}s)", emulatorSerial, timeoutSec);
        int waited = 0;
        while (waited < timeoutSec) {
            String devices = runAdb("devices");
            if (devices.contains(emulatorSerial) && devices.matches("(?s).*" + emulatorSerial + "\\s+device.*")) {
                String boot = runAdb("-s", emulatorSerial, "shell", "getprop", "sys.boot_completed");
                if ("1".equals(boot.trim())) {
                    logger.info("Emulator {} boot completed", emulatorSerial);
                    return true;
                }
            }
            try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            waited += 5;
        }
        logger.error("Emulator {} did not boot within {} seconds", emulatorSerial, timeoutSec);
        return false;
    }

    protected void ensureEmulatorReadyOrSkip(String emulatorSerial, int timeoutSec) {
        boolean ready = waitForEmulatorBoot(emulatorSerial, timeoutSec);
        if (!ready) {
            logger.error("Emulator {} not ready — skipping test", emulatorSerial);
            throw new SkipException("Emulator not ready");
        }
    }
}