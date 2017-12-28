package IVR_sanity_Tests;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.*;

import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Holidays;
import SourceClasses.Log;
import SourceClasses.MenuPaths;

/**
* ----------------
* This test tests an export of Holiday records
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    - Create a Holiday record
*    1. Enter the Import / Export menu and export the current Holidays.
*    2. Check that the created Holiday exists in the exported file.
*    3. Re-enter the Holiday menu and delete the created Holiday.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_export_holidays {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Holidays				testHolidays;
  MenuPaths 			testMenuPaths;

  // Default constructor for print the name of the used browser 
  public Management_export_holidays(String browser) {
	  
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
    testHolidays  = new Holidays();
	testMenuPaths = new  MenuPaths();  
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Export_Holidays() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	String id 	       = testFuncs.GenerateId();
	String holidayName = "myHoliday" 			 + id;
	String holidayDesc = "myHoliday description" + id;
	String startDate   = "2017-04-23 00:00";
	String endDate     = "2017-04-24 00:00";
	String expName     = "ExportHoliday";
	
	// Login and create an Holiday
	testFuncs.myDebugPrinting("Login and create an Holiday", testVars.logerVars.MAJOR);
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_Holidays", "Add Holiday Set");
	testHolidays.createHoliday(driver, holidayName, holidayDesc, startDate, endDate, true, false);  

	// Enter the Import-Export menu and Export the current Holiday
	testFuncs.myDebugPrinting("Step 1 - Enter the Import-Export menu and Export the current Holiday", testVars.logerVars.MAJOR);
	driver.switchTo().defaultContent();
	testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportSection), 3000); 
	testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportExport) , 3000);
	driver.switchTo().frame(1); 
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);
  	driver.findElement(By.xpath("//*[@id='HOLIDAY']/fieldset/table/tbody/tr[1]/td/a[1]")).click();
	testFuncs.myWait(5000);
	String exportFileName = testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), expName);
	if (exportFileName != null) {
		
		testFuncs.myDebugPrinting("exportFileName - " +  exportFileName, testVars.logerVars.MINOR);
		String exportFullPath = testVars.getDownloadsPath() + "\\" + exportFileName;
		@SuppressWarnings("resource")
		String content = new Scanner(new File(exportFullPath)).useDelimiter("\\Z").next();
		String[] data = {holidayName, holidayDesc};
		testFuncs.checkFile(content, data);			
	} else {
		
		testFuncs.myFail("File was not downloaded successfully !!");
	}
	driver.switchTo().defaultContent();
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);

	// Re-enter the Holidays menu and delete the created Holiday
	testFuncs.myDebugPrinting("Step 2 - Re-enter the Holidays menu and delete the created Holiday", testVars.logerVars.MAJOR);
	driver.switchTo().defaultContent();
	testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementHolidays), 3000); 
	driver.switchTo().frame(1); 
	testHolidays.deleteHoliday(driver, holidayName, holidayDesc);
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