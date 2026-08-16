package mobile.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base class for all mobile screens.
 * Provides common initialization and utility methods for element interactions.
 */
public abstract class BaseScreen {

    protected AppiumDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BaseScreen.class);

    public BaseScreen(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(20)), this);
        logger.info("Initialized {} with Appium driver", this.getClass().getSimpleName());
    }

    public boolean isTextInElementPresent(WebElement element, String text, int time) {
        logger.debug("Waiting for text '{}' to appear in element", text);
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(time))
                    .until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (TimeoutException e) {
            logger.warn("Text '{}' was not present in element within {} seconds", text, time);
            return false;
        }
    }

    public boolean isElementPresent(WebElement element, int time) {
        logger.debug("Waiting for element visibility within {} seconds", time);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(time))
                    .until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            logger.warn("Element was not visible within {} seconds", time);
            return false;
        }
    }

    public void click(WebElement element) {
        logger.debug("Waiting for element to be clickable before clicking: {}", element);
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * Types text into the specified input element.
     * Uses sendKeys(text + "\n") to ensure keyboard activation and stable input on Android.
     */
    public void type(WebElement element, String text) {
        if (text != null) {
            logger.debug("Typing text '{}' into element: {}", text, element);

            click(element);
            pause(300);

            try {
                element.sendKeys(text);
                logger.debug("Entered text using sendKeys(): {}", text);
            } catch (Exception e) {
                logger.error("Failed to enter text '{}': {}", text, e.getMessage());
            }
        }
    }

    public void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Pause interrupted: {}", e.getMessage());
        }
    }
}
