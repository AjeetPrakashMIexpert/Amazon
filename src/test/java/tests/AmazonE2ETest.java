package tests;


import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import utils.CredentialsManager;
import utils.LogHelper;

public class AmazonE2ETest extends BaseTest{





	//1. User Authentication (Login/Logout)
	//Flow: Navigate → Click "Sign in" → Enter email & password → Login → Logout.
	
	
	@Test(description = "Verify login with valid credential")
	public void testValidLogin() {
		
		
		
		// Fetch credentials securely
		String username=CredentialsManager.getAmazonUser();
		String password=CredentialsManager.getAmazonPass();

		LogHelper.info("Starting login test for user: " + username);
		
		// Perform login
		loginPage.login(username, password);
		
		
		
		boolean loginSuccess = homePage.loginGreetings().contains("Hello");
		
		// Assert login success using a reliable home page element
		Assert.assertTrue(loginSuccess,"Login failed – user not greeted after login");	
		
		LogHelper.info("Login test completed successfully.");
	}
	
	
	@Test(description = "Verify login fails with valid user but invalid password")
	public void testInvalidPassword() {
		

		
		// Fetch credentials securely
		String username=CredentialsManager.getAmazonUser();
		String password="1234";

		LogHelper.info("Starting negative login test for user: " + username+" with incorrect password");
		
		// Perform login
		loginPage.login(username, password);
		
		
		boolean wrongPwd = loginPage.incorrectPwd().contains("password is incorrect");
		
		// Assert login success using a reliable home page element
		Assert.assertTrue(wrongPwd,"Login failed – Your password is incorrect");	
		
		LogHelper.info("Negative login test completed successfully.");
		
	}

	
	
	









	//2. Product Search + Dynamic Suggestions
	//Flow: Type "iPhone 15" in search box → Handle auto-suggestions → Press Enter → Navigate to results.



	//3. Product Listing → Apply Filters & Sorting
	//Flow: Search product → Apply filters (brand, price, delivery option) → Sort results (e.g., price low to high).


	//4. Product Details Page (PDP)
	//Flow: Click product → Opens in new tab/window → Switch to it → Extract product details (price, rating, description).


	//5. Add to Cart + Verify Cart
	//Flow: Add product to cart → Navigate to cart → Validate product details/price.


	//6. Checkout Flow (Address + Payment Mock)
	//Flow: Proceed to checkout → Add/select delivery address → Select payment option → Stop before real payment.



	//7. Orders & Invoice Download
	//Flow: Go to Orders → Select an order → Download invoice (PDF).


	//8. Wishlist / Save for Later
	//Flow: Save product for later → Move to cart → Validate.


	//9. Profile Management
	//Flow: Navigate to Profile → Update details (like name/phone) → Save → Validate.


	//10. Advanced (Optional but Interview Gold ⭐)
	//Prime Video / Gift Cards / Subscriptions flows

}


