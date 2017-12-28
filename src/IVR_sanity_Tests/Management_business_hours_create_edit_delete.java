package IVR_sanity_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

import SourceClasses.BusinessHour;
import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Create, edit and delete of a Businees-hours record
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a Businees-hours record
*    2. Edit the created Businees-hours record
*    3. Delete the created Businees-hours record
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_business_hours_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  BusinessHour			testBuHours;

  // Default constructor for print the name of the used browser 
  public Management_business_hours_create_edit_delete(String browser) {
	  
	  Log.info("Browser - "  + browser);
	  this.usedBrowser = browser;
  }
  
  //Define each browser as a parameter
  @SuppressWarnings("rawtypes")
  @Parameters(name="{0}")
  public static Collection data() {
	  
	GlobalVars testVars2  = new GlobalVars();
    return Arrays.asList(testVars2.getBrowsers());
  }
  
  @BeforeClass 
  public static void setting_SystemProperties() {
	  
      System.out.println("System Properties seting Key value.");
  }  
  
  @Before
  public void setUp() throws Exception {
	  	
	testVars  	= new GlobalVars();
    testFuncs 	= new GlobalFuncs();
    testBuHours = new BusinessHour();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Business_hours_create_edit_delete() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id 	      = testFuncs.GenerateId();
	String busHouName = "myBusHours" 			 + id;
	String busHouDesc = "myBusHours description" + id;
	String isCheckDays[] = {"//*[@id='mon']", "//*[@id='tue']",
							"//*[@id='wed']", "//*[@id='thu']",
							"//*[@id='fri']", "//*[@id='sat']",
							"//*[@id='sun']"};
	String startXpath[] = {"day00", "day01", "day02", "day03", "day04", "day05", "day06"};
	String endXpath[] 	= {"day10", "day11", "day12", "day13", "day14", "day15", "day16"};
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_Buhs", "Add New Business Hours");
  
	// Create a Business Hours record
	testFuncs.myDebugPrinting("Step 1 - Create a Business Hours record", testVars.logerVars.MAJOR);
	testBuHours.createBusHours(driver, busHouName, busHouDesc, isCheckDays, startXpath, endXpath);
	
	// Edit a Business Hours record
	testFuncs.myDebugPrinting("Step 2 - Edit a Business Hours record", testVars.logerVars.MAJOR);
	String newMohName = "newBHours" + id;
	String newMohDesc = "newBHours description" + id;
	testBuHours.ediBusHours(driver, busHouName, newMohName, newMohDesc);
	
	// Delete a Business Hours record
	testFuncs.myDebugPrinting("Step 3 - Delete a Business Hours record", testVars.logerVars.MAJOR);
	testBuHours.deleteBusHours(driver, newMohName, newMohDesc);	
  }

  @After
  public void tearDown() throws Exception {
	  
    driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}