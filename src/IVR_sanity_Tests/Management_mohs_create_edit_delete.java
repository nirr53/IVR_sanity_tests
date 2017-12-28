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
import SourceClasses.MusicOnHold;

/**
* ----------------
* This test tests the Create, edit and delete of a MOH
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a MOH
*    2. Edit the created MOH
*    3. Delete the created MOH
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_mohs_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  MusicOnHold			testMusicOnHold;

  // Default constructor for print the name of the used browser 
  public Management_mohs_create_edit_delete(String browser) {
	  
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
    testMusicOnHold =  new MusicOnHold();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Mohs_create_edit_delete() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id 	   = testFuncs.GenerateId();
	String mohName = "myMOH" 			 + id;
	String mohDesc = "myMOH description" + id;
	String mohFile = testVars.getSrcFilesPath() + "//" + testVars.getImportFile("moh_file");
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_mohs", "Add New Music on Hold");
  
	// Create a MOH
	testFuncs.myDebugPrinting("Step 1 - Create a MOH", testVars.logerVars.MAJOR);
	testMusicOnHold.createMOH(driver, mohName, mohDesc, mohFile);
	
	// Edit a MOH
	testFuncs.myDebugPrinting("Step 2 - Edit a MOH", testVars.logerVars.MAJOR);
	String newMohDesc = "newMyMOH description" + id;
	testMusicOnHold.editMOH(driver, mohName, newMohDesc);
	
	// Delete a MOH
	testFuncs.myDebugPrinting("Step 3 - Delete a MOH", testVars.logerVars.MAJOR);
	testMusicOnHold.deleteMOH(driver, mohName, newMohDesc);	
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