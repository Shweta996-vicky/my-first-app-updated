package utils;

import org.testng.ITestListener;
import org.testng.ITestResult;

import base.BaseTest;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        System.err.println("FAILED: " + testName);

        if (result.getThrowable() != null) {
            result.getThrowable().printStackTrace(System.err);
        }

        Object instance = result.getInstance();

        if (!(instance instanceof BaseTest)) {
            System.err.println(
                    "Screenshot skipped: test does not extend BaseTest.");
            return;
        }

        BaseTest test = (BaseTest) instance;

        try {
            String path = ScreenshotUtil.capture(
                    test.getDriver(), testName);

            if (path != null) {
                System.out.println("Failure screenshot: " + path);
            } else {
                System.err.println(
                        "Failure screenshot could not be captured.");
            }

        } catch (RuntimeException e) {
            System.err.println(
                    "Error while capturing failure screenshot: "
                            + e.getMessage());
        }
    }
}
