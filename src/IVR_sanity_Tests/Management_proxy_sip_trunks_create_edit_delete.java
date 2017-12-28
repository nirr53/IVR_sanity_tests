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
import SourceClasses.Log;
import SourceClasses.ProxySipTrunks;

/**
* ----------------
* This test tests the Create, edit and delete of a Proxy SIP trunks
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a Proxy SIP trunks
*    2. Edit the created Proxy SIP trunks
*    3. Delete the created Proxy SIP trunks
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_proxy_sip_trunks_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  ProxySipTrunks		testProxyTrunks;

  // Default constructor for print the name of the used browser 
  public Management_proxy_sip_trunks_create_edit_delete(String browser) {
	  
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
	  	
	testVars  		= new GlobalVars();
    testFuncs 		= new GlobalFuncs(); 
    testProxyTrunks = new ProxySipTrunks();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Proxy_sip_trunks_create_edit_delete() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id 	      = testFuncs.GenerateId();
	String proName 	  = "myPro" 			 + id;
	String proDesc    = "myPro description" + id;
	String proAddress = testFuncs.getRandIp();
	String proPort    = "9999";
	String proUri     = "";
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "SiInter_Proxy_sip_trunks", "Add New Proxy SIP Trunk");
	
	// Create a Proxy SIP trunk
	testFuncs.myDebugPrinting("Step 1 - Create a Proxy SIP trunk", testVars.logerVars.MAJOR);
	testProxyTrunks.createProxySipTrunk(driver, proName, proDesc, proAddress, proPort, proUri);
	
	// Edit a Proxy SIP trunk
	testFuncs.myDebugPrinting("Step 2 - Edit a Proxy SIP trunk", testVars.logerVars.MAJOR);
	String newProName    = "newProSipTrunk" 	+ id;
	String newProDesc    = "nwePro description" + id;
	String newProAddress = testFuncs.getRandIp();
	String newProPort    = "7777";
	String newPortUri    = "";
	testProxyTrunks.editProxySipTrunk(driver, proName, newProName, newProDesc, newProAddress, newProPort, newPortUri);
	
	// Delete a Proxy SIP trunk
	testFuncs.myDebugPrinting("Step 3 - Delete a Proxy SIP trunk", testVars.logerVars.MAJOR);
	testProxyTrunks.deleteProxySipTrunk(driver, newProName, newProDesc);	
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