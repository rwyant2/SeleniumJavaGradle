package cases;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import bricks.PapaBless;

public class TextAreaCase extends PapaBless {
	private WebDriver driver = this.getDriver();
	private WebDriverWait wait = this.getWait();
	private static String actualText;	
		
	@Test
	public void textAreaLongString() {
		String inputText = "I can't think of anything clever to say here, either."
				+ "I wish the bus wasn't so rough to ride on. They're all drove like the General Lee."
				+ "It's hard to type when the bus is all over the place like that";
		super.driver.findElement(By.name("textArea")).sendKeys(inputText);  
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='textAreaResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='textAreaResult']")).getText();
		Assert.assertEquals(inputText,actualText);
	}
		
	@Test
	public void textAreaWithEnter() {
		String[] inputTextWithEnter = new String [3];
		inputTextWithEnter[0] = "I can't think of anything clever to say here, either.";
		inputTextWithEnter[1] = "I wish the bus wasn't so rough to ride on. They're all drove like the General Lee.";
		inputTextWithEnter[2] = "It's hard to type when the bus is all over the place like that";  
		super.driver.findElement(By.name("textArea")).sendKeys(inputTextWithEnter[0]);
		super.driver.findElement(By.name("textArea")).sendKeys(Keys.RETURN);
		super.driver.findElement(By.name("textArea")).sendKeys(inputTextWithEnter[1]);
		super.driver.findElement(By.name("textArea")).sendKeys(Keys.RETURN);
		super.driver.findElement(By.name("textArea")).sendKeys(inputTextWithEnter[2]);
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='textAreaResult']")));
		actualText = super.driver.findElement(By.xpath("//p[@id='textAreaResult']")).getText();
		Assert.assertEquals(inputTextWithEnter[0] + " "
				+ inputTextWithEnter[1] + " " 
				+ inputTextWithEnter[2],actualText);		
	}
}
