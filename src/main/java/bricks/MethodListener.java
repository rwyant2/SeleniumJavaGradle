package bricks;

import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import junit.framework.TestResult;

public class MethodListener implements IInvokedMethodListener {
    
	@Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
		System.out.println("beforeInvocation kicks off for " + result.getMethod());
// Thanks to this, I'm going to skip SkipException. I'll just fail these and move on
// with my life: https://github.com/cbeust/testng/issues/1632
//		Object currentClass = result.getInstance();
//		if(!((PapaBless)currentClass).getEverythingsSwell()) {
//			result.setStatus(ITestResult.SKIP);
//			throw new SkipException("Skipping the test case");
//		}
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
    	System.out.println("afterInvocation kicks off for " + result.getMethod());
        result.getStatus(); 
    }
}

