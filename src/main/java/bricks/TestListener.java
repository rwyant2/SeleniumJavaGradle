package bricks;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
//import org.testng.IInvokedMethod;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestNG;

public class TestListener implements ITestListener {
	TestNG whoThisIsListeningTo;
	
	//experiment, can I access the object this is listening to?!?!
	//This causes goofs in GrandpaBless. I would have to explicitly tie
	//this object to the TestNG object, which is already done in TestNG's
	//guts somewhere.
	//	public TestListener(TestNG in) {
//		whoThisIsListeningTo = in;
//	}
	
	@Override
	public void onTestStart(ITestResult result) { // before each @Test method 
		System.out.println("onTestStart kicks off for " + result.getMethod());
	}

	@Override
	public void onTestSuccess(ITestResult result) { // after each successful @Test method
		System.out.println("onTestSuccess kicks off for " + result.getMethod());
	}

	@Override
	public void onTestFailure(ITestResult result) { // after each failed @Test method
		System.out.println("onTestFailure kicks off for " + result.getMethod());
		Object currentClass = result.getInstance();
		WebDriver driver = ((PapaBless)currentClass).getDriver();
		TakesScreenshot driverWithScrShot = (TakesScreenshot)driver; // cast driver obj as driver obj with screenshot
		File scrShot = driverWithScrShot.getScreenshotAs(OutputType.FILE); // get the actual screenshot
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy_hh-mm-ss-SSS"); // format for date
		Date date = new Date(); // get current datetime
		String timestamp = new String(dateFormat.format(date));
		String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "/target/surefire-reports";
		File destFile = new File((String)
				reportDirectory
				+ "/failure_screenshots/"
				+ result.getName()
				+ "_" 
				+ timestamp
				+ ".png");
		try {
			FileUtils.copyFile(scrShot, destFile);
			Reporter.log("<a href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
		} catch(Exception e) {
			System.out.println("screenshot of " + timestamp + " failed.");
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("onTestSkipped kicks off for " + result.getMethod());
		Object currentClass = result.getInstance();
		WebDriver driver = ((PapaBless)currentClass).getDriver();
		
		if(driver != null) { // If we get a driver somehow, try to take a screenshot
			TakesScreenshot driverWithScrShot = (TakesScreenshot)driver; // cast driver obj as driver obj with screenshot
			File scrShot = driverWithScrShot.getScreenshotAs(OutputType.FILE); // get the actual screenshot
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy_hh-mm-ss-SSS"); // format for date
			Date date = new Date(); // get current datetime
			String timestamp = new String(dateFormat.format(date));
			try {
				FileUtils.copyFile(scrShot, new File("C:\\Users\\Richard\\Desktop\\screenshots\\Selenium_fun_times" +
						timestamp + ".png")); //parameterize this
			} catch(Exception e) {
				System.out.println("screenshot of " + timestamp + " failed.");
			}
		}
		
		// If PapaBless caught an error, get the error and Reporter.log it.
		if(!((PapaBless)currentClass).getEverythingsSwell()) { 
			Reporter.log(((PapaBless)currentClass).getWhatDoneSploded());
		}
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("onTestFailedPercentage kicks off for " + result.getMethod());
	}

	@Override
	public void onStart(ITestContext context) { // for everything in a <test/>
		System.out.println("onStart in TestListener kicks off for " + context.getName());
	}

	@Override 
	public void onFinish(ITestContext context) { 
		System.out.println("onFinish in TestListener kicks off for " + context.getName());
	}
}
