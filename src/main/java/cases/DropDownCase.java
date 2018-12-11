package cases;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import bricks.PapaBless;

public class DropDownCase extends PapaBless {
	private WebDriver driver = this.getDriver();
	private WebDriverWait wait = this.getWait();
	private static String actualText;
	
	@Test
	public void dropDownTomato() {
		Select dropDown = new Select(super.driver.findElement(By.xpath("//select[@id='dropDownSelection']")));  
		dropDown.selectByVisibleText("tomato");
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='dropDownResult']")));
		// reminder: assert with TestNG fails and stops, verify with JUnit fails and continues
		actualText = super.driver.findElement(By.xpath("//p[@id='dropDownResult']")).getText();
		assertEquals("red",actualText);
	}
	
	@Test
	public void dropDownMustard() {
		Select dropDown = new Select(super.driver.findElement(By.xpath("//select[@id='dropDownSelection']")));  
		dropDown.selectByVisibleText("mustard");
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='dropDownResult']")));
		// reminder: assert with TestNG fails and stops, verify with JUnit fails and continues
		actualText = super.driver.findElement(By.xpath("//p[@id='dropDownResult']")).getText();
		assertEquals("yellow",actualText);
	}
		
	@Test
	public void dropDownOnion() {
		Select dropDown = new Select(super.driver.findElement(By.xpath("//select[@id='dropDownSelection']")));  
		dropDown.selectByVisibleText("onions");
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='dropDownResult']")));
		// reminder: assert with TestNG fails and stops, verify with JUnit fails and continues
		actualText = super.driver.findElement(By.xpath("//p[@id='dropDownResult']")).getText();
		assertEquals("white",actualText);
	}

	@Test
	public void dropDownDefault() {  
		super.driver.findElement(By.xpath("//input[@type='submit']")).click();	
		super.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='dropDownResult']")));
		// reminder: assert with TestNG fails and stops, verify with JUnit fails and continues
		actualText = super.driver.findElement(By.xpath("//p[@id='dropDownResult']")).getText();
		assertEquals("yellow",actualText);
	}
}
