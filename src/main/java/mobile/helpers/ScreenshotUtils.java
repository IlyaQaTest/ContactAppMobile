package mobile.helpers;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for taking screenshots across the project.
 */
public class ScreenshotUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);

    // Take screenshot and save it to the 'screenshots' directory
    public static void takeScreenshot(AppiumDriver driver, String name) {
        logger.info("Capturing screenshot: {}", name);
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("screenshots/" + name + ".png"));
            logger.info("Screenshot saved successfully: screenshots/{}.png", name);
        } catch (IOException e) {
            logger.error("Failed to save screenshot '{}': {}", name, e.getMessage());
        }
    }
}