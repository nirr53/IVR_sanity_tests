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

import SourceClasses.BusinessHour;
import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Log;
import SourceClasses.MenuPaths;

/**
* ----------------
* This test tests an export of Business-Hour records
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    - Create a Business-Hour record
*    1. Enter the Import / Export menu and export the current Business-Hours.
*    2. Check that the created Business-Hour exists in the exported file.
*    3. Re-enter the Business-Hours menu and delete the created Business-Hour record.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_export_business_hours {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  BusinessHour			testBuHours;
  MenuPaths 			testMenuPaths;

  // Default constructor for print the name of the used browser 
  public Management_export_business_hours(String browser) {
	  
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
    testBuHours   = new BusinessHour();
	testMenuPaths = new  MenuPaths();  
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Export_Business_hours() throws Exception {
	    
	  Log.startTestCase(this.getClass().getName());
	  String id 	       = testFuncs.GenerateId();
	  String busHouName    = "myBusHours" 			  + id;
	  String busHouDesc    = "myBusHours description" + id;
	  String isCheckDays[] = {"//*[@id='mon']", "//*[@id='tue']",
								"//*[@id='wed']", "//*[@id='thu']",
								"//*[@id='fri']", "//*[@id='sat']",
								"//*[@id='sun']"};		
	  String startXpath[]  = {"day00", "day01", "day02", "day03", "day04", "day05", "day06"};	
	  String endXpath[]    = {"day10", "day11", "day12", "day13", "day14", "day15", "day16"};
	  String expName       = "ExportBusinessHours";
			
	  // Login and create a Business Hours record
	  testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	  testFuncs.enterMenu(driver, "AA_Buhs", "Add New Business Hours");	
	  testFuncs.myDebugPrinting("Step 1 - Create a Business Hours record", testVars.logerVars.MAJOR);	
	  testBuHours.createBusHours(driver, busHouName, busHouDesc, isCheckDays, startXpath, endXpath); 

	  // Enter the Import-Export menu and Export the current Business Hours records
	  testFuncs.myDebugPrinting("Step 1 - Enter the Import-Export menu and Export the current Business Hours records", testVars.logerVars.MAJOR);
	  driver.switchTo().defaultContent();
	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportSection), 3000); 
	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportExport) , 3000);
	  driver.switchTo().frame(1); 
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);
	  driver.findElement(By.xpath("//*[@id='BUSINESSHOUR']/fieldset/table/tbody/tr[1]/td/a[1]")).click();
	  testFuncs.myWait(5000);
	  String exportFileName = testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), expName);
	  if (exportFileName != null) {
		
		testFuncs.myDebugPrinting("exportFileName - " +  exportFileName, testVars.logerVars.MINOR);
		String exportFullPath = testVars.getDownloadsPath() + "\\" + exportFileName;
		@SuppressWarnings("resource")
		String content = new Scanner(new File(exportFullPath)).useDelimiter("\\Z").next();
		String[] data = {busHouName, busHouDesc};
		testFuncs.checkFile(content, data);			
	  } else {
				
		  testFuncs.myFail("File was not downloaded successfully !!");
	  }
	  driver.switchTo().defaultContent();
	  testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);

	
	  // Re-enter the Business Hour menu and delete the created Business Hour
	
	  testFuncs.myDebugPrinting("Step 2 - Re-enter the Business Hour menu and delete the created Business Hour", testVars.logerVars.MAJOR);
	  driver.switchTo().defaultContent();
	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementBusiHours), 3000); 
	  driver.switchTo().frame(1); 
	  testBuHours.deleteBusHours(driver, busHouName, busHouDesc);
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