package cases;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import bricks.PapaBless;

public class RadioButtonCase extends PapaBless {
	private WebDriver driver = this.getDriver();
	private WebDriverWait wait = this.getWait();
	private static String actualText;
	
	@Test
	public void radioChoiceA() {
		super.driver.findElement(By.xpath("//input[@id='radioButtonSelection1']")).click();  
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='radioResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='radioResult']")).getText();
		Assert.assertEquals("apple",actualText);
	}
	
	@Test
	public void radioChoiceB() {
		super.driver.findElement(By.xpath("//input[@id='radioButtonSelection2']")).click();  
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@id='radioResult']"))); 
		actualText = super.driver.findElement(By.xpath("//p[@id='radioResult']")).getText();
		Assert.assertEquals("banana",actualText);
	}
	
	@Test
	public void radioChoiceC() {
		super.driver.findElement(By.xpath("//input[@id='radioButtonSelection3']")).click();  
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='radioResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='radioResult']")).getText();
		Assert.assertEquals("cherry",actualText);
	}
	
	@Test // default
	public void radioDefault() {  
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@id='radioResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='radioResult']")).getText();
		Assert.assertEquals("",actualText);
	}
}
