package bricks;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.ITestResult;
import org.testng.Reporter;

//import org.openqa.selenium.Dimension;
//import org.openqa.selenium.WebElement;
//import java.io.InputStreamReader;

// The original example I plagarized had an abstract class. This caused adventure because the childlins
// didn't have a parent driver to refer to by the time annotations involving the driver were being kicked off.
// Using getters didn't help. I had to revert to shenanigans that made the point of a parent class m00t.
// I want all this stuff in one place so I can encapsulate parnemamitrazation logic. With this as something real,
// the kids can refer to the driver here once I figure out which driver to use.
public class PapaBless {
	private String localUrl;
	private static int timeout;
	private static String timeoutValue;
	protected WebDriverWait wait;  //for the child classes
	
	private DesiredCapabilities capability; 
	private ChromeOptions cOpt;
	private static String hubUrl;
	private static String nodeUrl;
	private static String landingPageUrl;
	private static String browser;
	private static String hubOS;
	private static String nodeOS;
	private String absPath = new File("").getAbsolutePath();
	private String optionsPath;
	private static String OS;
	
	private static boolean everythingsSwell;
	private static String whatDoneSploded;
	
	private URL url;
	
	protected WebDriver driver; //for the child classes
	
	private static boolean onGrid = false;
	
	private final static boolean HANDLE_ERROR = true;
	private final static boolean IGNORE_ERROR = false;
	
	private String options;
	private String multiple;
	
	private void someoneSetUsUpTheDriver(String nodeOSP, String nodeUrlP, String browserP, String timeoutValue) {
		
		everythingsSwell = true;
		whatDoneSploded = ""; //im making an empty immutable string and nobody can stop me. HA HA!
			
		hubOS = System.getProperty("os.name").toLowerCase();
		
	    if(everythingsSwell) {
	    	try { 
	    		timeout = Integer.parseInt(timeoutValue.trim());
	    	} catch (Exception e) {
	    		whatDoneSploded = timeoutValue + " is not a number for timeout. You are silly.";
	    		everythingsSwell = false;
	    	}
	    }
	    
	    if(everythingsSwell) {
	    	if(isValidIP(nodeUrlP,HANDLE_ERROR)) {
	    		if(nodeUrlP.matches("127.0.0.1|localhost")) {
	    			onGrid = false;
	    			nodeUrl = nodeUrlP;
	    		} else {
	    			onGrid = true;
	    			nodeUrl = "http://" + nodeUrlP + ":5555/wd/hub";
	    		}	
	    	}
		}
	    
	    if(everythingsSwell) {
	    	if(onGrid) {
	    		if(!nodeOSP.matches("win7|win10|linux")) {
	    			whatDoneSploded = nodeOSP + " is not a recognized nodeOS. You are silly.";
	    			everythingsSwell = false;
	    		} else {
	    		nodeOS = nodeOSP;
	    		}
	    	}
	    	
	    	if(!onGrid) {nodeOS = hubOS;}
	    }
	    
	    if(everythingsSwell) {
	    	if(nodeOS.equals("linux")) {
	    		if(browserP.matches("firefox|chrome")) {
	    			browser = browserP;
	    		} else {
	    			everythingsSwell = false;
	    		}
	    	} else {
	    		if(browserP.matches("firefox|chrome|ie")) {
	    			browser = browserP;
	    		} else {
	    			everythingsSwell = false;
	    		}
	    	} 
	    	
	    	if(!everythingsSwell) {
	    		whatDoneSploded = browserP + " is not a supported browser for " + nodeOS;
	    	}
	    }
	    
	    if(everythingsSwell && !onGrid) {
	    	try {
	    		InetAddress localhost = InetAddress.getLocalHost();
	    		hubUrl = localhost.getHostAddress().trim();
	    	} catch (Exception e) {
	    		System.out.println(e.getMessage());
	    		everythingsSwell = false;
	    	}
	    }
	    
	    if(everythingsSwell && onGrid) {
	    	hubUrl = " ";
	    	try { 
	    		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	            while (interfaces.hasMoreElements()) {
	                NetworkInterface iface = interfaces.nextElement();
	                
	                // filters out 127.0.0.1 and inactive interfaces
	                if (iface.isLoopback() || !iface.isUp()) continue;
	                
	                Enumeration<InetAddress> addresses = iface.getInetAddresses();
	                while(addresses.hasMoreElements() && hubUrl.equals(" ")) {
	                    InetAddress addr = addresses.nextElement();
	                    String ip = addr.getHostAddress();
	                    if(isValidIP(ip,IGNORE_ERROR)) {hubUrl = ip;}
	                }
	            }
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }  
	    }
	    		
	    if(everythingsSwell) {landingPageUrl = "http://" + hubUrl + ":8080";}
	}

	// Returns whether the IPv4 Address is valid or not.
	// If handlingError, sets error flags and message to log.
	// TODO: support for IPv6
	private static boolean isValidIP(String parm, boolean handlingError) { 
		
		if(parm.matches("127.0.0.1|localhost")) {return true;}
		
		if(parm.contains(":")) {
			if(handlingError) {
				whatDoneSploded = "ey b0ss, no http or port, please: " + parm;
				everythingsSwell = false;
			}
			return false;
		}
		
		int firstDot = parm.indexOf(".",1);
		int secondDot = parm.indexOf(".",firstDot + 1);
		int thirdDot = parm.indexOf(".",secondDot + 1);
		try {
			int number1 = Integer.parseInt(parm.substring(0,firstDot));
			int number2 = Integer.parseInt(parm.substring(firstDot + 1,secondDot));
			int number3 = Integer.parseInt(parm.substring(secondDot + 1,thirdDot));
			int number4 = Integer.parseInt(parm.substring(thirdDot + 1));
			if((number1 < 256) && (number2 < 256) && (number3 < 256) && (number4 < 256) ) {
				return true;
			} else {
				if(handlingError) {
					whatDoneSploded = parm + " is not a valid IP address. You are silly.";
					everythingsSwell = false;
				}
				return false;
			}
		} catch (Exception e) {
			if(handlingError) {
//				TODO:Set this up to handle >1 error messages
//				System.out.println(e.getMessage());
//				Reporter.log(e.getMessage());
//				System.out.println(parm + " is not a valid IP address. You are silly.");
//				Reporter.log(parm + " is not a valid IP address. You are silly.");
				whatDoneSploded = parm + " is not a valid IP address. You are silly.";
				everythingsSwell = false;
			}
			return false;
		}
		
		//return false; // defaults to here not returning earlier
	}	
		
	public WebDriver getDriver() {
		return driver;
	}
	
	public WebDriverWait getWait() {
		return wait;
	}
	
	public String getLocalURL() {
		return localUrl;
	}
	
	public boolean getEverythingsSwell() {
		return everythingsSwell;
	}
	
	public String getWhatDoneSploded() {
		return whatDoneSploded;
	}
	
	@BeforeSuite // before each <suite> in the xml
	@Parameters ({"nodeOSP", "nodeURLP", "browserP", "timeoutP"})
	public void beforeSuite(String nodeOSP, String nodeURLP, String browserP, String timeoutP) {
		System.out.println("**********************************************************************");
		System.out.println("@BeforeSuite kicks off for " + this.getClass().getName());
		System.out.println(nodeOSP + " " + nodeURLP + " " + browserP + " " + timeoutP);
		System.out.println("**********************************************************************");
		someoneSetUsUpTheDriver(nodeOSP, nodeURLP, browserP, timeoutP);
	}
	
	@BeforeTest // before each <test> in the xml
	public void beforeTest() {
		System.out.println("@BeforeTest kicks off when everythingsSwell = " + everythingsSwell + " for " + this.getClass().getName());
	}

	@BeforeClass //before each <class> in the xml
	public void beforeClass() {
		System.out.println("@BeforeClass kicks off for " + this.getClass().getName());
	
		if(onGrid && everythingsSwell) { 
//			capability = new DesiredCapabilities();
			
			switch(browser) {
				case "ie": capability = DesiredCapabilities.internetExplorer(); break;
				case "internet explorer": capability = DesiredCapabilities.internetExplorer(); break;
				case "firefox": capability = DesiredCapabilities.firefox(); break;
				case "chrome": capability = DesiredCapabilities.chrome(); break;
			}
				
			try {
				url = new URL(nodeUrl);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			try {
				System.out.println("*****************************Trying to connect to " +
						nodeUrl + " " + nodeOS + " " + browser);
				driver = new RemoteWebDriver(url,capability);
			} catch(Exception e) {
				System.out.println(e.getMessage());
				whatDoneSploded = e.getMessage();
				everythingsSwell = false;
			}
		}
		
		if (!onGrid && everythingsSwell) {
			String exeExt = new String();
			String driverPath = new String();
//			TODO: make this an option with a default
//			Ubuntu 18.04 no lieky /webdrivers/ right off the hard drive
			if(hubOS.equals("linux")) {
				exeExt = "";
				driverPath = "/webdrivers/";
			} else {
				exeExt = ".exe";
				driverPath = "C:\\webdrivers\\";
			}
			switch(browser) {
			case "firefox":
				System.setProperty("webdriver.gecko.driver",driverPath + "geckodriver" + exeExt);
				driver = new FirefoxDriver();
				break;
			case "chrome":
				System.setProperty("webdriver.chrome.driver",driverPath + "chromedriver" + exeExt);
				ChromeOptions ugh = new ChromeOptions();
				ugh.addArguments("disable-infobars");
//				// On Linux start-maximized does not expand browser window to max screen size. Always set a window size and position.
//				if (platform_name.equalsIgnoreCase("linux")) {
//					options.addArguments(Arrays.asList("--window-position=0,0"));
//					options.addArguments(Arrays.asList("--window-size=1920,1080"));	
//					} else {
//					options.addArguments(Arrays.asList("--start-maximized"));
//					}
//				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//				} 
				driver = new ChromeDriver(ugh);
				break;
			case "ie":
				System.setProperty("webdriver.internetexplorer.driver","C:\\webdrivers\\IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				break;
			}
		}
		
		if(everythingsSwell) {
			driver.get(landingPageUrl);
			wait = new WebDriverWait(driver, timeout);
		} 
	}
	
	@BeforeMethod // before each @Test method in this class
	public void beforeMethod(ITestResult r) {
		System.out.println("@BeforeMethod kicks off");
		driver.findElement(By.xpath("//a[@href='/html5']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='checkBoxSelection1']")));
	}
	
	@AfterMethod // after each @Test method in this class
	public void afterMethod() {
		System.out.println("@AfterMethod kicks off");
	}
	
	@AfterTest // after each </test> in the xml
	public void afterTest() {
		System.out.println("@AfterTest kicks off");
	}
	
	@AfterClass // after each </class> in the xml
	public void afterClass() {
		System.out.println("@AfterClass kicks off");
		driver.close();
	}
	
	@AfterSuite // after each </suite> in the xml
	public void afterSuite() {
		System.out.println("@AfterSuite kicks off");
	}
}