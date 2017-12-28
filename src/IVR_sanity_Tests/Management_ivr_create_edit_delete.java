package IVR_sanity_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Ivrs;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Create, edit and delete of an IVR record
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a IVR record
*    2. Edit the created IVR record
*    3. Delete the created IVR record
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_ivr_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Ivrs					testIvrs;

  // Default constructor for print the name of the used browser 
  public Management_ivr_create_edit_delete(String browser) {
	  
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
    testIvrs 	 = new Ivrs();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Ivr_create_edit_delete() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id 	    = testFuncs.GenerateId();
	String ivrName  = "myIvrname" 		 + id;
	String ivrDesc  = "myIvrDescription" + id;
	
	// Login and create an IVR

	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_Ivrs", "Add New IVR");
  
	// Create a new IVR
	testFuncs.myDebugPrinting("Step 1 - Create a new IVR", testVars.logerVars.MAJOR);
    Map<String, String> data = new HashMap<String, String>();
    data.put("endpointIsEnabled", "1");
    data.put("endpointValue"	, "firstIdx");
    testIvrs.createIvr(driver, ivrName, ivrDesc, data);
    
	// Edit an IVR record
	testFuncs.myDebugPrinting("Step 2 - Edit an IVR record", testVars.logerVars.MAJOR);
	String newIvrName = "newIvrName" 		 + id;
	String newIvrDesc = "newIve description" + id;
	testIvrs.editIvr(driver, ivrName, newIvrName, newIvrDesc, data);
	
	// Delete an IVR record
	testFuncs.myDebugPrinting("Step 3 - Delete an IVR record", testVars.logerVars.MAJOR);
	testIvrs.deleteIvr(driver, newIvrName, newIvrDesc);	
  }

  @After
  public void tearDown() throws Exception {
	  
    //driver.quit();
    System.clearProperty("webdriver.chrome.driver");
	System.clearProperty("webdriver.ie.driver");
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
    	
      testFuncs.myFail(verificationErrorString);
    }
  }
}