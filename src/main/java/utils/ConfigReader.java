package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    // Locates, opens, and loads the config.properties file from the resource classpath into memory
    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "config.properties was not found on the classpath. "
                        + "Ensure it is in the project's resources folder.");
            }

            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConfigReader() {
    }

    // Searches for the property key, ensures it is not blank, and returns the trimmed value string
    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing configuration property: " + key);
        }

        return value.trim();
    }
}
