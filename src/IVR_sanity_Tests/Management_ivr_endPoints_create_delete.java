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
import SourceClasses.IvrEndPoints;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Create and delete of an IVR-end point record
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create an IVR-endPoint record
*    3. Delete the created IVR-endPoint record
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_ivr_endPoints_create_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  IvrEndPoints			testIvrPoints;

  // Default constructor for print the name of the used browser 
  public Management_ivr_endPoints_create_delete(String browser) {
	  
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
	  	
	testVars  	  = new GlobalVars();
    testFuncs 	  = new GlobalFuncs(); 
    testIvrPoints = new IvrEndPoints();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void IVR_endPoints_create_delete() throws Exception {
	  
	String ivrNumber = "52990";
	Log.startTestCase(this.getClass().getName());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_IVRendPoints", "Add New IVR Endpoint");
  
	// Create an IVR endPoint
	testFuncs.myDebugPrinting("Step 1 - Create an IVR endPoint", testVars.logerVars.MAJOR);
	testIvrPoints.createIVREndPoint(driver, ivrNumber);
	
	// Delete an IVR endPoint
	testFuncs.myDebugPrinting("Step 3 - Delete an IVR endPoint", testVars.logerVars.MAJOR); 
	testIvrPoints.deleteIVREndPoint(driver, ivrNumber);	
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