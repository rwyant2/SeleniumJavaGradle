package exe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
//import java.io.StringReader;
import java.net.HttpURLConnection;
//import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
//import java.util.function.Consumer;
//import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.xml.sax.InputSource;
//import bricks.MethodListener;
import bricks.SuiteListener;
import bricks.TestListener;


// This is the one that drives everything and is ran from the hub machine.
// * Reads the .csv file and loops through until no more configs.
// * Reads options.txt to determine environmental options.

public class GrandpaBless {
	private static boolean everythingsSwell = true;
	private static String absPath = new File("").getAbsolutePath();
	private static String filePath;
	private static String csvStream;
	private static String optStream;
	private static Scanner sc;
	private static List<XmlSuite> suites = new ArrayList<XmlSuite>();
	private static ITestNGListener sListener = new SuiteListener();
	private static ITestNGListener tListener = new TestListener();
//	private static ITestNGListener mListener = new MethodListener();
	
	// While I'm sure there are less complicated ways to do this,
	// I get a chance to set up a structure kind of like a table
	// in a relational database. I'm guessing this will come up in
	// reality some day and it would be good to have an example I made
	// from scratch.
	
	private static HashMap<String, HashMap<String,String>> capMap = new HashMap<String, HashMap<String,String>>();
	
	private static int capKey = 0;
	
	private static boolean usingCSV = false;
	
	private static void eyB0ssCanIHabeNoedsPlz() {
		// Go forth and find me nodes... NODES!!1!.
		boolean gridIsPresent = true;
		URL consoleServerUrl = null;
		HttpURLConnection serverCon = null;
		BufferedReader br = null;
		StringBuffer response = new StringBuffer();
		Document doc = null;
			
		// Try to send a request to the grid console's server:
		// http://localhost:4444/grid/console?config=true&configDebug=true
		// Oh, look. It's acting liek a  RESTful service. :D
		String endpoint = "http://localhost:4444";
		String resource = "/grid/console";
		try {
			consoleServerUrl = new URL(endpoint+resource);
		} catch (Exception e) {			
			gridIsPresent = false; // It may not be on and that's fine
		}
		
		if(gridIsPresent) {
			try {
				serverCon = (HttpURLConnection) consoleServerUrl.openConnection();		
			} catch (Exception e) {
				gridIsPresent = false; 
				System.out.println(e.getMessage()); // By now, we should know if there's a grid or not.
			}
		}
		
		if(gridIsPresent) {
			try {
				serverCon.setRequestMethod("GET");
				serverCon.setRequestProperty("config","true");
				serverCon.setRequestProperty("configDebug","true");
				br = new BufferedReader(new InputStreamReader((serverCon.getInputStream())));
				String currentLine;
				while ((currentLine = br.readLine()) != null) {
					response.append(currentLine);
//					System.out.println(currentLine); // for troubleshooting
				}
				doc = Jsoup.parse(response.toString());
//				System.out.println(doc); // for troubleshooting
				serverCon.disconnect();
			} catch (Exception e) {
				gridIsPresent = false;
				System.out.println(e.getMessage());
			}
		}
		
		// So now I have a HTML document object I can find what I need.
		if(gridIsPresent) {
//			for each div with type = "config"
			Elements configs = doc.select("div[type=config]");
			for(Element config : configs) {
//				for each cap in there
				Elements caps = config.select("p:contains(capabilities)");
				for(Element cap : caps) {
					HashMap<String,String> nodeCapMap = new HashMap<String,String>();
					
					int start; int end;
					String ipElement = config.select("p:contains(remoteHost)").toString();
					start = ipElement.indexOf("/") + 2;
					end = ipElement.indexOf(":",start);
					nodeCapMap.put("ip",ipElement.substring(start,end));
					
					String capElement = cap.select("p:contains(browser)").toString();
					start = capElement.indexOf("browserName");
					start = capElement.indexOf(":",start) + 2;
					end = capElement.indexOf(",",start);
					String browser = capElement.substring(start,end).toLowerCase();			
					
					// Oooookay. So. Ie has to always be "windows" regardless of if it's Windows 10 or 7.
					// So I put in a "capability" ignored by grid that allows me to determine the original OS.
					// This comes from the scripts in grid-scripts that kickoff the nodes. 
					if(browser.equals("internet explorer")) {
						nodeCapMap.put("browser", "ie"); // Papabless still looks for "ie"
						start = capElement.indexOf("originalOS");
					} else {
						nodeCapMap.put("browser", browser);
						start = capElement.indexOf("platform");
					}

					start = capElement.indexOf(":",start) + 2;
					end = capElement.indexOf(",",start);
					String platform = capElement.substring(start,end).toLowerCase();
					if(platform.equals("vista")) {
						nodeCapMap.put("os","win7");
					} else {
						nodeCapMap.put("os",platform);
					}

					capMap.put((Integer.toString(capKey)), nodeCapMap);
					capKey++;
				}
			}
		}
		
		// Regardless of if the grid is present or not, I still want to include the localhost as a "node" like the spreadsheet can.
		HashMap<String,String> hostFFMap = new HashMap<String,String>();
		hostFFMap.put("os",System.getProperty("os.name").toLowerCase());
		hostFFMap.put("ip","localhost");
		hostFFMap.put("browser","firefox");
		capMap.put((Integer.toString(capKey)), hostFFMap);
		capKey++;
		
		HashMap<String,String> hostCHMap = new HashMap<String,String>();
		hostCHMap.putAll(hostFFMap);
		hostCHMap.put("browser","chrome");		
		capMap.put((Integer.toString(capKey)), hostCHMap);
		capKey++;
		
		if(!System.getProperty("os.name").toLowerCase().equals("linux")) {
			HashMap<String,String> hostIEMap = new HashMap<String,String>();
			hostIEMap.putAll(hostFFMap);
			hostIEMap.put("browser","ie");		
			capMap.put((Integer.toString(capKey)), hostIEMap);
			capKey++;
		}
		
		
		System.out.println(capMap); // troubleshooting
//		System.out.println(); // troubleshooting
	}
	
	public static void main (String[] args) {
		eyB0ssCanIHabeNoedsPlz();
		
		// For now, I'm assuming the hub machine will be my dev env on Ubutnu
		filePath = absPath + "/src/main/java/exe/";
		
		try(FileInputStream fis = new FileInputStream(filePath + "options.txt")) {  
			optStream = IOUtils.toString(fis,"UTF-8");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			everythingsSwell = false;
		}
		
		if(everythingsSwell) {
			sc = new Scanner(optStream);
			while(sc.hasNext()) {
				String params = sc.nextLine();
				int start = params.indexOf("useCSV");
				if(start > -1) { 
					start = params.indexOf("'",start) + 1;
					int end = params.indexOf("'",start + 1);
					if(params.substring(start, end).matches("y|Y")) {usingCSV = true;}
					else {usingCSV = false;}
				}
			}
		}
		
		if(usingCSV && everythingsSwell) {
			try(FileInputStream fis = new FileInputStream(filePath + "multiple.csv")) {  
				csvStream = IOUtils.toString(fis,"UTF-8");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				everythingsSwell = false;
			}
		}
		
		if(everythingsSwell && usingCSV) {
			sc = new Scanner(csvStream);
			System.out.println(sc.nextLine()); // assuming 1st line is header
			while(sc.hasNext()) {
				String params = sc.nextLine();
				
//				ignore comments and empty lines
				if(params.isEmpty()) {continue;}
				if(params.substring(0,2).equals("//")) {continue;}
				
				int first = params.indexOf(",");
				int second = params.indexOf(",",first+1);
				int third = params.indexOf(",",second+1);				
				String nodeOS = params.substring(0, first);
				String nodeURL = params.substring(first+1, second);
				String browser = params.substring(second+1, third);
				String timeout = params.substring(third+1, params.length());
				
				// If the IP address of the node is empty, check the capMap
				// for something that has those caps.
				// TODO: I don't like nesting this deep. Maybe split this into it's own method.
				if(nodeURL.isEmpty()) {
					Iterator<?> i = capMap.entrySet().iterator();
					while (i.hasNext()) {
						Map.Entry capEntry = (Map.Entry) i.next();
						HashMap<String,String> capData = (HashMap<String,String>) capEntry.getValue();
						
						if(browser.equals("ie") &&
								capData.get("browser").equals("internet explorer") &&
								capData.get("os").equals(nodeOS)) {
							nodeURL = (String) capData.get("ip"); break; // We found our match, moving on.
						}
							
						// if the browser and nodeOS are a match, use this IP address
						if((capData.get("browser").equals(browser)) && (capData.get("os").equals(nodeOS))) {
							nodeURL = (String) capData.get("ip"); break; // We found our match, moving on.
						}
//				        System.out.println(capEntry.getKey() + " = " + capList); // troubleshooting
					}
				}
				System.out.println(nodeOS + " " + nodeURL + " " + browser + " " + timeout); // troubleshooting
				suites.add(buildSuite(nodeOS,nodeURL,browser,timeout));
			}
		}
			
		// If we're not using the spreadsheet, make suites based on what's out there. 
		if(everythingsSwell && !usingCSV) {
			Iterator<?> i = capMap.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry capEntry = (Map.Entry) i.next();
				HashMap<String,String> capData = (HashMap<String,String>) capEntry.getValue();
				String nodeOS = capData.get("os");
				String nodeURL = capData.get("ip");
				String browser = capData.get("browser");		
				
				suites.add(buildSuite(nodeOS,nodeURL,browser,"10"));
			}
		}
			
		TestNG tng = new TestNG();
//		tng.addListener(mListener);
		tng.addListener(tListener);
		tng.addListener(sListener);
		tng.setXmlSuites(suites);
		tng.run();
		
		System.out.println("done Done Done done");
		System.out.println("done Done Done done DOOOOOOOOOOOOOOOOONE");
		
	}
	
	private static XmlSuite buildSuite(String nodeOS, String nodeURL, String browser, String timeout) {
		
		// equivalient of <suite> tag
		XmlSuite xs = new XmlSuite();
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy hh:mm:ss a");
		Date date = new Date();
		String timestamp = new String(dateFormat.format(date));
		
		xs.setName(timestamp + " "
			+ nodeOS + "/"
			+ nodeURL + "/"
			+ browser + "/"
			+ timeout
		);
		
		xs.setParallel(XmlSuite.ParallelMode.NONE);// TestNG 6.13 version
//		xs.setParallel(XmlSuite.PARALLEL_NONE); // TestNG 6.8 version
		
		// equivalent of <parameters> tag
		Map <String, String> xp = new HashMap <String, String>();
		xp.put("nodeOSP", nodeOS);
		xp.put("nodeURLP", nodeURL);
		xp.put("browserP", browser);
		xp.put("timeoutP", timeout); // you are here
		xs.setParameters(xp);
		
		// equivalent of <packages> tag
		List<XmlPackage> xPackages = new ArrayList<XmlPackage>();
		xPackages.add(new XmlPackage("cases"));
		xs.setPackages(xPackages);
		
		// equivalent to <test> tag
		XmlTest xtp = new XmlTest();
		xtp.setName("dynamic test with packages"); 
		xtp.setSuite(xs);
		xtp.setPackages(xPackages);
		
		xs.addTest(xtp); // add <test> to the <suite>
		
		return xs;
	}
}				