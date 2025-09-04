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
	
	
	        //function returning webelement after finding it by css selector
	        public static WebElement findElementUsingCssSelector(WebDriver driver, By cssSelector) {
	        	WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(30));
	        	WebElement element=wait.until(ExpectedConditions.visibilityOfElementLocated(cssSelector));
	        	return element;
	        }
	
	        //function returning webelement after finding it by xpath
			public static WebElement findElementUsingXpath(WebDriver driver,String xpath) {
				WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(30));
				WebElement element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
				return element;
			}	


			//function returning webelement after finding it by id
			public static WebElement findElementUsingId(WebDriver driver,By val) {
				WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(30));
				WebElement element=wait.until(ExpectedConditions.visibilityOfElementLocated(val));
				return element;
			}	

			//function to wait till the page loads completely
			public static void waitForPageLoad(WebDriver driver) {
				WebDriverWait w=new WebDriverWait(driver,Duration.ofSeconds(30));
				w.until((ExpectedCondition<Boolean>) wd -> {
					JavascriptExecutor js1= (JavascriptExecutor) wd;
					String readyState=(String) js1.executeScript("return document.readyState");
					return readyState.equals("complete");
				});

			}


			//function to switch driver to the latest opened browser window tab
			public static void switchDriverToLatestBrowserWindow(WebDriver driver) {
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
			public static ChromeOptions incogintioMode(ChromeOptions options) {
				options.addArguments("--incognito");
				return options;
			}

			//function to delete all cookies
			public static WebDriver deleteAllTheCookies(WebDriver driver) {
				driver.manage().deleteAllCookies();
				return driver;		
			}


			//function to open url in maximized window
			public static void openURLinMaximizedWindow(WebDriver driver,String URL) {
				driver.get(URL);
				driver.manage().window().maximize();		
			}


			//function to scroll into view to product,click the product image and wait till new page loads
			public static void clickProduct(WebDriver driver,String productXpath) throws InterruptedException {
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
					waitForPageLoad(driver);
				}}


			
				

			}


