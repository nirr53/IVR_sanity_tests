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
* This test tests the export-all and Create-all Templates buttons at Import-export menu
* -----------------
* Tests:
*    - Enter the Import-export section
*    1. Press the Export-all button and verify that zip-file with all data is downloaded.
*    2. Press the Create-all Templates button and verify that zip-file with all data is downloaded.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_import_export_export_all_create_all_templates {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  MenuPaths 			testMenuPaths;

  // Default constructor for print the name of the used browser 
  public Management_import_export_export_all_create_all_templates(String browser) {
	  
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
	  	
	testVars  	  = new GlobalVars();
    testFuncs 	  = new GlobalFuncs(); 
	testMenuPaths = new  MenuPaths();  
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void ImportExport_export_create_all() throws Exception {
	    
	  Log.startTestCase(this.getClass().getName());
			
	  // Login and enter the Import-Export menu
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, "AA_Buhs", "Add New Business Hours");	
	  testFuncs.myDebugPrinting("Login and enter the Import-Export menu", testVars.logerVars.MAJOR);	
	  testFuncs.enterMenu(driver, "Import_ImportExport", "Export All");
	  driver.switchTo().frame(1);
	  
	  // Check Export-All button
	  testFuncs.myDebugPrinting("Step 1 - Check Export-All button", testVars.logerVars.MAJOR);
	  exportAll(driver, "//*[@id='trunkTBL']/div[1]/table/tbody/tr/td[1]/a", "exportAll");
	  
	  // Check the Create-All Templates button
	  testFuncs.myDebugPrinting("Step 2 - Check the Create-All Templates button", testVars.logerVars.MAJOR);
	  exportAll(driver, "//*[@id='trunkTBL']/div[1]/table/tbody/tr/td[2]/a", "AllTemplates");
  }
  
  // Press on the button, check that file is dowenloaded and then delete it
  private void exportAll(WebDriver driver, String buttonXpath, String prefix) {
	  
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), prefix);
	  driver.findElement(By.xpath(buttonXpath)).click();
	  testFuncs.myWait(60000);
	  String exportFileName = testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), prefix);
	  testFuncs.myAssertTrue("File was not downloaded successfully !!\nprefix - " + prefix, exportFileName != null);
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), prefix);
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