package mobile.helpers;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

/**
 * Utility interface providing swipe actions for mobile automation.
 * Supports screen-level and element-level swipes in multiple directions using W3C Actions.
 */
public interface SwipeUtils {

    Logger logger = LoggerFactory.getLogger(SwipeUtils.class);

    /**
     * Performs a swipe gesture across the entire screen in the specified direction.
     *
     * @param driver    the Appium driver instance
     * @param direction the direction of the swipe (UP, DOWN, LEFT, RIGHT)
     */
    default void swipeScreen(AppiumDriver driver, Direction direction) {
        Dimension size = driver.manage().window().getSize();
        int startX, endX, startY, endY;
        int middleX = size.width / 2;
        int middleY = size.height / 2;

        switch (direction) {
            case RIGHT -> {
                startX = (int) (size.width * 0.2);
                endX = (int) (size.width * 0.8);
                startY = endY = middleY;
            }
            case LEFT -> {
                startX = (int) (size.width * 0.8);
                endX = (int) (size.width * 0.2);
                startY = endY = middleY;
            }
            case UP -> {
                startY = (int) (size.height * 0.9);
                endY = (int) (size.height * 0.1);
                startX = endX = middleX;
            }
            case DOWN -> {
                startY = (int) (size.height * 0.1);
                endY = (int) (size.height * 0.9);
                startX = endX = middleX;
            }
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        logger.info("Performing swipe {} across the screen", direction);
        performSwipe(driver, startX, startY, endX, endY);
    }

    /**
     * Performs a swipe gesture inside a specific element.
     *
     * @param driver    the Appium driver instance
     * @param element   the target element
     * @param direction the direction of the swipe (LEFT or RIGHT)
     */
    default void swipeInsideElement(AppiumDriver driver, WebElement element, Direction direction) {
        Rectangle rect = element.getRect();
        int startX, endX;
        int middleY = rect.y + rect.height / 2;

        switch (direction) {
            case RIGHT -> {
                startX = rect.x + (int) (rect.width * 0.2);
                endX = rect.x + (int) (rect.width * 0.8);
            }
            case LEFT -> {
                startX = rect.x + (int) (rect.width * 0.8);
                endX = rect.x + (int) (rect.width * 0.2);
            }
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        logger.info("Performing swipe {} inside element", direction);
        performSwipe(driver, startX, middleY, endX, middleY);
    }

    /**
     * Performs a short swipe gesture from right to center inside an element (used for update actions).
     */
    default void swipeInsideElementUpdate(AppiumDriver driver, WebElement element) {
        Rectangle rect = element.getRect();
        int startX = rect.x + (int) (rect.width * 0.9);
        int endX = rect.x + (int) (rect.width * 0.5);
        int middleY = rect.y + rect.height / 2;

        logger.info("Performing update swipe inside element");
        performSwipe(driver, startX, middleY, endX, middleY);
    }

    /**
     * Performs a full swipe gesture across an element from right to left (used for delete actions).
     */
    default void swipeInsideElementDelete(AppiumDriver driver, WebElement element) {
        Rectangle rect = element.getRect();
        int startX = rect.x + (int) (rect.width * 0.9);
        int endX = rect.x + (int) (rect.width * 0.1);
        int middleY = rect.y + rect.height / 2;

        logger.info("Performing delete swipe inside element");
        performSwipe(driver, startX, middleY, endX, middleY);
    }

    /**
     * Helper method to perform swipe gesture using W3C pointer input.
     */
    private void performSwipe(AppiumDriver driver, int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }
}