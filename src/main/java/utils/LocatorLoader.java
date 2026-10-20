package utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class LocatorLoader {
    private static Properties props = new Properties();

    static {
        try (InputStream fis = LocatorLoader.class.getClassLoader().getResourceAsStream("locators.properties")) {
            if (fis != null) {
                props.load(fis);
            } else {
                // Fallback for standalone absolute execution paths
                try (FileInputStream fileStream = new FileInputStream("src/main/resources/locators.properties")) {
                    props.load(fileStream);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to safely parse execution properties storage repository file structures.", e);
        }
    }

    public static String getValue(String targetKey) {
        String fetched = props.getProperty(targetKey);
        if (fetched == null) {
            throw new IllegalArgumentException("PROPERTY ERROR: Request key parameter mapping '" + targetKey + "' was not found inside locators.properties storage config map.");
        }
        return fetched.trim();
    }
}