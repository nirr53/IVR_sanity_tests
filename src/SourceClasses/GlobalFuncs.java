package SourceClasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.Assert.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
* This class holds all the functions which been used by the tests
* @author Nir Klieman
* @version 1.00
*/

public class GlobalFuncs {
	
	  /**
	  *  webUrl  	  - Default url for the used funcs
	  *  username  	  - Default username for the used funcs
	  *  password 	  - Default password for the used funcs
	  *  StringBuffer - Default string for errors buffering
	  */
	  GlobalVars 		   testVars;
	  MenuPaths            testMenuPaths;
	  private String	   webUrl;
	  @SuppressWarnings("unused")
	  private StringBuffer verificationErrors = new StringBuffer();
	  private static final Logger logger = LogManager.getLogger();

	  /**
	  *  Default constructor
	  */
	  public GlobalFuncs() {
		  
		  testVars 		= new GlobalVars();
		  testMenuPaths = new  MenuPaths();
		  webUrl   	    = testVars.getUrl();
	  }
	  
	  /**
	  *  Constructor which get data
	  *  @param givenUrl      - a given url
	  *  @param givenUsername - a given username
	  *  @param givenPassword - a given password
	  */
	  public GlobalFuncs(String givenUrl, String givenUsername, String givenPassword) {
		 
		  testVars      = new GlobalVars();
		  testMenuPaths = new  MenuPaths(); 
		  webUrl   		= givenUrl;	 
	  }
	  
	  /**
	  *  login method
	  *  @param driver  A given driver for make all tasks
	  *  @param username A given string for system
	  *  @param password A given password for the system
	  *  @param mainStr A given string for verify good access
	  *  @param httpStr A given string for using as a prefix for the url
	  *  @param brwType A name of the used browser
	  */
	  public void login(WebDriver 	driver, String username, String password, String mainStr, String httpStr, String brwType) {
		  
    	  myDebugPrinting("system url - " + httpStr + webUrl,testVars.logerVars.MINOR);
	      driver.get(httpStr + webUrl);
	      myWait(1000);
	      assertTrue("IVR-Fax Admin system was not reached !! (" + driver.getTitle() + ")", driver.getTitle().equals(testVars.getLoginPageStr()));
    	  myDebugPrinting("username - " + username ,testVars.logerVars.MINOR);
    	  myDebugPrinting("password - " + password ,testVars.logerVars.MINOR);
	      driver.findElement(By.name("auth_admin_user_")).clear();
	      driver.findElement(By.name("auth_admin_user_")).sendKeys(username);
    	  myWait(500);
	      driver.findElement(By.name("auth_admin_password_")).clear();
	      driver.findElement(By.name("auth_admin_password_")).sendKeys(password); 
	      driver.findElement(By.xpath("//*[@id='Submit1']")).click();
	      myWait(5000);
    	  assertTrue("Login string was not detected !! <" + driver.getTitle() + ">", driver.getTitle().equals(testVars.getMainPageStr()));
	  }
	  
	  /**
	  *  Delete all files in directory by given prefix
	  *  @param dir - given directory path
	  *  @param prefix - a given prefix
	  */
	  public void deleteFilesByPrefix(String dir, String prefix) {
	    	
		myDebugPrinting("dir    - " + dir   ,  testVars.logerVars.MINOR);
		myDebugPrinting("prefix - " + prefix,  testVars.logerVars.MINOR);
    	File[] dirFiles = new File(dir).listFiles();
    	int filesNum = dirFiles.length;
    	for (int i = 0; i < filesNum; i++) {
    		
    	    if (dirFiles[i].getName().startsWith(prefix, 0)) {
    	    	
    			myDebugPrinting("Delete file - " + dirFiles[i].getName(),  testVars.logerVars.MINOR);
    	        new File(dir + "\\" + dirFiles[i].getName()).delete();
    		    myWait(3000);    
    	    }
    	}	
	  }
	  
	  /**
	  *  Verify string in page based on read the whole page
	  *  @param driver    A given driver
	  *  @param strName   A given string for detect
	  */
	  public void searchStr(WebDriver 	driver, String strName) {
		  
		  String bodyText     = driver.findElement(By.tagName("body")).getText();
		  if (bodyText.contains(strName)) {
			  myDebugPrinting(strName + " was detected !!",  testVars.logerVars.MINOR);
		  } else {
			  myFail(strName + " was not detected !! \nbodyText - \n" + bodyText);
		  }
	  }
	  
	  /**
	  *  Check that several strings exist on given file
	  * @param content - given string
	  * @param data - array of strings
	  */
	  public void checkFile(String content, String[] data) {
		  
		  int dataLen = data.length;
		  for (int i = 0 ; i < dataLen; ++i) {
				
			  myDebugPrinting("data[" + i + "] - " + data[i], testVars.logerVars.MINOR);
			  if (!content.contains(data[i])) {
				  
				  myFail("The value <" + data[i] + "> was not detected !!");
			  } 
		  }
	  }
	  
	  /**
	  *  Verify xpath contains a string
	  *  @param driver    A given driver
	  *  @param elemName  A given element xpath
	  *  @param strName   A given string for detect
	  */
	  public void verifyStrByXpathContains(WebDriver 	driver, String xpath, String strName) {
	  	  
		  if (driver.findElement(By.xpath(xpath)).getText().contains(strName)) {
		  } else {
			  
			  myFail (strName + " was not detected !! (" + driver.findElement(By.xpath(xpath)).getText() + ")");
		  }
	  }
	    
	  /**
	  *  Print a given string to the console
	  *  @param str   - A given string to print
	  *  @param level - A given print level (MAJOR, NORMAL, MINOR, DEBUG)
	  */
	  public void myDebugPrinting(String str, int level) {
		  
		  String spaces = testVars.logerVars.debug[level];
		  logger.info(spaces + str);
	  }
	  
	  /**
	  *  Click on given element by given xpath and waits a given timeout
	  *  @param driver  - a given element
	  *  @param byType  - A given By element (By xpath, name or id)
	  *  @param timeout - a given timeout value (Integer)
	  */
	  public void myClick(WebDriver driver, By byType, int timeout) {
		  
	      driver.findElement(byType).click();
		  myWait(timeout);
	  }
	 
	  /**
	  *  Print a given string to the console with default level of MAJOR
	  *  @param str   - A given string to print
	  */
      public void myDebugPrinting(String str) {
			
		String spaces = testVars.logerVars.debug[testVars.logerVars.MAJOR];
		logger.info(spaces + str);
	  }
      
	  /**
	  *  Print a given error string and declares the test as a myFailure
	  *  @param str   - A given error string
	  */
      public void myFail(String str) {
			
		logger.error(str);
		fail(str);
	  }
	 
	  /**
	  *  Verify string  method by xpath
	  *  @param driver    A given driver
	  *  @param elemName  A given element name
	  *  @param strName   A given string for detect
	  */
	  public void verifyStrByXpath(WebDriver 	driver, String elemName, String strName) {
		  
	   markElemet(driver, driver.findElement(By.xpath(elemName)));
	   try {
		   assert (driver.findElement(By.xpath(elemName)).getText().contains(strName)); 
	        
	   } catch (Error e) {
		   
	    	myDebugPrinting("The curr str is " + strName, testVars.logerVars.DEBUG);
	   }
	  }
	  
	  /**
	  *  Highlight given element
	  *  @param driver     A given driver
	  *  @param element    A given element
	  */
	  public void markElemet(WebDriver 	driver, WebElement element) {
			
		// Mark element
	    try {
	    	
		    ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e1) {
		}
	   ((JavascriptExecutor)driver).executeScript("arguments[0].style.border=''", element);
	  }
	
	  /**
	  *  Sleep for a given time
	  *  @param sleepValue     A given sleep factor in Milliseconds
	  */
	  public void myWait(int sleepValue) {
			
	    try {
	    	
			TimeUnit.MILLISECONDS.sleep(sleepValue);		
		} catch (InterruptedException e1) {
		}	
	  }
	
	  /**
	  *  Create a unique Id based on current time
	  */
	  public String getId() {
		
	    // set id
	    DateFormat dateFormat = new SimpleDateFormat("HH_mm_ssdd");
	    Date date     = new Date();
	    String id     = dateFormat.format(date);
	    id = id.replaceAll("_", "");
		myDebugPrinting("Id is:" + id, testVars.logerVars.MINOR);
		
	    return id;
	  }
	  
	  /**
	  *  Wrap assertTrue with logger
	  *  @param errorStr  - an error message for display at the logger
	  *  @param condition - a condition for mark if the assert succeeded or not
	  */
	  public void myAssertTrue(String errorStr, Boolean condition) {
		  
		  if (!condition) {
			  myFail(errorStr);  
		  }
	  }
	  
	  /**
	  *  Wrap assertFalse with logger
	  *  @param errorStr  - an error message for display at the logger
	  *  @param condition - a condition for mark if the assert succeeded or not
	  */
	  public void myAssertFalse(String errorStr, Boolean condition) {
		  
		  if (condition) {
			  myFail(errorStr);  
		  }
	  }
	  
	  /**
	  *  Send a string to a given elemnent using given parameters
	  *  @param driver  - A given driver
	  *  @param byType  - A given By element (By xpath, name or id)
	  *  @param currUsr - A given string to send
	  *  @param timeout - A given timeout (Integer)
	  */
	  public void mySendKeys(WebDriver driver, By byType, String currUsr, int timeOut) {
		  
		  //driver.findElement(byType).clear();
		  driver.findElement(byType).sendKeys(currUsr);
		  myWait(timeOut);
	  }
	  
	  /**
	  *  Find files in a given directory by a given prefix
	  *  @param dir - given directory path
	  *  @param prefix - a given prefix
	  *  @return true if files were found
	  */
	  public String findFilesByGivenPrefix(String dir, String prefix) {
	    	
			myDebugPrinting("dir    - " + dir   ,  testVars.logerVars.MINOR);
			myDebugPrinting("prefix - " + prefix,  testVars.logerVars.MINOR);
	    	File[] dirFiles = new File(dir).listFiles();
	    	int filesNum = dirFiles.length;
	    	for (int i = 0; i < filesNum; i++) {
	    		
	    	    if (dirFiles[i].getName().startsWith(prefix, 0)) {
	    			
	    	    	myDebugPrinting("Find a file ! (" + dirFiles[i].getName() + ")",  testVars.logerVars.MINOR);
	    	        return dirFiles[i].getName();
	    	    }
	    	}
	    	
	    	return null;
	  }
	  
	  /**
	  *  Return name of files that start with the given prefix
	  *  @param dir - given directory path
	  *  @param prefix - a given prefix
	  *  @return full string of file its name starts with the given prefix
	  */
	  public String returnFilesByGivenPrefix(String dir, String prefix) {
	    	
			myDebugPrinting("dir    - " + dir   ,  testVars.logerVars.MINOR);
			myDebugPrinting("prefix - " + prefix,  testVars.logerVars.MINOR);
	    	File[] dirFiles = new File(dir).listFiles();
	    	int filesNum = dirFiles.length;
	    	for (int i = 0; i < filesNum; i++) {
	    		
	    	    if (dirFiles[i].getName().startsWith(prefix, 0)) {
	    			
	    	    	myDebugPrinting("Find a file ! (" + dirFiles[i].getName() + ")",  testVars.logerVars.MINOR);
	    	        return dirFiles[i].getName();
	    	    }
	    	}
	    	
	    	return "";
	  }
	
	  /**
	  *  Get last file save on given path
	  *  @param  dirPath - directory path
	  *  @return last modified file
	  */
	  public File getLatestFilefromDir(String dirPath) {
		
	    File dir = new File(dirPath);
	    File[] files = dir.listFiles();
	    if (files == null || files.length == 0) {
	    	
	        return null;
	    }
	    File lastModifiedFile = files[0];
	    for (int i = 1; i < files.length; i++) {
	    	
	       if (lastModifiedFile.lastModified() < files[i].lastModified()) {
	    	   
	           lastModifiedFile = files[i];
	       }
	    }
	    
	    return lastModifiedFile;
	  }

	  /**
	  *  Enter a menu
	  *  @param driver   A given driver for make all tasks
	  *  @param menuName A given menu name for the paths function
	  *  @param verifyHeader A string for verify that enter the menu succeeded
	  */
	  public void enterMenu(WebDriver 	driver, String menuName, String verifyHeader) {
		  
		  myDebugPrinting("enterMenu  - " +  menuName, testVars.logerVars.NORMAL);
		  myDebugPrinting("verifyHeader - " +  verifyHeader, testVars.logerVars.MINOR);
		  String paths[] = testMenuPaths.getPaths(menuName);
		  int length = paths.length;
		  for (int i = 0; i < length; ++i) {
		  
			  if (paths[i].isEmpty()) {
				  
				  break;
			  }
			  myDebugPrinting("paths[" + i + "] - " +  paths[i], testVars.logerVars.MINOR);
			  markElemet(driver, driver.findElement(By.xpath(paths[i])));
			  myWait(1000);
		      driver.findElement(By.xpath(paths[i])).click();
			  myWait(4000);
		  }
		  driver.switchTo().frame(1);	
		  String title = driver.findElement(By.tagName("body")).getText();
		  driver.switchTo().defaultContent();
		  Assert.assertTrue("Title was not found (" + title + ")", title.contains(verifyHeader));
		  myDebugPrinting("enterMenu  - " +  menuName + " ended successfully !!", testVars.logerVars.NORMAL);
		  myWait(3000);
	  }

	  /**
	  * Set a driver according to a given browser name
	  * @param  usedBrowser - given browser name (Chrome, FireFox or IE)
	  * @return driver      - a driver object
	  */
	  public WebDriver defineUsedBrowser(String usedBrowser) {
			
		if 		  (usedBrowser.equals(testVars.CHROME)) {
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			return new ChromeDriver(options);
			
		} else if (usedBrowser.equals(testVars.FF))     {
			
			myDebugPrinting(testVars.getGeckoPath());
			System.setProperty("webdriver.gecko.driver", testVars.getGeckoPath());
			System.setProperty("webdriver.firefox.marionette", "false");
			return new FirefoxDriver();
		} else if (usedBrowser.equals(testVars.IE))     {    	
			
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability("requireWindowFocus", true);   	
			return new InternetExplorerDriver(capabilities);	
		
		}  else   
		{
			myFail ("The browser type is invalid - " + usedBrowser);
		}
		
		return null;
	  }  
  
	  /**
	  *  Generate a random ID based on current time
	  *  @return id - a random id sting
	  */
	  public String GenerateId() {
		  
		DateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
	  	Date date  = new Date();
	  	String id  = dateFormat.format(date);
	  	id         = id.replaceAll("_", "");
		myDebugPrinting("id - " + id, testVars.logerVars.MAJOR);
		
		return id;
	  }  
	  
	  
	  /**
	  *  Generate a random IP
	  *  @return ip - a random ip sting
	  */
	  public String getRandIp() {
		  
		  Random r = new Random();
		  return (r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256));
		  
	  }
	  
}