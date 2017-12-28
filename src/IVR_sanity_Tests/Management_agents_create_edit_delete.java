package IVR_sanity_Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;
import SourceClasses.Agents;
import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Create, edit and delete of an Agent record
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a new Agent
*    2. Edit the created Agent
*    3. Delete the created Agent
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_agents_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Agents				testAgents;
  
  // Default constructor for print the name of the used browser 
  public Management_agents_create_edit_delete(String browser) {
	  
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
    testAgents 	 = new Agents();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Agents_create_edit_delete() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id = testFuncs.GenerateId();
	String phoneNumber = id;
	String displayName = "Display name" + id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "ACD_Agents", "Add New Agent");
  
	// Create an Agent based on Phone Number
	testFuncs.myDebugPrinting("Step 1 - Create an Agent", testVars.logerVars.MAJOR);
	testAgents.createAgentUsPhoneNumber(driver, phoneNumber, displayName);

	// Edit an Agent
	testFuncs.myDebugPrinting("Step 2 - Edit an Agent", testVars.logerVars.MAJOR);
	id = testFuncs.GenerateId();
	String newPhoneNumber = id;
	String newDisplayName = "New Display name" + id;
	testAgents.editAgent(driver, phoneNumber, newPhoneNumber, newDisplayName);
	
	// Delete an Agent
	testFuncs.myDebugPrinting("Step 3 - Delete an Agent", testVars.logerVars.MAJOR);
	testAgents.deleteAgent(driver, newPhoneNumber, newDisplayName);	
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