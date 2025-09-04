package utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;


import com.aventstack.extentreports.ExtentTest;

public class LogHelper {

	private static final Logger logger = LogManager.getLogger(LogHelper.class);
	//private static ExtentTest extentTest;---ExtentTest handling is NOT thread-safe That means:
	/*
	 * 
	 * 
	 * In parallel runs, every test thread will overwrite the same extentTest.
	 * 
	 * Logs from multiple tests will mix up into one report entry.
	 */
	
	
	private static ThreadLocal<ExtentTest> extentTest= new ThreadLocal<>();
	
	//Attach ExtentTest for current Thread
	public static void setExtentTest(ExtentTest test) {
		extentTest.set(test);
	}
	
	//Fetch ExtentTest for current Thread
	public static ExtentTest getExtentTest() {
		return extentTest.get();
	}
	
	
	

	//	private static WebDriver driver; 
	//
	//    // Attach WebDriver (from BaseTest)
	//    public static void setDriver(WebDriver driverInstance) {
	//        driver = driverInstance;
	//    }
	//In single-threaded tests (one browser at a time), this is fine.

	//In parallel tests (multiple browsers), the static driver in LogHelper will get overwritten → wrong screenshots, mixed logs.
	//
	//Example issue:
	//
	//Test1 sets Chrome → LogHelper.driver = Chrome
	//
	//Test2 sets Firefox → LogHelper.driver = Firefox
	//
	//Now Test1 logs an error → screenshot taken from Firefox instead of Chrome 😱	

	//ThreadLocal<T> is a Java class that lets you store data per thread.

	//Each thread (test case when running in parallel with TestNG) gets its own copy of the variable.
	//
	//So even if it’s declared static, each thread won’t share the same WebDriver instance. Instead, each gets its own isolated copy.

	//	Thread A → has its own ChromeDriver instance.
	//
	//	Thread B → has its own FirefoxDriver instance.
	//
	//	No conflicts.
	private static ThreadLocal<WebDriver>  driver=new ThreadLocal<>();


	//Attach webdriver for current thread
	public static void setDriver(WebDriver driverInstance) {
		driver.set(driverInstance);

	}

	//  here driverInstance is not the driver from BaseTest class
	//
	//	driver is a ThreadLocal<WebDriver>, not a regular WebDriver.
	//
	//	driverInstance is a WebDriver object created in your BaseTest class (new ChromeDriver() etc.).
	//
	//	driver.set(driverInstance) stores this WebDriver instance in the ThreadLocal storage for the current thread.
	//
	//  it is needed for Thread safety (parallel tests):





	private static WebDriver getDriver() {
		return driver.get();
	}


	//	driver → is a ThreadLocal<WebDriver> variable, meaning each test thread has its own isolated WebDriver instance.
	//
	//	driver.get() here → is a method from ThreadLocal, not Selenium’s driver.get("url").
	//
	//	👉 What it does:
	//	It fetches the WebDriver object that was stored for the current thread (using setDriver(driverInstance) earlier in your BaseTest).


	//--------------------------INFO--------------------------
	public static void info(String message) {
		logger.info(message);//logger is for log4j logs
		Reporter.log("INFO: "+message,true);//reporter is for TestNg logs and second argument if set true then prints the log in console
		if(getExtentTest()!=null) getExtentTest().info(message);//extentTest is for extent report
	}



	//--------------------------WARNING--------------------------
	public static void warn(String message) {
		logger.warn(message);
		Reporter.log("WARN: "+message,true);
		if(getExtentTest()!=null) getExtentTest().warning(message);
	}



	//--------------------------Error(with exception)--------------------------
	public static void error(String message,Throwable t) {
		logger.error(message,t);
		Reporter.log("Error: "+message,true);
		//If WebDriver exists, capture screenshot and return its path; otherwise, just return null.”
		String screenshotpath= (getDriver()!=null) ? ScreenshotUtil.captureScreenshot(getDriver(), "Error_"):null;
		if(getExtentTest()!=null) {
			if(screenshotpath!=null) {
				String base64= ScreenshotUtil.capturesScreenshotBase64(getDriver());
				getExtentTest().fail(message+" | Exception: "+t.getMessage()).addScreenCaptureFromBase64String(base64);
			}
			else {
				getExtentTest().fail(message+" | Exception: "+t.getMessage());
			}
		}

	}

	

	//--------------------------Error(without exception)--------------------------
	public static void error(String message) {
		logger.error(message);
		Reporter.log("Error: "+message,true);
		String screenshotpath= (getDriver()!=null) ? ScreenshotUtil.captureScreenshot(getDriver(), "Error_"):null;
		if(getExtentTest()!=null) {
			if(screenshotpath!=null) {
				String base64= ScreenshotUtil.capturesScreenshotBase64(getDriver());
				getExtentTest().fail(message).addScreenCaptureFromBase64String(base64);
			}
			else {
				getExtentTest().fail(message);
			}

		}

	}


	//--------------------------DEBUG--------------------------
	public static void debug(String message) {
		logger.debug(message);
		Reporter.log("DEBUG: "+message,true);
		if(getExtentTest()!=null) getExtentTest().info(message);
	}
	
	
	//Cleanup method for safety
	/*
	 * call LogHelper.clear(); in your @AfterMethod after quitting driver. This
	 * prevents any memory leaks if TestNG reuses threads.
	 */
	
	public static void clear() {
		driver.remove();
		extentTest.remove();
	}
}
