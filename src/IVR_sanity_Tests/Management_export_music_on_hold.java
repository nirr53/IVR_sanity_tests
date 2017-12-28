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
import SourceClasses.MusicOnHold;

/**
* ----------------
* This test tests an export of Music On Hold records
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    - Create a Music On Hold record
*    1. Enter the Import / Export menu and export the current Music On Holds.
*    2. Check that the created Music On Hold exists in the exported file.
*    3. Re-enter the Music On Hold menu and delete the created Music On Hold.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_export_music_on_hold {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  MusicOnHold			testMusicOnHold;
  MenuPaths 			testMenuPaths;

  // Default constructor for print the name of the used browser 
  public Management_export_music_on_hold(String browser) {
	  
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
    testMusicOnHold = new MusicOnHold();
	testMenuPaths   = new  MenuPaths();  
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Export_Music_on_hold() throws Exception {
	   
	  Log.startTestCase(this.getClass().getName());
	  String id 	   = testFuncs.GenerateId();
	  String mohName = "myMOH" 			   + id;
	  String mohDesc = "myMOH description" + id;
	  String expName = "ExportMohs";	
	
	  // Login and create a Music On Hold
	  testFuncs.myDebugPrinting("Login and create a Music On Hold", testVars.logerVars.MAJOR);
	  String mohFile = testVars.getSrcFilesPath() + "//" + testVars.getImportFile("moh_file");
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, "AA_mohs", "Add New Music on Hold");
	  testMusicOnHold.createMOH(driver, mohName, mohDesc, mohFile);
	  
	  // Enter the Import-Export menu and Export the Music On Holds
	
	  testFuncs.myDebugPrinting("Step 1 - Enter the Import-Export menu and Export the Music On Holds", testVars.logerVars.MAJOR);
	  driver.switchTo().defaultContent();
	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportSection), 3000); 
	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportExport) , 3000);
	  driver.switchTo().frame(1); 
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);
	  driver.findElement(By.xpath("//*[@id='MOH']/fieldset/table/tbody/tr[1]/td/a[1]")).click(); 
	  testFuncs.myWait(30000);
	  String exportFileName = testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), expName);
	  testFuncs.myAssertTrue("Moh Files file was not downloaded successfully !", exportFileName != null);
	  driver.switchTo().defaultContent();
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);

	  // Re-enter the Music-On-Hold menu and delete the created Music On Hold
	  testFuncs.myDebugPrinting("Step 2 - Re-enter the Music-On-Hold menu and delete the created Music On Hold", testVars.logerVars.MAJOR);
	  driver.switchTo().defaultContent();
	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementAAMohs), 3000); 
	  driver.switchTo().frame(1); 
	  testMusicOnHold.deleteMOH(driver, mohName, mohDesc);	
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