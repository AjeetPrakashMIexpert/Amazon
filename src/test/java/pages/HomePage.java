package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.ElementsUtil;

public class HomePage {
	
	private WebDriver driver;
	
	public HomePage(WebDriver driver) {
		this.driver=driver;
	}
	
	private By greetingText=By.id("nav-link-accountList-nav-line-1");
	
	public String loginGreetings() {
		return ElementsUtil.findElementUsingId(driver, greetingText).getText();
	}

}
