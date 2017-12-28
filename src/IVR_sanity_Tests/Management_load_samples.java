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
import SourceClasses.MenuPaths;

/**
* ----------------
* This test tests the Load Samples page
* -----------------
* Tests:
*    - Enter to the Load Samples section
*    1. Load Samples:
*    	- Load a sample of an IVR-ACD.
*    	- Load a sample of an IVR-Ambiguity.
*    	- Load a sample of an IVR-Passcode.
*    	- Load a sample of an IVR-working-days.
*    2. Check that the IVRs were created.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_load_samples {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;

  // Default constructor for print the name of the used browser 
  public Management_load_samples(String browser) {
	  
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
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Load_samples() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String acdNames[] = {"ACD_Sample", "Ambiguous_IVR", "Passcode_Access", "Company_IVR"};
	
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "Import_Load_samples", "Description");
	driver.switchTo().frame(1);
  
	// Load Samples
	testFuncs.myDebugPrinting("Step 1 - Load Samples", testVars.logerVars.MAJOR);
	for (int i = 1; i < 5; ++i) {
		
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody[1]/tr[" + i + "]/td[3]/a"), 7000);
		testFuncs.verifyStrByXpath(driver, "//*[@id='jqi_state_state0']/div[1]", "Are you sure you want to load this sample?");
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonLoad']"), 7000);
		testFuncs.verifyStrByXpath(driver, "//*[@id='jqi_state_state0']/div[1]", "Sample was loaded successfully");
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonOk']"), 7000);
	}

	
	// Check Create
	MenuPaths testMenuPaths = new  MenuPaths();    
	driver.switchTo().defaultContent();	  	  
	testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementAASection), 6000);  
	testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementIvrs), 6000);  
	driver.switchTo().frame(1);   
	String bodyText     = driver.findElement(By.tagName("body")).getText();
	if (bodyText.contains(acdNames[0])) {
  		  
		testFuncs.myDebugPrinting(acdNames[0] + " was detected !!", testVars.logerVars.MINOR);
	} else {
  		  
		testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
  		bodyText     = driver.findElement(By.tagName("body")).getText();
  		if (bodyText.contains(acdNames[0])) {  
  			  
  		 testFuncs.myDebugPrinting(acdNames[0] + " was detected !!", testVars.logerVars.MINOR);
  		} else {
  			
  			testFuncs.myFail("Queue was not detected !!");  
  		}
	}
	for (int i = 0; i < 4; ++i) {
		
		testFuncs.myAssertTrue("Sample ACD (" + acdNames[i] + ") was not detected !!", bodyText.contains(acdNames[i]));
	}	
	// TODO add Delete of IVRs
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