package recyclingbin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

// This class is meant as a smoke test for Selenium itself. Is it installed and working right?
// This will require internet access to work.

public class CheckSelenium {
	private static String url = "http://demo.guru99.com/selenium/newtours/";
	private static String expectedTitle = "Welcome: Mercury Tours";
	
	@Test
	public void isFFDriverGood() {
    	System.setProperty("webdriver.gecko.driver","C:\\webdrivers\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();   	
        driver.get(url);
        Assert.assertEquals(driver.getTitle(),expectedTitle);
        driver.close();
	}
	
	@Test
	public void isChromeDriverGood() {
		System.setProperty("webdriver.chrome.driver","C:\\webdrivers\\2_32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver(); 	
		driver.get(url);
        Assert.assertEquals(driver.getTitle(),expectedTitle);
        driver.close();
	}
	
	@Test
	public void isIEDriverGood() {
		System.setProperty("webdriver.ie.driver", "C:\\webdrivers\\IEDriverServer.exe");
		WebDriver driver = new InternetExplorerDriver();
		driver.get(url);
        Assert.assertEquals(driver.getTitle(),expectedTitle);
        driver.close();
    }
}
