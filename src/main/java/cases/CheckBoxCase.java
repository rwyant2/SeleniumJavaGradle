package cases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import bricks.PapaBless;

public class CheckBoxCase extends PapaBless {
	
	private static String actualText;
	
	@Test(priority=2)	
	public void checkBoxOneTwo() {			
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection1']")).click();  
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection2']")).click();
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='checkBoxResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='checkBoxResult']")).getText();
		Assert.assertTrue(actualText.contains("one"));
		Assert.assertTrue(actualText.contains("two"));
		Assert.assertFalse(actualText.contains("three"));
	}

	@Test(priority=2)
	public void checkBoxTwoThree () {
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection2']")).click();  
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection3']")).click();
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='checkBoxResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='checkBoxResult']")).getText();
		assertFalse(actualText.contains("one"));
		assertTrue(actualText.contains("two"));
		assertTrue(actualText.contains("three"));
	}

	@Test(priority=2)
	public void checkBoxOneThree() {
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection1']")).click();  
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection3']")).click();
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='checkBoxResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='checkBoxResult']")).getText();
		assertTrue(actualText.contains("one"));
		assertFalse(actualText.contains("two"));
		assertTrue(actualText.contains("three"));
	}

	@Test(priority=2) // Meant to see if I'm handling failures right
	public void intentionalFailcheckBox() {
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection1']")).click();  
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection3']")).click();
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='checkBoxResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='checkBoxResult']")).getText();
		assertTrue(actualText.contains("one"));
		assertFalse(actualText.contains("two"));
		assertTrue(actualText.contains("over 9000"));
	}
	
	@Test(priority=2)
	public void checkBoxAll() {
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection1']")).click();
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection2']")).click();  
		super.driver.findElement(By.xpath("//input[@id='checkBoxSelection3']")).click();
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='checkBoxResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='checkBoxResult']")).getText();
		assertTrue(actualText.contains("one"));
		assertTrue(actualText.contains("two"));
		assertTrue(actualText.contains("three"));
	}
	
	@Test(priority=2)
	public void checkBoxNone() {	
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();
		//This is going to empty, hence not visible.
		super.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@id='checkBoxResult']"))); 
		actualText = super.driver.findElement(By.xpath("//p[@id='checkBoxResult']")).getText();
		assertFalse(actualText.contains("one"));
		assertFalse(actualText.contains("two"));
		assertFalse(actualText.contains("three"));
	}
}
