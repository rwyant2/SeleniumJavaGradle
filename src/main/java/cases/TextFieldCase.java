package cases;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import bricks.PapaBless;

public class TextFieldCase extends PapaBless {
	private static String actualText;
	
	// priorities are left over from an earlier experiment, but they won't hurt
	@Test (priority = 2)
	public void testTextField() {
		final String inputText = "It's just a prank, bro.";
		super.driver.findElement(By.name("textField")).sendKeys(inputText);  
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		// This is what Clark Kent does at the dentists's.
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='textResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='textResult']")).getText();
		Assert.assertEquals(inputText,actualText);
	}
	
	@Test (priority = 2) // Meant to test if I'm handling failures right
	public void intentionalFailTextField() { 
		final String inputText = "This should fail";
		super.driver.findElement(By.name("textField")).sendKeys(inputText);  
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		// The last super.wait joke was bad and I should feel bad. Code sober, kids. Be professionable.
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='textResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='textResult']")).getText();
		Assert.assertEquals("Please fail",actualText);
	}
}
