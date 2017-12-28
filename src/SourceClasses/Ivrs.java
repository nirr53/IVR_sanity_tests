package SourceClasses;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class Ivrs {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public Ivrs() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }

	/**
	 * <pre>
	*  Create an IVR using given data
	*  @param driver  - A given web driver
	*  @param ivrName - A given IVR name
	*  @param ivrDesc - A given IVR description
	*  @param data    - An Hash map of (key)(value)
	*  	(endpointIsEnabled) - anything  but empty
	*  	(endpointValue)	    - option for set the IVR endpoint
	*  </pre>
	*/
	public void createIvr(WebDriver driver, String ivrName, String ivrDesc, Map<String, String> data) {
		
		// Create a new IVR  
		driver.switchTo().frame(1); 
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a[1]"), 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")       , ivrName		   , 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), ivrDesc		   , 3000);
		
		// Set IVR endpoint
		if (data.containsKey("endpointIsEnabled")) {
			
			// Set data according to value of "endpointIsEnabled"
			Select ivrEndpoint = new Select(driver.findElement(By.xpath("//*[@id='userEndpoint']")));
			switch (data.get("endpointValue")) {
			
				case "firstIdx":
					ivrEndpoint.selectByIndex(0);
					break;
				case "givenIdx":
					ivrEndpoint.selectByIndex(0);
					break; 
				case "visibleText":
					ivrEndpoint.selectByIndex(0);
					break;			
			}
		}

		// Submit and verify the create
		driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 5000);
		MenuPaths testPaths = new MenuPaths();
		testFuncs.myClick(driver, By.xpath(testPaths.aaManagementIvrs), 5000);
		driver.switchTo().frame(1);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		if (bodyText.contains(ivrName)) {
			
			  testFuncs.myDebugPrinting("Agent was detected !!", testVars.logerVars.MINOR);
		} else {
			
			testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
			bodyText     = driver.findElement(By.tagName("body")).getText();
			testFuncs.myAssertTrue("Agent was not detected !!", bodyText.contains(ivrName));
		}
	}

	/**
	*  <pre>
	*  Get a index for a row with a given Agent phone number
	*  @param driver    - A given web driver
	*  @param ivrNumber - A given IVR number
	*  </pre>
	*/
	public int getIdx(WebDriver driver, String ivrNumber) {
		  
		Boolean isFirstPage = true;
		int lastSearch 	  = 0;  
		  
		while (true) {
			  
		  // Search for endpoint in the menu records
		  String bodyText     = driver.findElement(By.tagName("body")).getText(); 
		  String[] rows = bodyText.split("\n");
		  for (int i = 0; i < rows.length; ++i) {
			  
			  testFuncs.myDebugPrinting("rows[" + i + "] - " + rows[i], testVars.logerVars.MINOR);
			  if (rows[i].contains(ivrNumber)) {
				  
				  testFuncs.myDebugPrinting(ivrNumber + " row is: " + (i - 3 + lastSearch), testVars.logerVars.MINOR);
				  return (i - 3 + lastSearch);
			  }  
		  }
		  lastSearch = rows.length - 4;
		  testFuncs.myDebugPrinting("lastSearch - " + lastSearch, testVars.logerVars.MINOR);
		  		  
		  // If Record was not found search for it in second page 
		  testFuncs.myDebugPrinting("Record was not found search for it in second page  !!", testVars.logerVars.MINOR);
		  if (!isFirstPage) {
				  
			  testFuncs.myFail("IVR Agent was not detected !! (" + ivrNumber + ")");
			  break;
		  } else {
				  
			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);  
			  isFirstPage = false;  
		  }
		  
		}
	 
		return -1;
	}
	
	/**
	*  <pre>
	*  Delete an IVR using a given parameters
	*  @param driver    - A given web driver
	*  @param ivrNumber - A given IVR number
	*  @param ivrDesc   - A given IVR description
	*  </pre>
	*/
	public void deleteIvr(WebDriver driver, String ivrNumber, String ivrDesc) {
		
		// Return to start of IVRs list
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementIvrs), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, ivrNumber);		
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[8]/a"), 3000); 
		testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete IVR entry?");
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

		// Verify delete
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		testFuncs.myAssertTrue("IVR number was still detected !! (newPhoneNumber - " + ivrNumber + ")"	 , !bodyText.contains(ivrNumber));  
		testFuncs.myAssertTrue("IVR description was still detected !! (newDisplayName - " + ivrDesc + ")", !bodyText.contains(ivrDesc));
	}
	
	/**
	*  <pre>
	*  Edit an IVR using a given parameters
	*  @param driver    - A given web driver
	*  @param oldIvrNumber - A pre-created IVR
	*  @param ivrNumber    - A new IVR name
	*  @param ivrDesc      - A new IVr description
	*  @param data         - An Hash map of (key)(value)
	*  </pre>
	*/
	public void editIvr(WebDriver driver, String ivrName, String newIvrName, String newIvrDesc, Map<String, String> data) {
		
		// Return to start of ivrs list
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementIvrs), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, ivrName);
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[7]/a"), 3000); 
		driver.findElement(By.xpath("//*[@id='name']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")       , newIvrName, 3000);
		driver.findElement(By.xpath("//*[@id='description']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), newIvrDesc, 3000);
		driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 5000);
		
		// Verify the edit
		MenuPaths testPaths = new MenuPaths();
		testFuncs.myClick(driver, By.xpath(testPaths.aaManagementIvrs), 5000);
		driver.switchTo().frame(1);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		if (bodyText.contains(newIvrName)) {
			
			  testFuncs.myDebugPrinting("IVR was detected !!", testVars.logerVars.MINOR);
		} else {
			
			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
			  bodyText     = driver.findElement(By.tagName("body")).getText();
			  if (bodyText.contains(newIvrName)) {  
				  
				  testFuncs.myDebugPrinting("IVR was detected !!", testVars.logerVars.MINOR);
			  } else {
				  
				  testFuncs.myFail("IVR was not detected !!");
			  }
		}
	}
}
