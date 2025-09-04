package utils;

public class CredentialsManager {
	
	public static String getAmazonUser() {
		LogHelper.info("Fetching Amazon user credential from environment variables");
		String userId = System.getenv("AMAZON_USER");
		if(userId==null) {
			LogHelper.error("Environment variable AMAZON_USER is not set!");
			throw new RuntimeException("Environment variables AMAZON_USER not set!");
		}
		else {
			return userId;}
	}
	
	public static String getAmazonPass() {
		LogHelper.info("Fetching Amazon password from environment variables");
		String pass = System.getenv("AMAZON_PASS");
		if(pass==null) {
			LogHelper.error("Environment variable AMAZON_PASS is not set!");
			throw new RuntimeException("Environment variables AMAZON_PASS not set!");
		}
		return pass;
	}
	
	public static String getGmailUser() {
		LogHelper.info("Fetching Gmail user from environment variables");
		String userId = System.getenv("GMAIL_USER");
		if(userId==null) {
			LogHelper.error("Environment variable GMAIL_USER is not set!");
			throw new RuntimeException("Environment variables GMAIL_USER not set!");
		}
		else {
			return userId;}
	}
	
	
	//App Passwords are NOT for manual login (Gmail web or mobile app).If you try to log in manually with it the login will fail
	//First in your gmail account you need to remove any pass key then turn on the two step verification using your mobile number 
	//then when you search "App Password" then you click on the suggestion and there enter any text for app name then 
	//app password like "abcde fghi jklm nopq" will be generated >save it as it is without removing space in between in system 
	//environment variable
	public static String getGmailAppPass() {
		LogHelper.info("Fetching Gmail App Password from environment variables");
		String pass = System.getenv("GMAIL_APP_PASS");
		if(pass==null) {
			LogHelper.error("Environment variable GMAIL_APP_PASS is not set!");
			throw new RuntimeException("Environment variables GMAIL_APP_PASS not set!");
		}
		return pass;
	}
		
}
