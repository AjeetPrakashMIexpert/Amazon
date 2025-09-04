package pages;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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

	public void login(String email,String password) {
		openUrl();
		waitHelper.waitForPageLoad();
		clickSignInLink();
		waitHelper.waitForPageLoad();
		enterEmail(email);		
		clickContinue();
		waitHelper.waitForPageLoad();
		enterPassword(password);
		clickSignIn();
		waitHelper.waitForPageLoad();

	}
	
	public String incorrectPwd() {
		return elementsUtil.findElement(pwdErrMsgBox).getText();
	}

}
