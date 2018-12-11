package recyclingbin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

// This class is meant to see that TestNG is reporting fails.
// I don't see why it wouldn't work, but since I'm testing the environment anyway.
// And I want to be sure I have suites down.

public class IntentionalFail {
	
	@Test
	public void isAppUp() {
    	System.setProperty("webdriver.gecko.driver","C:\\webdrivers\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();   	
        driver.get("http://localhost:8080/");
        Assert.assertEquals(driver.getTitle(),"ಠ_ಠ");
        driver.close();
    }
}
