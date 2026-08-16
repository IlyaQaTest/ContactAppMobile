package mobile.config;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

import static mobile.config.PropertiesReader.getProperty;


/**
 * Configures and initializes the Appium driver using parameters from properties
 * or system environment overrides for CI/CD compatibility.
 */
public class AppiumConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppiumConfig.class);

    /**
     * Creates and returns an AndroidDriver instance.
     * Allows system properties to override configuration file values for GitHub Actions.
     *
     * @param fileName the name of the properties file located in resources/properties/
     * @return an initialized AndroidDriver instance
     */
    public static AndroidDriver createAppiumDriver(String fileName) {

        // Normalize Appium URL
        String appiumUrl = getValueOptional(fileName, "appiumUrl");
        if (appiumUrl == null || appiumUrl.isEmpty()) {
            appiumUrl = System.getProperty("appiumUrl", "http://127.0.0.1:4723/");
        }

        if (appiumUrl.endsWith("/wd/hub") || appiumUrl.endsWith("/wd/hub/")) {
            appiumUrl = appiumUrl.replaceAll("/wd/hub/?$", "");
        }
        if (!appiumUrl.endsWith("/")) {
            appiumUrl = appiumUrl + "/";
        }

        logger.info("Connecting to Appium server at: {}", appiumUrl);

        // Core capabilities
        String platformName = System.getProperty("platformName", getValueOptional(fileName, "os"));
        if (platformName == null || platformName.isEmpty()) {
            platformName = "Android";
        }

        String automationName = System.getProperty("automationName", getValueOptional(fileName, "automationName"));
        if (automationName == null || automationName.isEmpty()) {
            automationName = "UiAutomator2";
        }

        String deviceName = System.getProperty("deviceName", getValueOptional(fileName, "deviceName"));
        if (deviceName == null || deviceName.isEmpty()) {
            deviceName = "emulator-5554";
        }

        // Correct package & activity for Contact App
        String appPackage = System.getProperty("appPackage", "com.sheygam.contactapp");
        String appActivity = System.getProperty("appActivity", "com.sheygam.contactapp.SplashActivity");

        // APK path (CI or local)
        String appPath = System.getProperty("appPath", getValueOptional(fileName, "appPath"));

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(platformName)
                .setAutomationName(automationName)
                .setDeviceName(deviceName)
                .setAppPackage(appPackage)
                .setAppActivity(appActivity)
                .setAppWaitPackage(appPackage)
                .setAppWaitActivity(appActivity)
                .setNewCommandTimeout(Duration.ofSeconds(300))
                .setAndroidInstallTimeout(Duration.ofSeconds(180))
                .setAdbExecTimeout(Duration.ofSeconds(120))
                .setAppWaitDuration(Duration.ofSeconds(45))
                .setUiautomator2ServerInstallTimeout(Duration.ofSeconds(90))
                .setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(90))
                .setAutoGrantPermissions(true)
                .setDisableWindowAnimation(true)
                .setNoReset(false);

        // Resolve APK path
        if (appPath != null && !appPath.isEmpty()) {
            File apkFile = new File(appPath);
            String absoluteAppPath = apkFile.isAbsolute()
                    ? apkFile.getAbsolutePath()
                    : Paths.get(appPath).toAbsolutePath().toString();

            logger.info("Setting APK path: {}", absoluteAppPath);
            options.setApp(absoluteAppPath);
        } else {
            logger.warn("No appPath specified. Expecting appPackage to be already installed on the device.");
        }

        try {
            return new AndroidDriver(new URL(appiumUrl), options);
        } catch (MalformedURLException e) {
            logger.error("Invalid Appium URL: {}", appiumUrl, e);
            throw new RuntimeException("Bad Appium URL: " + appiumUrl, e);
        }
    }

    /**
     * Checks System property first, then falls back to properties file.
     */
    private static String getValue(String fileName, String key) {
        String sysValue = System.getProperty(key);
        if (sysValue != null && !sysValue.isEmpty()) {
            return sysValue;
        }
        String fileValue = getProperty(fileName, key);
        if (fileValue == null) {
            throw new RuntimeException("Missing required property: " + key + " in " + fileName);
        }
        return fileValue;
    }

    /**
     * Optional property retriever without throwing exception.
     */
    private static String getValueOptional(String fileName, String key) {
        String sysValue = System.getProperty(key);
        if (sysValue != null && !sysValue.isEmpty()) {
            return sysValue;
        }
        return getProperty(fileName, key);
    }
}