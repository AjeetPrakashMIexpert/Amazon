package pages;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.CredentialsManager;
import utils.ElementsUtil;

public class LoginPage {

	private WebDriver driver;

	//Locators
	private By emailField=By.id("ap_email_login");
	private By signIn=By.cssSelector("nav-link-accountList-nav-line-1");
	private By continueButton=By.id("continue");
	private By passwordField=By.id("ap_password");
	private By signinButton=By.id("signInSubmit");
	private By pwdErrMsgBox=By.id("auth-error-message-box");


	//Constructor --constructor does not have any return type not even void if it has any return type 
	//it will become method not constructor---also driver is non static to access it within constructor or any method all the methods
	//are kept non static thus static is not there unlike as we see in public static void main(Strings[] args){}
	public LoginPage(WebDriver driver) {
		this.driver=driver;
	}


	//Actions
	public void enterEmail(String email) {
		ElementsUtil.findElementUsingId(driver,emailField).sendKeys(email);
	}
	
	public void clickSignInLink() {
		ElementsUtil.findElementUsingCssSelector(driver, signIn).click();
	}

	public void clickContinue() {
		ElementsUtil.findElementUsingId(driver,continueButton).click();		
	}

	public void enterPassword(String password) {
		ElementsUtil.findElementUsingId(driver,passwordField).sendKeys(password);	
	}

	public void clickSignIn() {
		ElementsUtil.findElementUsingId(driver,signinButton).click();
	}

	public void login(String email,String password) {

		enterEmail(email);
		clickSignInLink();
		clickContinue();
		enterPassword(password);
		clickSignIn();

	}
	
	public String incorrectPwd() {
		return ElementsUtil.findElementUsingId(driver, pwdErrMsgBox).getText();
	}

}
