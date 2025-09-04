package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.ElementsUtil;
import utils.WaitHelper;

public class HomePage {
	
	private WebDriver driver;
	private ElementsUtil elementsUtil;
	private WaitHelper waitHelper;
	
	public HomePage(WebDriver driver) {
		this.driver=driver;
		this.elementsUtil=new ElementsUtil(driver);
		this.waitHelper=new WaitHelper(driver);
	}
	
	private By greetingText=By.id("nav-link-accountList-nav-line-1");
	
	public String loginGreetings() {
		return elementsUtil.findElement(greetingText).getText();
	}

}



/*
 * If the driver field itself is never accessed directly inside HomePage or
 * LoginPage methods, the IDE flags it as “unused”. You can safely keep it
 * because it’s passed to helpers, and in enterprise frameworks, it’s common to
 * store driver in page objects for future use.
 */


/*
 * why these variables are private and not protected? private WebDriver driver;
 * private ElementsUtil elementsUtil; private WaitHelper waitHelper;
 * 
 * Rule of Thumb: Use private unless you explicitly need subclass access
 * Encapsulation: only the page object should control driver usage. Exposing it
 * increases risk of misuse.
 */
