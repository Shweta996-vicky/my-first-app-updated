package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class LocatorLoader {

    private static final Properties PROPERTIES = new Properties();

    // Attemps to load locators.properties from the resource classpath, falling back to a direct filesystem check if missing
    static {
        try (InputStream resource = LocatorLoader.class
                .getClassLoader()
                .getResourceAsStream("locators.properties")) {

            if (resource != null) {
                PROPERTIES.load(resource);
            } else {
                try (InputStream file = new FileInputStream("locators.properties")) {
                    PROPERTIES.load(file);
                }
            }

        } catch (IOException e) {
            throw new ExceptionInInitializerError(
                    new IllegalStateException(
                            "Failed to load locators.properties. Check the file location and format.",
                            e));
        }
    }

    private LocatorLoader() {
    }

    // Searches for the locator property key, ensures it is not blank, and returns the trimmed locator string
    public static String getValue(String key) {
        String value = PROPERTIES.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing locator property: " + key);
        }

        return value.trim();
    }
}
