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
import SourceClasses.Prompts;

/**
* ----------------
* This test tests an export of Prompt records
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    - Create a Prompt record
*    1. Enter the Import / Export menu and export the current Prompts.
*    2. Check that the created Prompt exists in the exported file.
*    3. Re-enter the Prompts menu and delete the created Prompt.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_export_prompts {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Prompts				testPrompts;
  MenuPaths 			testMenuPaths;

  // Default constructor for print the name of the used browser 
  public Management_export_prompts(String browser) {
	  
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
	  	
	testVars  	    = new GlobalVars();
    testFuncs 	    = new GlobalFuncs(); 
    testPrompts     = new Prompts();
	testMenuPaths   = new  MenuPaths();  
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Export_Prompts() throws Exception {
	   
	  Log.startTestCase(this.getClass().getName());
	  String id         =  testFuncs.GenerateId();
	  String promptName = "textPrompt" 	    + id;		
	  String promptDesc = "textDescription" + id;
	  String expName 	= "ExportPrompts";
	
	  // Login and create a Prompt
	  testFuncs.myDebugPrinting("Login and create a Prompt", testVars.logerVars.MAJOR);
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);	
	  testFuncs.enterMenu(driver, "AA_prompts", "Add New Prompt");
	  testPrompts.createTextPrompt(driver, promptName, promptDesc);
	  
	  	  
	  // Enter the Import-Export menu and Export the Prompts
	  testFuncs.myDebugPrinting("Step 1 - Enter the Import-Export menu and Export the Prompts", testVars.logerVars.MAJOR);
	  driver.switchTo().defaultContent();
	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportSection), 3000); 
	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportExport) , 3000);
	  driver.switchTo().frame(1); 
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);
	  driver.findElement(By.xpath("//*[@id='PROMPT']/fieldset/table/tbody/tr[1]/td/a[1]")).click(); 
	  testFuncs.myWait(20000);
	  String exportFileName = testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), expName);
	  testFuncs.myAssertTrue("Prompts file was not downloaded successfully !", exportFileName != null);
	  driver.switchTo().defaultContent();
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);

	  // Re-enter the Prompts menu and delete the created Prompt
	  testFuncs.myDebugPrinting("Step 2 - Re-enter the Prompts menu and delete the created Prompt", testVars.logerVars.MAJOR);
	  driver.switchTo().defaultContent();
	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementAAPrompts), 3000); 
	  driver.switchTo().frame(1); 
	  testPrompts.deletePrompt(driver, promptName, promptDesc);
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