package IVR_sanity_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Holidays;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Create, edit and delete of a Holiday record
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create an Holiday record
*    2. Edit the created Holiday record
*    3. Delete the created Holiday record
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_holidays_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Holidays				testHolidays;
  
  // Default constructor for print the name of the used browser 
  public Management_holidays_create_edit_delete(String browser) {
	  
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
	  	
	testVars  	 = new GlobalVars();
    testFuncs 	 = new GlobalFuncs();
    testHolidays = new Holidays();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Holidays_create_edit_delete() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id 	       = testFuncs.GenerateId();
	String holidayName = "myHoliday" 			 + id;
	String holidayDesc = "myHoliday description" + id;
	String startDate   = "2017-04-23 00:00";
	String endDate     = "2017-04-24 00:00";
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_Holidays", "Add Holiday Set");
  
	// Create an Holiday
	testFuncs.myDebugPrinting("Step 1 - Create an Holiday", testVars.logerVars.MAJOR);
	testHolidays.createHoliday(driver, holidayName, holidayDesc, startDate, endDate, true, false);
	
	// Edit an Holiday
	testFuncs.myDebugPrinting("Step 2 - Edit an Holiday", testVars.logerVars.MAJOR);
	String newHoName = "new" + holidayName;
	String newHoDesc = "new" + holidayDesc;
	testHolidays.editHoliday(driver, holidayName, newHoName, newHoDesc);
	
	// Delete an Holiday
	testFuncs.myDebugPrinting("Step 3 - // Delete an Holiday", testVars.logerVars.MAJOR);
	testHolidays.deleteHoliday(driver, newHoName, newHoDesc);	
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