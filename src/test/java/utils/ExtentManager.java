package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	
	private static ExtentReports extent;
	private static String extentReportPath;
	
	   /**
     * Creates an ExtentReports instance with a timestamped report name
     * and configures the report with a clean, enterprise-ready look.
     *
     * @param filePath Path where the report will be saved
     * @return ExtentReports instance
     */
	
	
	
	public static ExtentReports createInstance(String filePath) {
		String dateFolder=DateTimeUtil.getDateFolder();
		System.out.println("##########################################################################################"+dateFolder);
		String timeStamp=DateTimeUtil.getTimestampedName(filePath);
		String reportFile=timeStamp+".html";
		//To get the absolute path of the extent report on my laptop
		Path extentReportPath=Paths.get(System.getProperty("user.dir"),"test-output","old",dateFolder,reportFile);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+extentReportPath);
        Files.createDirectories(extentReportPath.getParent());
		
		ExtentSparkReporter sparkReporter=new ExtentSparkReporter(reportFile);
		
		//Report configuration
		sparkReporter.config().setDocumentTitle("Automation Test Report");
		sparkReporter.config().setReportName("amazon.com automation using java selenium");
		sparkReporter.config().setTheme(Theme.STANDARD);
		sparkReporter.config().setEncoding("utf-8");
		
		
		extent= new ExtentReports();
		extent.attachReporter(sparkReporter);
		
		
		// Optional: System info for enterprise reporting
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("JAVA Version", System.getProperty("java.version"));
		extent.setSystemInfo("Browser",System.getProperty("browser", "chrome"));
		extent.setSystemInfo("Environment", System.getProperty("Environment", "qa"));
		
		
		
		//"browser" and "env" are Java system properties, not OS-level environment variables.
		//System.getProperty("browser", "chrome") looks for a system property named "browser". If it's not set, it defaults to "chrome".
		//System.getProperty("env", "qa") does the same for the environment, defaulting to "qa"
		
		return extent;
	}
	
	
	/*
	 * Returns the current ExtentReports instance
	 */
	
	public static ExtentReports getExtent() {
		if(extent==null) {
			throw new RuntimeException("ExtentReports not initialized.Call createInstance() first.");
		}
		return extent;
	}
	
	public static String getExtentReportAbsPath() {
		return extentReportPath;
	}
	
}

