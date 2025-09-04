package utils;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementsUtil {
	private WebDriver driver;
	private WaitHelper waitHelper;
	
	public ElementsUtil(WebDriver driver) {
		this.driver=driver;
		this.waitHelper=new WaitHelper(driver);
	}


	public WebElement findElement(By locator) {
		return waitHelper.waitForVisibility(locator);
	}



	//function to switch driver to the latest opened browser window tab
	public void switchDriverToLatestBrowserWindow() {
		ArrayList<String> tabs= new ArrayList<>(driver.getWindowHandles());//to get all the window handles
		driver.switchTo().window(tabs.get(tabs.size()-1));		
	}



	//function to alter ChromeOptions object to block notifications from Myntra Application when shirt image was clicked and I needed to select shirt size
	public static ChromeOptions suppressWebApplicationNotification(ChromeOptions options) {
		Map<String,Object> prefs=new HashMap<>();
		prefs.put("profile.default_content_setting_values.notifications", 2);
		options.setExperimentalOption("prefs", prefs);
		return options;
	}

	//function to alter ChromeOptions object to open url in incognito mode
	public static ChromeOptions incogintoMode(ChromeOptions options) {
		options.addArguments("--incognito");
		return options;
	}

	//function to delete all cookies
	public WebDriver deleteAllTheCookies() {
		driver.manage().deleteAllCookies();
		return driver;		
	}


	//function to open url in maximized window
	public void openURLinMaximizedWindow(String URL) {
		driver.get(URL);
		driver.manage().window().maximize();		
	}


	//function to scroll into view to product,click the product image and wait till new page loads
	public void clickProduct(String productXpath) throws InterruptedException {
		JavascriptExecutor js=(JavascriptExecutor) driver;
		boolean found=false;
		for(int i=0;i<10;i++) {
			try {			
				WebElement element = driver.findElement(By.xpath(productXpath));				
				js.executeScript("arguments[0].scrollIntoView(true)", element);				
				js.executeScript("arguments[0].click()",element);
				found=true;
				break;

			} catch (NoSuchElementException ex) {
				System.out.println("Product not found in current view, scrolling...");
				js.executeScript("window.scrollBy(0,1000);");
				Thread.sleep(3000);
			}
		}
		if(!found) {
			System.out.println("Product not found after scrolling!");
		}
		else {
			new WaitHelper(driver).waitForPageLoad();
		}}





}




/*
 * Version 1 (instance-level) ---used in real time as:-
 * No need to pass driver repeatedly in every method call.
 * Only one WaitHelper instance per page object or per ElementsUtil.
 * Slightly better memory efficiency than creating a new WaitHelper every call.
 * 
 * public class ElementsUtil {
 * 
 * private WebDriver driver; 
 * private WaitHelper waitHelper;
 * 
 * public ElementsUtil(WebDriver driver) { 
 * this.driver = driver; 
 * this.waitHelper= new WaitHelper(driver); 
 * }
 * 
 * 
 * public WebElement findElement(By locator, int timeout) { 
 * return waitHelper.waitForVisibility(locator, timeout); 
 * } 
 * 
 * }
 * 
 * usage in page object 
 * ElementsUtil elements = new ElementsUtil(driver);
 * WebElement username = elements.findElement(By.id("username"), 10);
 * 
 * 
 * Version 2 (driver-param each call)
 * 
 * public class ElementsUtil {
 * 
 * public WebElement findElement(WebDriver driver, By locator, int timeout) {
 * return new WaitHelper(driver).waitForVisibility(locator, timeout); 
 * } 
 * 
 * }
 * 
 * usage in page object 
 * WebElement username = elementsUtil.findElement(driver,By.id("username"), 10);
 * 
 * 
 * 
 * 
 * 
 */