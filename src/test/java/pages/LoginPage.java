package pages;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


import io.qameta.allure.Allure;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter.Mode;
import utils.ConfigReader;
import utils.CredentialsManager;
import utils.ElementsUtil;
import utils.WaitHelper;

public class LoginPage {

	private WebDriver driver;
	private ElementsUtil elementsUtil;
	private WaitHelper waitHelper;

	//Locators
	private By emailField=By.id("ap_email_login");
	private By signIn=By.cssSelector("#nav-link-accountList-nav-line-1");
	private By continueButton=By.id("continue");
	private By passwordField=By.id("ap_password");
	private By signinButton=By.id("signInSubmit");
	private By pwdErrMsgBox=By.id("auth-error-message-box");


	//Constructor --constructor does not have any return type not even void if it has any return type 
	//it will become method not constructor---also driver is non static to access it within constructor or any method all the methods
	//are kept non static thus static is not there unlike as we see in public static void main(Strings[] args){}
	public LoginPage(WebDriver driver) {
		this.driver=driver;
		this.elementsUtil=new ElementsUtil(driver);
		this.waitHelper=new WaitHelper(driver);
	}


	//Actions
	public void openUrl() {
		driver.get(System.getProperty("baseUrl","https://www.google.com"));
	}
	
	@Step("Enter email: {0}")
	public void enterEmail(String email) {
		elementsUtil.findElement(emailField).sendKeys(email);
	}
	
	public void clickSignInLink() {
		elementsUtil.findElement(signIn).click();
	}

	public void clickContinue() {
		elementsUtil.findElement(continueButton).click();		
	}

	public void enterPassword(String password) {
		elementsUtil.findElement(passwordField).sendKeys(password);	
	}

	public void clickSignIn() {
		elementsUtil.findElement(signinButton).click();
	}


	@Step("Login with masked credentials")
	//@Param(mode=Mode.HIDDEN) using this password won't be shown in allure report
	public void login(String email,@Param(mode=Mode.HIDDEN) String password) {
		Allure.step("Login with email:- "+email+" and password:- *******");
		openUrl();
		waitHelper.waitForPageLoad();
		clickSignInLink();

		enterEmail(email);		
		clickContinue();

		enterPassword(password);
		clickSignIn();
		waitHelper.waitForPageLoad();
//Use element-level waits (explicit waits) instead of full-page load waits in most cases.
	}
	
	public String incorrectPwd() {
		return elementsUtil.findElement(pwdErrMsgBox).getText();
	}

}






/*
 * What {0} Means in Allure @Step
 * 
 * It refers to the method parameter value that gets passed at runtime.
 * 
 * {0} = first parameter
 * 
 * {1} = second parameter
 * 
 * {2} = third parameter … and so on.
 * 
 * So when you write:
 * 
 * @Step("Enter email: {0}") public void enterEmail(String email) {
 * driver.findElement(By.id("ap_email")).sendKeys(email); }
 * 
 * 
 * And call it like this:
 * 
 * loginPage.enterEmail("test@gmail.com");
 * 
 * 
 * In the Allure report you’ll see:
 * 
 * Enter email: test@gmail.com
 */