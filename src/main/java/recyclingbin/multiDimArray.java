/*
package recyclingbin;

import org.openqa.selenium.By;

public class multiDimArray {
	private static boolean[][] test = new boolean[4][4]; 
	//setting up test cases
	test[1][1] = true;
	test[1][2] = true;
	test[1][3] = false;
	
	test[2][1] = true;
	test[2][2] = false;
	test[2][3] = true;
	
	test[3][1] = false;
	test[3][2] = true;
	test[3][3] = true;

driver.get(url);
for(int testcase = 1; testcase < test.length; testcase++) {
for(int value = 1; value < test[testcase].length; value++) {
	if(test[testcase][value] == true) {
		driver.findElement(By.xpath("//input[@id='checkBoxSelection" + value +"']")).click(); // set status instead?
	} 
}
}
*/