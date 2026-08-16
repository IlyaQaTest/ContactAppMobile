package mobile.helpers;

import io.qameta.allure.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Captures full desktop screenshot and attaches it to Allure report.
 */
public class CrashHandlerPrtSC {
    private static final Logger logger = LoggerFactory.getLogger(CrashHandlerPrtSC.class);

    public static void captureDesktopScreenshot(String screenshotName) {
        logger.error("App crash detected! Capturing full desktop screenshot...");
        try {
            Thread.sleep(1500); // a short pause to allow the crash window to appear

            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenCapture = robot.createScreenCapture(screenRect);

            File destFile = new File("screenshots/" + screenshotName + "_desktop.png");
            ImageIO.write(screenCapture, "png", destFile);
            logger.info("Desktop screenshot saved: {}", destFile.getAbsolutePath());

            // 📎 attach to Allure
            attachScreenshot(screenCapture, screenshotName);
        } catch (Exception e) {
            logger.error("Failed to capture desktop screenshot: {}", e.getMessage());
        }
    }

    @Attachment(value = "Desktop Screenshot - {screenshotName}", type = "image/png")
    private static byte[] attachScreenshot(BufferedImage image, String screenshotName) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to Allure: {}", e.getMessage());
            return new byte[0];
        }
    }
}