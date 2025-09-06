package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Attachment;

public class ScreenshotUtil {
		
	public static String captureScreenshot(WebDriver driver,String fileName) {		
		String dateFolder=DateTimeUtil.getDateFolder();
		String timeStamp=DateTimeUtil.getTimestampedName(fileName);
		//To get the absolute path of the screenshot on my laptop
		Path destination=Paths.get(System.getProperty("user.dir"),"target","screenshots",dateFolder,timeStamp+".png");
		
		try {
			Files.createDirectories(destination.getParent());
			File src=((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(src.toPath(), destination,StandardCopyOption.REPLACE_EXISTING);
			LogHelper.info("Screenshot saved at: "+destination.toString());
			return destination.toString();
			
		} catch (Exception e) {
			LogHelper.error("Failed to save screenshot", e);
	        return null;
		}
		
	}
	
	//for extent report
	public static String capturesScreenshotBase64(WebDriver driver) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
	}
	
	//for allure report
	@Attachment(value = "screenshot - {0}" ,type = "image/png")
	public static byte[] screenshotForAllure(String testName,WebDriver driver) {
		
		try {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		} catch (Exception e) {
			LogHelper.error("Failed to capture screenshot for Allure", e);
			return new byte[0]; //prevent Allure crash
		}
		
	}

}


/*
 * Paths.get("target","screenshots", fileName + "_" + timeStamp + ".png");
 * 
 *  is
 * part of Java’s java.nio.file package and is used to construct a file path in
 * a platform-independent way. Let me break it down clearly:
 * 
 * 1. Paths.get(...) Paths.get() creates a Path object representing a file or
 * directory path.
 * 
 * It automatically handles file separators (/ on Linux/macOS, \ on Windows), so
 * your code is cross-platform.
 * 
 * 2. Arguments: "target", "screenshots", fileName + "_" + timeStamp + ".png"
 * These are concatenated as path segments:
 * 
 * "target" → top-level folder in your project (commonly used in Maven projects
 * for build outputs).
 * 
 * "screenshots" → subfolder inside target.
 * 
 * fileName + "_" + timeStamp + ".png" → the actual file name.
 * 
 * fileName could be something like "loginTest"
 * 
 * timeStamp could be "2025-09-02_0945"
 * 
 * Combined: "loginTest_2025-09-02_0945.png"
 * 
 * 3. Result The Path object represents a file location like:
 * 
 * Windows: target\screenshots\loginTest_2025-09-02_0945.png
 * 
 * Linux/macOS: target/screenshots/loginTest_2025-09-02_0945.png
 * 
 */



/*
 * destination.getParent()
 * 
 * destination is usually a Path object representing a file, e.g.:
 * 
 * Path destination = Paths.get("target", "screenshots",
 * "loginTest_2025-09-02_0945.png");
 * 
 * 
 * getParent() returns the directory path containing the file:
 * 
 * target/screenshots
 * 
 * 2. Files.createDirectories(...)
 * 
 * This method creates the directory (and any nonexistent parent directories) if
 * they don’t exist.
 * 
 * If the directories already exist, it does nothing—it won’t throw an
 * exception.
 */




/*
 * src.toPath()
 * 
 * src is usually a File object, e.g., a screenshot file taken by Selenium.
 * 
 * toPath() converts the File into a Path object, which is required by
 * Files.copy().
 * 
 * 2. destination
 * 
 * This is the target location where you want to copy the file.
 * 
 * Usually a Path object, e.g.:
 * 
 * Path destination = Paths.get("target", "screenshots",
 * "loginTest_2025-09-02_0945.png");
 * 
 * 3. StandardCopyOption.REPLACE_EXISTING
 * 
 * This is an option that tells Java:
 * 
 * If a file already exists at the destination, replace it.
 * 
 * Without this, Java would throw a FileAlreadyExistsException if the file
 * exists.
 * 
 * 4. What it does overall
 * 
 * It copies the file from src to destination.
 * 
 * If the destination file already exists, it overwrites it.
 * 
 * If the destination directories don’t exist, you need to create them first
 * (using Files.createDirectories(destination.getParent()))—otherwise, it will
 * throw an exception.
 * 
 * 
 * 
 * Summary: It saves the screenshot file to your desired folder, replacing any existing file with the same name.
 */





/*
 * String fileName = "loginTest"; String timeStamp = "2025-09-02_0945"; Path
 * destination = Paths.get("target","screenshots", fileName + "_" + timeStamp +
 * ".png");
 * 
 * File file = new File(destination.toString());
 * System.out.println("Saving screenshot at: " + destination.toString()); 1.
 * destination.toString() destination is a Path object, constructed with
 * segments "target", "screenshots", and "loginTest_2025-09-02_0945.png".
 * 
 * toString() converts the Path into a String using the system’s file separator.
 * 
 * So, destination.toString() will be:
 * 
 * Windows:
 * 
 * Copy code target\screenshots\loginTest_2025-09-02_0945.png Linux/macOS:
 * 
 * bash Copy code target/screenshots/loginTest_2025-09-02_0945.png This is
 * exactly what gets printed by:
 * 
 * java Copy code System.out.println("Saving screenshot at: " +
 * destination.toString());
 * 
 * 
 * destination: Printing a Path object calls its toString() method internally by
 * default.
 * 
 * destination.toString(): Explicitly converts the Path object to a string.
 * 
 * 💡 In practice, for System.out.println(), there is no difference between
 * printing the Path object or destination.toString().
 * 
 * 
 * Important Note
 * 
 * The only real difference is if you use the Path object in APIs that
 * specifically require a Path, like:
 * 
 * Files.copy(src.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
 * 
 * 
 * Here, you must pass destination as a Path object, not a string.
 * 
 * toString() gives a string path, but Files.copy() prefers Path.
 */




/*
 * System.getProperty("user.dir") returns the current working directory of the
 * JVM.
 * 
 * Practically → it points to the root of your project when you run tests in
 * Eclipse, IntelliJ, or Maven.
 * 
 * 
 * Say your project is here:
 * 
 * C:\Users\noble\eclipse-workspace\Amazon\
 * 
 * 
 * When you run your tests,
 * 
 * System.out.println(System.getProperty("user.dir"));
 * 
 * 
 * 👉 Output will be:
 * 
 * C:\Users\noble\eclipse-workspace\Amazon
 */





/*
 * Why no try/catch for Base64 method? public static String
 * captureScreenshotBase64(WebDriver driver) { return ((TakesScreenshot)
 * driver).getScreenshotAs(OutputType.BASE64); }
 * 
 * 
 * 👉 In real frameworks:
 * 
 * This method is always called from a listener (ITestListener) or from a
 * wrapper (Extent report logger).
 * 
 * If it fails, Extent report just won’t have the screenshot. It doesn’t break
 * the framework because Extent can tolerate null/empty screenshots.
 * 
 * So many teams skip try/catch here for simplicity, while for file saving /
 * Allure, they wrap in try/catch because those are more error-sensitive (file
 * system + byte[] handling).
 */




/*
 * Why not one big try/catch for everything?
 * 
 * Example of what you mean:
 * 
 * try { // file save // base64 // allure } catch (Exception e) { // handle all
 * }
 * 
 * 
 * 👉 Problem in enterprise-grade frameworks:
 * 
 * Separation of concerns is important.
 * 
 * File saving might fail (disk issue) but Base64 might still succeed.
 * 
 * If you wrap everything in one try/catch, a failure in one part will stop
 * other captures too.
 * 
 * ✅ Best practice = independent try/catch in each method so one failure doesn’t
 * block others.
 */



/*
 * why byte[] in public static byte[] screenshotForAllure(String
 * testName,WebDriver driver)
 * 
 * Allure needs the raw data of the file (not Base64, not a File path).
 * 
 * Screenshots are binary files (PNG, JPG) → so the natural way to represent
 * them in Java is as a byte array (byte[]).
 */



/*
 * What does return new byte[0]; mean?
 * 
 * This is a fallback for error cases.
 * 
 * new byte[0] = creates an empty byte array (length = 0).
 * 
 * Instead of returning null (which could cause Allure to throw
 * NullPointerException), it safely returns an empty array.
 * 
 * Allure will just not render anything for that attachment (or show “empty
 * screenshot”), but your test won’t crash.
 */