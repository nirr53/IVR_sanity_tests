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
import SourceClasses.Language;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Create, edit and delete of a Language record
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a Language record
*    2. Edit the created Language record
*    3. Delete the created Language record
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_languages_create_edit_delete {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Language				testLangs;
  
  // Default constructor for print the name of the used browser 
  public Management_languages_create_edit_delete(String browser) {
	  
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
	  	
	testVars  = new GlobalVars();
    testFuncs = new GlobalFuncs(); 
    testLangs = new Language();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Language_create_edit_delete() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id 	      = testFuncs.GenerateId();
	String langName   = "myLang" + id;
	String langDesc   = "myLang" + id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_Languages", "");  
	driver.switchTo().defaultContent();	  
	testFuncs.searchStr(driver, "Ivr Languages List");	  

	// Create a Language record
	testFuncs.myDebugPrinting("Step 1 - Create a Language record", testVars.logerVars.MAJOR);
	testLangs.createLanguage(driver, langName, langDesc);
	
	// Edit a Language record
	testFuncs.myDebugPrinting("Step 2 - Edit a Language record", testVars.logerVars.MAJOR);
	String newLangDesc = "newLang description" + id;
	testLangs.editLang(driver, langName, newLangDesc);
	
	// Delete a Language record
	testFuncs.myDebugPrinting("Step 3 - Delete a Language record", testVars.logerVars.MAJOR);
	testLangs.deleteLang(driver, langName, newLangDesc);	
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