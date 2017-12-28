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
import SourceClasses.Groups;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Create, edit and delete of an Group record
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a new Group
*    2. Edit the created Group
*    3. Delete the created Group
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_groups_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Groups				testGroups;
  
  // Default constructor for print the name of the used browser 
  public Management_groups_create_edit_delete(String browser) {
	  
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
    testGroups 	 = new Groups();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Groups_create_edit_delete() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id = testFuncs.GenerateId();
	String groupName 		= "myGroup" 			+ id;
	String groupDescription = "myGroup description" + id;
	String alertTime		=  testGroups.getRandAlertTime();
	String routingMethod	=  testGroups.getRandRoutingMethod();
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "ACD_Groups", "Add New Group");
	driver.switchTo().frame(1);
  
	// Create a Group
	testFuncs.myDebugPrinting("Step 1 - Create a Group", testVars.logerVars.MAJOR);
	testGroups.createGroup(driver, groupName, groupDescription, alertTime, routingMethod);

	// Edit a Group
	testFuncs.myDebugPrinting("Step 2 - Edit a Group", testVars.logerVars.MAJOR);
	id = testFuncs.GenerateId();
	String newGroupName     = "newGroupName" +id;
	String newDisplayName   = "newDisplayName" + id;
	String newAlertTime		=  testGroups.getRandAlertTime();
	String newRoutingMethod	=  testGroups.getRandRoutingMethod();
	testGroups.editGroup(driver, groupName, newGroupName, newDisplayName, newAlertTime, newRoutingMethod);
	
	// Delete a Group
	testFuncs.myDebugPrinting("Step 3 - Delete a Group", testVars.logerVars.MAJOR);
	testGroups.deleteGroup(driver, newGroupName, newDisplayName);	
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