package mobile.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for reading property files from the resources/properties directory.
 * Provides a simple method to retrieve configuration values by key.
 */
public class PropertiesReader {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesReader.class);

    /**
     * Reads a property value from the specified file.
     *
     * @param fileName the name of the properties file located in resources/properties/
     * @param key      the property key to retrieve
     * @return the property value, or null if not found
     */
    public static String getProperty(String fileName, String key) {
        Properties properties = new Properties();
        ClassLoader loader = PropertiesReader.class.getClassLoader();

        try (InputStream inputStream = loader.getResourceAsStream("properties/" + fileName)) {
            if (inputStream == null) {
                logger.error("Cannot find file in resources: properties/{}", fileName);
                return null;
            }
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            logger.error("Failed to load properties file: {}", fileName, e);
            throw new RuntimeException("Failed to load properties file: " + fileName, e);
        }
    }
}