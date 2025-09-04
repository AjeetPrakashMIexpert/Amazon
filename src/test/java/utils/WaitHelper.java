package utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitHelper {
	
	private WebDriver driver;
	private int defaultTimeout=Integer.parseInt(ConfigReader.get("default.timeout"));
	private int pageLoadTimeout=Integer.parseInt(ConfigReader.get("page.load.timeout"));
	
	public WaitHelper(WebDriver driver) {
		this.driver=driver;
	}
	
	public WebElement waitForVisibility(By locator) {
		return new WebDriverWait(driver,Duration.ofSeconds(defaultTimeout))
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public WebElement waitForClickability(By locator) {
		return new WebDriverWait(driver,Duration.ofSeconds(defaultTimeout))
                .until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	public void waitForPageLoad() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(pageLoadTimeout));
		LogHelper.info("Waiting for the page to Load...");
		wait.until((ExpectedCondition<Boolean>) wd -> {
			JavascriptExecutor js= (JavascriptExecutor) wd;
			String readyState=(String) js.executeScript("return document.readyState");
			return readyState.equals("complete");
	});
	}

}




















/*
 * Option 1: Instance method public WebElement waitForVisibility(By locator, int
 * timeout) { return new WebDriverWait(driver, Duration.ofSeconds(timeout))
 * .until(ExpectedConditions.visibilityOfElementLocated(locator)); }
 * 
 * 
 * ✅ Uses the non-static driver field inside the class.
 * 
 * Requires you to create an object (constructor injection).
 * 
 * Example usage:
 * 
 * WaitHelper waitHelper = new WaitHelper(driver); WebElement username =
 * waitHelper.waitForVisibility(By.id("username"), 10);
 * 
 * 
 * Pros:
 * 
 * Cleaner syntax when working with Page Objects (no need to pass driver
 * everywhere).
 * 
 * Supports parallel execution easily (each object carries its own driver).
 * 
 * Cons:
 * 
 * You need to create an object, even if you just want a one-time wait.
 * 
 * Option 2: Static utility method public static WebElement
 * waitForVisibility(WebDriver driver, By locator, int timeout) { return new
 * WebDriverWait(driver, Duration.ofSeconds(timeout))
 * .until(ExpectedConditions.visibilityOfElementLocated(locator)); }
 * 
 * 
 * ✅ Static → belongs to the class, not the object.
 * 
 * You must pass driver explicitly every time.
 * 
 * Example usage:
 * 
 * WebElement username = WaitHelper.waitForVisibility(driver, By.id("username"),
 * 10);
 * 
 * 
 * Pros:
 * 
 * No need to instantiate WaitHelper.
 * 
 * Good for small, stateless utility classes (ElementsUtil, WaitUtils).
 * 
 * Cons:
 * 
 * Every call requires passing driver.
 * 
 * If overused, can lead to “utility sprawl” and reduce readability in Page
 * Object Model.
 * 
 * 🔑 How it’s done in real enterprise frameworks
 * 
 * For Page Object classes & helpers → use Option 1 (non-static, constructor
 * injection).
 * 
 * Cleaner, more OOP, easier to maintain, safe for parallel runs.
 * 
 * For utility-only classes (like DateTimeUtil, FileUtil, ElementsUtil) → use
 * Option 2 (static methods).
 * 
 * No state, no objects needed, just pure helpers.
 * 
 * ✅ Rule of Thumb:
 * 
 * If the method needs to work with an instance variable (like driver), keep it
 * non-static.
 * 
 * If the method is stateless (works only with what’s passed as parameters),
 * make it static.
 */



/*
 * Real-world guideline
 * 
 * Static methods → For general helpers/utils (date formatting, screenshot,
 * string utils, ChromeOptions tweaks).
 * 
 * Non-static methods (with constructor injection) → For driver-dependent
 * actions (finding elements, waiting, interacting). ex- public static void
 * openURLinMaximizedWindow(WebDriver driver,String URL) { driver.get(URL);
 * driver.manage().window().maximize(); }
 * 
 * if you write static you will have to keep passing WebDiver driver so in real
 * time static method is not used if driver is needed but if method is stateless
 * then static method is used ex- public static ChromeOptions
 * incogintoMode(ChromeOptions options) { options.addArguments("--incognito");
 * return options; }
 */