package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import utils.LogHelper;

public class TestListener implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		LogHelper.info("Listener: Test Started - "+ result.getName());
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		LogHelper.info("Listener: Test Passed - "+result.getName());
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		//here we have LogHelper.error not LogHelper.info
		LogHelper.error("Listener: Test Failed - "+result.getName(),result.getThrowable());
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		LogHelper.warn("Listener: Test Skipped - "+result.getName());
		
	}



	@Override
	public void onStart(ITestContext context) {
		LogHelper.info("Listener: Test Context Started - "+context.getName());
		
	}

	@Override
	public void onFinish(ITestContext context) {
		LogHelper.info("Listener: Test Context Finished - "+context.getName());
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}
	


}
