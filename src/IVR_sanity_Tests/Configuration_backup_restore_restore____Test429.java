package IVR_sanity_Tests;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Log;

/**
* ----------------
* This test tests the Restore mechanism
* -----------------
* Tests:
*    - Press the 'Backup Now' key to create a backup
*    1. Press the 'Restore' key.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Configuration_backup_restore_restore____Test429 {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  

  // Default constructor for print the name of the used browser 
  public Configuration_backup_restore_restore____Test429(String browser) {
	  
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
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Backup_Restore() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_Backup_and_restore", "Last Backup");
  
	// Press the 'Backup Now' key and download Backup
	testFuncs.myDebugPrinting("Press the 'Backup Now' key and download Backup", testVars.logerVars.MAJOR);
	driver.switchTo().frame(1); 
	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/form/div[2]/fieldset/a/span"), 7000);
	Date date = new Date();
	String backupName = (new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(date));
	testFuncs.myDebugPrinting("backupName - "  + backupName, testVars.logerVars.MINOR);
	testFuncs.searchStr(driver, backupName);
	testFuncs.myClick(driver, By.cssSelector("a[title*='Download']"), 7000);
	
	// Restore last Backup
	testFuncs.myDebugPrinting("Restore last Backup", testVars.logerVars.MAJOR);
	String lastBackupFullPath;
	if ((lastBackupFullPath = testFuncs.returnFilesByGivenPrefix(testVars.getDownloadsPath(), backupName)) != "") {
	
		testFuncs.myDebugPrinting("lastBackupPath - " + lastBackupFullPath, testVars.logerVars.MINOR);
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/form/div[4]/fieldset/table/tbody/tr[1]/td/table/tbody/tr[6]/td/table/tbody/tr[1]/td[3]/a/span"), 5000);
		testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to restore with file " + lastBackupFullPath);
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonYes']"), 4000);
		testFuncs.searchStr(driver, " Restore is running");
		testFuncs.myWait(300000);
		testFuncs.myAssertTrue("Restore was not ended after 5 minutes !!", driver.findElement(By.tagName("body")).getText().contains("Last Backup"));
		testFuncs.searchStr(driver, "Last Backup");
		
	} else {
		
		testFuncs.myFail("Backup file name was not detecetd - ");
	}
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