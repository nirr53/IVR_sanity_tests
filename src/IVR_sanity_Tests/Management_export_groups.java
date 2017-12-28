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
import SourceClasses.Groups;
import SourceClasses.GlobalFuncs;
import SourceClasses.GlobalVars;
import SourceClasses.Log;
import SourceClasses.MenuPaths;

/**
* ----------------
* This test tests an export of Group records
* -----------------
* Tests:
*    - Enter the Auto-Attendant section
*    - Create a Group record
*    1. Enter the Import / Export menu and export the current Groups.
*    2. Check that the created Group exists in the exported file.
*    3. Re-enter the Groups menu and delete the created Group.
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Management_export_groups {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  Groups				testGroups;
  MenuPaths 			testMenuPaths;

  // Default constructor for print the name of the used browser 
  public Management_export_groups(String browser) {
	  
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
    testGroups   	= new Groups();
	testMenuPaths   = new MenuPaths();  
    System.setProperty("webdriver.chrome.driver", testVars.getchromeDrvPath());
	System.setProperty("webdriver.ie.driver"    , testVars.getIeDrvPath());
	testFuncs.myDebugPrinting("Enter setUp(); usedbrowser - " + this.usedBrowser);
	driver = testFuncs.defineUsedBrowser(this.usedBrowser);
    //driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void Export_Groups() throws Exception {
	   
	Log.startTestCase(this.getClass().getName());
	String id = testFuncs.GenerateId();
	String groupName 		= "myGroup" 			+ id;
	String groupDescription = "myGroup description" + id;
	String alertTime		=  testGroups.getRandAlertTime();
	String routingMethod	=  testGroups.getRandRoutingMethod();
	String expName			= "ExportGroups";
	
	// Login and create a Group
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "ACD_Groups", "Add New Group");
	driver.switchTo().frame(1);
	testGroups.createGroup(driver, groupName, groupDescription, alertTime, routingMethod);
	
	// Enter the Import-Export menu and Export the Groups
	  
	testFuncs.myDebugPrinting("Step 1 - Enter the Import-Export menu and Export the Groups", testVars.logerVars.MAJOR);
	driver.switchTo().defaultContent();
	testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportSection), 3000); 
	testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementImportExport) , 3000);
	driver.switchTo().frame(1);   
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);  
	driver.findElement(By.xpath("//*[@id='GROUP']/fieldset/table/tbody/tr[1]/td/a[1]")).click(); 
	testFuncs.myWait(5000);
	String exportFileName = testFuncs.findFilesByGivenPrefix(testVars.getDownloadsPath(), expName);  
	if (exportFileName != null) {
							  
		testFuncs.myDebugPrinting("exportFileName - " +  exportFileName, testVars.logerVars.MINOR);			  
		String exportFullPath = testVars.getDownloadsPath() + "\\" + exportFileName;			  
		@SuppressWarnings("resource")  
		String content = new Scanner(new File(exportFullPath)).useDelimiter("\\Z").next();			  
		String[] data = {groupName, groupDescription};				 
		testFuncs.checkFile(content, data);			 
	} else {
		testFuncs.myFail("File was not downloaded successfully !!");	
	  
	}
	driver.switchTo().defaultContent();	  
	testFuncs.deleteFilesByPrefix(testVars.getDownloadsPath(), expName);
	  
	// Re-enter the Groups menu and delete the created Group  
	testFuncs.myDebugPrinting("Step 2 - Re-enter the Groups menu and delete the created Group", testVars.logerVars.MAJOR);	
	driver.switchTo().defaultContent();		
	testFuncs.myClick(driver, By.xpath(testMenuPaths.acdGroups), 3000);  
	driver.switchTo().frame(1); 
	testGroups.deleteGroup(driver, groupName, groupDescription);	
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