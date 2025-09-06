package listeners;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import base.BaseTest;
import utils.LogHelper;
import utils.ScreenshotUtil;

public class TestListener implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		LogHelper.info("Listener: Test Started - "+ result.getName());

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		LogHelper.info("Listener: Test Passed - "+result.getName());

	}

	@Override
	public void onTestFailure(ITestResult result) {
		//here we have LogHelper.error not LogHelper.info
		LogHelper.error("Listener: Test Failed - " + result.getName(), result.getThrowable());
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+ScreenshotUtil.screenshotForAllure(result.getName(), BaseTest.getDriver()));
		//attachArtifactsIfAvailable(result);

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		LogHelper.warn("Listener: Test Skipped - "+result.getName());
		attachArtifactsIfAvailable(result);

	}



	@Override
	public void onStart(ITestContext context) {
		LogHelper.info("Listener: Test Context Started - "+context.getName());

	}

	@Override
	public void onFinish(ITestContext context) {
		LogHelper.info("Listener: Test Context Finished - "+context.getName());

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}


	private void attachArtifactsIfAvailable(ITestResult result) {
		WebDriver driver=BaseTest.getDriver();
		if (driver != null) {
			// Screenshot
			System.out.println("###################################################################################################################################Capturing Allure screenshot for test: " + result.getName() + " | Driver: " + driver);
			ScreenshotUtil.screenshotForAllure(result.getName(), driver);
		} else {
			LogHelper.warn("No WebDriver instance available for screenshot in test: " + result.getName());

		}

	}


}




/*
 * Where is captureScreenshotForAllure called in real projects?
 * 
 * In most enterprise frameworks:
 * 
 * ✅ It’s called in the Listener (TestNG ITestListener)
 * 
 * ❌ It’s usually not called inside LogHelper
 * 
 * 🔹 Why Listener, not LogHelper?
 * 
 * Single responsibility
 * 
 * LogHelper: handles logging (Log4j, Extent, Reporter).
 * 
 * Listener: handles test lifecycle events (start, success, fail, skip).
 * 
 * If you put Allure screenshot inside LogHelper, you’re mixing “logging” with
 * “reporting/attachment responsibilities”.
 * 
 * Cleaner control
 * 
 * Listener automatically knows when a test failed (onTestFailure).
 * 
 * No need to rely on whether LogHelper.error() was called or not.
 * 
 * Even if a failure happens silently (like an Assert.fail()), onTestFailure
 * will still trigger → screenshot guaranteed.
 * 
 * Avoid duplication
 * 
 * If LogHelper calls both Extent screenshot and Allure screenshot, you might
 * end up with two attachments per failure.
 * 
 * By keeping Allure inside the listener, you ensure one clean screenshot per
 * failure.
 */
