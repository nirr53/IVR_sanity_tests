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
import SourceClasses.Queues;
import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Create, edit and delete of a Queue record
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a Queue record
*    2. Edit the created Queue record
*    3. Delete the created Queue record
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_queues_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Queues				testQueues;

  // Default constructor for print the name of the used browser 
  public Management_queues_create_edit_delete(String browser) {
	  
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
    testQueues  = new Queues();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Queues_create_edit_delete() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id 	      = testFuncs.GenerateId();
	String queueName  = "myQueueName" 		 + id;
	String queueDesc  = "myQueueDescription" + id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "ACD_Queues", "Add New Queue");
  
	// Create a new Queue
	testFuncs.myDebugPrinting("Step 1 - Create a new Queue", testVars.logerVars.MAJOR);
    Map<String, String> data = new HashMap<String, String>();
	data.put("timeOutIsEnabled"	  , "1");
	data.put("timeOutPeriod"   	  , "20");
	data.put("timeOutAction"   	  , testQueues.getRandomAction(testQueues.timeoutActions));
	data.put("timeOutTarget"   	  , "+97252997");	
	data.put("overflowIsEnabled"  , "1");
	data.put("overflowCallsNumber", "2");
	data.put("overflowTarget"     , "+97252998");
	data.put("overflowForwardMode", testQueues.getRandomAction(testQueues.overflowForMode));
	data.put("overflowAction"     , testQueues.getRandomAction(testQueues.overflowActions));
	testQueues.createQueue(driver, queueName, queueDesc, data);
	
	// Edit a Queue record
	testFuncs.myDebugPrinting("Step 2 - Edit a Queue record", testVars.logerVars.MAJOR);
	String newQueueName = "newQueue" 			 + id;
	String newQueueDesc = "newQueue description" + id;
	testQueues.editQueue(driver, queueName, newQueueName, newQueueDesc);
	
	// Delete a Queue record
	testFuncs.myDebugPrinting("Step 3 - Delete a Queue record", testVars.logerVars.MAJOR);
	testQueues.deleteQueue(driver, newQueueName, newQueueDesc);	
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