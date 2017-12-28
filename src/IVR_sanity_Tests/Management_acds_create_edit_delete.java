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
import SourceClasses.AcdFlows;
import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Create, edit and delete of an ACD record
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a ACD record
*    2. Edit the created ACD record
*    3. Delete the created ACD record
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_acds_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  AcdFlows				testAcdFlows;

  // Default constructor for print the name of the used browser 
  public Management_acds_create_edit_delete(String browser) {
	  
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
    testAcdFlows = new AcdFlows();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Acds_create_edit_delete() throws Exception {
	  
	  
	  
	testFuncs.myFail("Test still not ready !!");
	Log.startTestCase(this.getClass().getName());
	String id 	    = testFuncs.GenerateId();
//	String acdName  = "myACDName" 		 + id;
//	String acdDesc  = "myACDDescription" + id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "ACD_Acds", "Add New ACD Flow");
  

	// Create a new ACD
	testFuncs.myDebugPrinting("Step 1 - Create a new ACD", testVars.logerVars.MAJOR);
    Map<String, String> data = new HashMap<String, String>();
    
	driver.switchTo().frame(1); 
	//testFuncs.myClick(driver, By.xpath()   , 7000);
	
	driver.findElement(By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a")).click();
	testFuncs.myWait(2000);
	//driver.switchTo().defaultContent();
	testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
	driver.switchTo().frame(1); 
    
    
	
	
    //testAcdFlows.createAcdFlow(driver, acdName, acdDesc, data);
//	
//	// Edit a Queue record
//	testFuncs.myDebugPrinting("Step 2 - Edit a Queue record", testVars.logerVars.MAJOR);
//	String newQueueName = "newQueue" 			 + id;
//	String newQueueDesc = "newQueue description" + id;
//	testQueues.editQueue(driver, queueName, newQueueName, newQueueDesc);
//	
//	// Delete a Queue record
//	testFuncs.myDebugPrinting("Step 3 - Delete a Queue record", testVars.logerVars.MAJOR);
//	testQueues.deleteQueue(driver, newQueueName, newQueueDesc);	
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