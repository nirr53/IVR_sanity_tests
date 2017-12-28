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
import SourceClasses.Prompts;

/**
* ----------------
* This test tests the Create-prompt using upload-file mechanism
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    1. Create a prompt using upload-file
*    2. Delete the created prompt
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_prompts_create_prompt_based_file {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Prompts				testPrompts;
  
  // Default constructor for print the name of the used browser 
  public Management_prompts_create_prompt_based_file(String browser) {
	  
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
	  	
	testVars    = new GlobalVars();
    testFuncs   = new GlobalFuncs(); 
    testPrompts = new Prompts();
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Create_prompt_based_file() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id = testFuncs.GenerateId();
	String promptName = "textPrompt" 	  + id;
	String promptDesc = "textDescription" + id;
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_prompts", "Add New Prompt");
  
	// Create a prompt using file
	testFuncs.myDebugPrinting("Create a prompt using file", testVars.logerVars.MAJOR);
	testPrompts.createFilePrompt(driver, promptName, promptDesc);
	
	// Delete the created prompt
	testFuncs.myDebugPrinting("Delete the created prompt", testVars.logerVars.MAJOR);
	testPrompts.deletePrompt(driver, promptName, promptDesc); }

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