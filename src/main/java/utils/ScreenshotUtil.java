package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public final class ScreenshotUtil {

    private static final String SCREENSHOT_DIRECTORY = "screenshots";

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {
    }

    // Creates the directory structure, captures the browser screen file layout, and saves it using a sanitized timestamp filename
    public static String capture(WebDriver driver, String testName) {
        if (driver == null) {
            System.err.println("Screenshot capture skipped: WebDriver is null.");
            return null;
        }

        if (!(driver instanceof TakesScreenshot)) {
            System.err.println("Screenshot capture skipped: WebDriver does not support screenshots.");
            return null;
        }

        try {
            Path directory = Paths.get(SCREENSHOT_DIRECTORY);
            Files.createDirectories(directory);

            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String safeTestName = sanitizeFileName(testName);
            Path destination = directory.resolve(safeTestName + "_" + timestamp + ".png");

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            String savedPath = destination.toAbsolutePath().toString();
            System.out.println("Screenshot saved: " + savedPath);
            return savedPath;

        } catch (IOException | WebDriverException e) {
            System.err.println("Screenshot capture failed: " + e.getMessage());
            return null;
        }
    }

    // Strips out all illegal filename characters from the provided test name string and replaces them with underscores
    private static String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "unnamed_test";
        }

        return fileName.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
