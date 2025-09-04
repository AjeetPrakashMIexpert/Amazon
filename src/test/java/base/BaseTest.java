package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.ElementsUtil;
import utils.EmailUtil;
import utils.ExtentManager;
import utils.LogHelper;
import utils.ScreenshotUtil;
import java.lang.reflect.Method;
import java.time.Duration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


@Listeners(listeners.TestListener.class)
public class BaseTest {

	//protected WebDriver driver;
	// ThreadLocal driver for parallel safety
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
	protected ChromeOptions options;
	protected static ExtentReports extent;
	protected ExtentTest test;
	
	// Utility to get driver from ThreadLocal
    public static WebDriver getDriver() {
        return tlDriver.get();
    }

	//Page Objects
	protected LoginPage loginPage;
	protected HomePage homePage;


	//Example test data
	//protected Object classTestData;



	//-------------------------------------------------------SUITE LEVEL--------------------------------------------------------
	@BeforeSuite(alwaysRun=true)
	public void beforeSuite() {
		extent= ExtentManager.createInstance("extent-report");
		LogHelper.info("BeforeSuite: Extent Report initialized");

		// Suite-level DB connection
		//DatabaseManager.initConnection();
		//LogHelper.info("BeforeSuite: Database connection initialized");
	}

	@AfterSuite(alwaysRun=true)
	public void afterSuite() {
		if(extent!=null) {
			extent.flush();
			LogHelper.info("AfterSuite: Extent Report flushed");
		}
		//mail.to field in config file has multiple emails;so splitting that string on the basis of comma and 
		//storing in an array of string
		String[] recipients = ConfigReader.get("mail.to").split(",");
		EmailUtil.emailExtentReport(
				recipients, 
				ConfigReader.get("mail.subject"), 
				ConfigReader.get("mail.body"),
				ExtentManager.getExtentReportAbsPath()
				);
		
		
		
		
		/*
		 * DatabaseManager.closeConnection();
		 * LogHelper.info("AfterSuite: Database connection closed");
		 */

	}
	


	//-------------------------------------------------------TEST LEVEL--------------------------------------------------------

	@BeforeTest(alwaysRun=true)
	public void beforeTest() {
		//System.getProperty("env") looks for a JVM property.
		//System.getenv("env") looks for an OS environment variable.

		/*
		 * >>>Typical Selenium Maven Project Structure Amazon/ ├── src/ │ ├──
		 * main/java/... (framework code) │ ├── test/java/... (test classes) ├── pom.xml
		 * 
		 * 
		 * >>>POM.xml is in C:\Users\noble\eclipse-workspace\Amazon so in cmd first do
		 * cd C:\Users\noble\eclipse-workspace\Amazon
		 * 
		 * >>>then mvn test -Denv=qa
		 * 
		 * >>>How Maven runs tests mvn test -Denv=qa
		 * 
		 * mvn test → compiles and runs all TestNG/JUnit tests. -Denv=qa → passes a JVM
		 * system property, available in your BaseTest
		 * 
		 * >>>mvn test -Denv=prod Your framework automatically loads prod config.
		 */
		
		/*
		 * mvn test -Denv=qa when we do it from cmd it picks qa.properties file but
		 * after this run when i run from eclipse it again falls back to
		 * config.properties why so? why env of the JVM is not permanently set to qa?
		 * 
		 * Eclipse does not know about the -Denv=qa you passed earlier in CMD.
		 * 
		 * Eclipse launches a different JVM instance (its own runner).
		 * 
		 * Since you didn’t tell Eclipse about -Denv=qa, it uses the fallback:
		 */
		
		
		String env= System.getProperty("env","config");
		ConfigReader.loadConfig(env);
		System.setProperty("baseUrl", ConfigReader.get("baseUrl"));
		System.setProperty("browser", ConfigReader.get("browser"));

		LogHelper.info("BeforeTest: Environment set to: "+env);
		LogHelper.info("BeforeTest: Browser set to: "+System.getProperty("browser"));
		LogHelper.info("BeforeTest: Base URL set to: "+System.getProperty("baseUrl"));

		// Example: prepare test-level temp data
		//TestDataLoader.prepareTempData();		
	}

	@AfterTest(alwaysRun=true)
	public void afterTest() {
		LogHelper.info("AfterTest: Cleaning up test-level resources");
		// Example: cleanup test-level temp data
		//TestDataLoader.clearTempData();

	}


	//-------------------------------------------------------CLASS LEVEL--------------------------------------------------------

	@BeforeClass(alwaysRun=true)
	public void beforeClass() {
		LogHelper.info("BeforeClass: Initializing page objects for class: "+this.getClass().getSimpleName());

		
		// Load class-specific test data
		//classTestData = TestDataLoader.load(this.getClass().getSimpleName() + "Data.json");
	}

	@AfterClass(alwaysRun=true)
	public void afterClass() {
		LogHelper.info("AfterClass: Cleaning up class resources for: "+this.getClass().getSimpleName());
		
	}


	//-------------------------------------------------------METHOD LEVEL--------------------------------------------------------
	@BeforeMethod(alwaysRun=true)
	public void setUp(Method method) {
		LogHelper.info("BeforeMethod: Starting WebDriver for test method: "+method.getName());

		test=extent.createTest(this.getClass().getSimpleName() + " :: " + method.getName());
		LogHelper.setExtentTest(test);

		//WebDriver setup
		
		
		String browser= System.getProperty("browser","chrome");
		
		WebDriver driver;
		switch (browser.toLowerCase()) {

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver=new FirefoxDriver();
			break;

		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;

		default:
			ChromeOptions options=new ChromeOptions();
			options=ElementsUtil.suppressWebApplicationNotification(options);
			options=ElementsUtil.incogintioMode(options);
			WebDriverManager.chromedriver().setup();		
			driver=new ChromeDriver(options);
			ElementsUtil.deleteAllTheCookies(driver);
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(ConfigReader.get("implicitWait"))));
		driver.manage().window().maximize();
		
		// Store in ThreadLocal
        tlDriver.set(driver);
		
		
		LogHelper.setDriver(getDriver());
		
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
		
		driver.get(System.getProperty("baseUrl","https://www.google.com"));

	}
	
	
	@AfterMethod(alwaysRun=true)
	public void tearDown(ITestResult result) {
		//public static void tearDown(ITestResult result) {WRONG !!!--- as protected WebDriver driver is an instance variable (non-static).
		//but public static void tearDown(ITestResult result) is a static method, which means it cannot directly access instance variables 
		//like driver
		//result.getName() returns the name of the currently executed test method
		LogHelper.info("AfterMethod: Cleaning up after test method - "+result.getName());
		
		
		loginPage=null;
		homePage=null;
		
		try {
			if(getDriver()!=null) {
				getDriver().quit();
				LogHelper.info("AfterMethod: WebDriver quit successfully");
			}
			
		} catch (Exception e) {
			LogHelper.warn("AfterMethod: WebDriver already closed");
		}
		
		// Clean up ThreadLocals
	    tlDriver.remove();     // clears BaseTest ThreadLocal<WebDriver>
	    LogHelper.clear();     // clears LogHelper’s ThreadLocals
	}
	
	
	//-------------------------------------------------------GROUP LEVEL--------------------------------------------------------
	@BeforeGroups("login")
	public void beforeLoginGroup() {
		LogHelper.info("BeforeGroups: Setting up users for Login tests");
				
		//Example: Insert Test users into Database
		//DatabaseManager.insertTestUser("testuser1","password1");
		//DatabaseManager.insertTestUser("testuser2","password2");		
	}
	
	public void afterLoginGroup() {
		  LogHelper.info("AfterGroups: Cleaning up users for Login tests");

	        // Remove test users after the group tests
	        //DatabaseManager.deleteTestUser("testuser1");
	        //DatabaseManager.deleteTestUser("testuser2");
	}
}





