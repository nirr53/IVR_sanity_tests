package IVR_sanity_Tests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
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
* This test tests the automatic backup mechanism
* -----------------
* Tests:
*    1. Insert specific time in the 'Backup hours' timing text box. and save it
*    2. Verify that backup is created
* 
* @author Nir Klieman
* @version 1.00
*/

@RunWith(Parameterized.class)
public class Configuration_backup_restore_auto_backup____Test428 {
	
  private WebDriver 	driver;
  private StringBuffer  verificationErrors = new StringBuffer();
  private String        usedBrowser = "";
  GlobalVars 			testVars;
  GlobalFuncs			testFuncs;
  

  // Default constructor for print the name of the used browser 
  public Configuration_backup_restore_auto_backup____Test428(String browser) {
	  
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
  public void Automatic_backup() throws Exception {
	  
	Log.startTestCase(this.getClass().getName());
	testFuncs.login(driver, testVars.getSysUsername(), testVars.getSysPassword(), testVars.getSysMainStr(), "http://", this.usedBrowser);  
	testFuncs.enterMenu(driver, "AA_Backup_and_restore", "Last Backup");
	driver.switchTo().frame(1); 

	// Set futre time on 1 minute
	DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    Calendar cal = Calendar.getInstance(); 
	testFuncs.myDebugPrinting("Cuurent time - " + dateFormat.format(cal.getTime()), testVars.logerVars.MINOR);
    cal.add(Calendar.MINUTE, 1);
	testFuncs.myDebugPrinting("Backup time - " + dateFormat.format(cal.getTime()), testVars.logerVars.MINOR);
    String time = dateFormat.format(cal.getTime());
	
	// Insert Backup-value
	testFuncs.myDebugPrinting("Insert Backup-value", testVars.logerVars.MAJOR);
	List<WebElement> el = driver.findElements(By.cssSelector("*"));
	for ( WebElement e : el ) {
		
		String tempAttName = e.getAttribute("placeholder");
		if (tempAttName != null && tempAttName.equals("Click to choose a time")) {
			
		  e.clear();
		  e.sendKeys(time);
		  testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/form/div[2]/fieldset/table[2]/tbody/tr[1]/td[3]/a/span"), 2000);
		  testFuncs.verifyStrByXpathContains(driver, "//*[@id='promt_div_id']", "Server updated successfully.");
		  testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonOk']"), 2000);
		  testFuncs.myWait(127000);
		  driver.navigate().refresh();
		  driver.switchTo().defaultContent(); 
		  testFuncs.enterMenu(driver, "AA_Backup_and_restore", "Last Backup");
		  driver.switchTo().frame(1); 
		  testFuncs.searchStr(driver, time.replaceAll(":", "-"));
		  break;
		}
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