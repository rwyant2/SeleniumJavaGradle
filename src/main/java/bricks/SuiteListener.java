package bricks;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.Reporter;

public class SuiteListener implements ISuiteListener{

	@Override
	public void onStart(ISuite suite) {
		System.out.println("onStart in SuiteListener kicks off for " + suite.getName());
//		FYI: Reporter.log doesn't work here either. :(
		//Reporter.log("browser param = " + suite.getParameter("browserP"));
	}
	
	@Override
	public void onFinish(ISuite suite) {
		System.out.println("onFinish in SuiteListener kicks off for " + suite.getName());
	}
	
}
