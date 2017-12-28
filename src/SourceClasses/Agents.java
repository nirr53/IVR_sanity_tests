package SourceClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Agents {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public Agents() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }

    // Create an Agent using given data
	public void createAgentUsPhoneNumber(WebDriver driver, String phoneNumber, String displayName) {
		
		// Create new Business hours  
		driver.switchTo().frame(1); 
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a")   , 3000);
		testFuncs.myClick(driver, By.xpath("//*[@id='search_form']/div[1]/fieldset/a/span"), 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='telUri']")     , phoneNumber, 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='displayName']"), displayName, 3000);
		driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 5000);
		
		// Verify the create
		MenuPaths testPaths = new MenuPaths();
		testFuncs.myClick(driver, By.xpath(testPaths.acdAgents), 5000);
		driver.switchTo().frame(1);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		if (bodyText.contains(phoneNumber)) {
			
			  testFuncs.myDebugPrinting("Agent was detected !!", testVars.logerVars.MINOR);
		} else {
			
			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
			  bodyText     = driver.findElement(By.tagName("body")).getText();
			  if (bodyText.contains(phoneNumber)) {  
				  
				  testFuncs.myDebugPrinting("Agent was detected !!", testVars.logerVars.MINOR);
			  } else {
				  
				  testFuncs.myFail("Agent was not detected !!");
			  }
		}
	}
	
	// Edit an Agent
	public void editAgent(WebDriver driver, String oldPhoneNumber, String newPhoneNumber, String newDisplayName) {
		
		// Return to start of IVR-Agents list
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.acdAgents), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, oldPhoneNumber);
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[5]/a"), 3000); 
		driver.findElement(By.xpath("//*[@id='telUri']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='telUri']")     , newPhoneNumber, 3000);
		driver.findElement(By.xpath("//*[@id='displayName']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='displayName']"), newDisplayName, 3000);
		driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 5000);
		
		// Verify the edit
		MenuPaths testPaths = new MenuPaths();
		testFuncs.myClick(driver, By.xpath(testPaths.acdAgents), 5000);
		driver.switchTo().frame(1);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		if (bodyText.contains(newPhoneNumber)) {
			
			  testFuncs.myDebugPrinting("Agent was detected !!", testVars.logerVars.MINOR);
		} else {
			
			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
			  bodyText     = driver.findElement(By.tagName("body")).getText();
			  if (bodyText.contains(newPhoneNumber)) {  
				  
				  testFuncs.myDebugPrinting("Agent was detected !!", testVars.logerVars.MINOR);
			  } else {
				  
				  testFuncs.myFail("Agent was not detected !!");
			  }
		}
	}
	  
	// Get a index for a row with a given Agent phone number
	public int getIdx(WebDriver driver, String phoneNumber) {
		  
		Boolean isFirstPage = true;
		int lastSearch 	  = 0;  
		  
		while (true) {
			  
		  // Search for endpoint in the menu records
		  String bodyText     = driver.findElement(By.tagName("body")).getText(); 
		  String[] rows = bodyText.split("\n");
		  for (int i = 0; i < rows.length; ++i) {
			  
			  testFuncs.myDebugPrinting("rows[" + i + "] - " + rows[i], testVars.logerVars.MINOR);
			  if (rows[i].contains(phoneNumber)) {
				  
				  testFuncs.myDebugPrinting(phoneNumber + " row is: " + (i - 2 + lastSearch), testVars.logerVars.MINOR);
				  return (i - 2 + lastSearch);
			  }  
		  }
		  lastSearch = rows.length - 3;
		  testFuncs.myDebugPrinting("lastSearch - " + lastSearch, testVars.logerVars.MINOR);
		  		  
		  // If Record was not found search for it in second page 
		  testFuncs.myDebugPrinting("Record was not found search for it in second page  !!", testVars.logerVars.MINOR);
		  if (!isFirstPage) {
				  
			  testFuncs.myFail("IVR Agent was not detected !! (" + phoneNumber + ")");
			  break;
		  } else {
				  
			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);  
			  isFirstPage = false;  
		  }
		  
		}
	 
		return -1;
	}
	
	// Delete an Agent using a given parameters
	public void deleteAgent(WebDriver driver, String newPhoneNumber, String newDisplayName) {
		
		// Return to start of IVR-Agents list
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.acdAgents), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, newPhoneNumber);
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[6]/a"), 3000); 
		testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete " + newDisplayName + " entry?");
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

		// Verify delete
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		testFuncs.myAssertTrue("Agent name was still detected !! (newPhoneNumber - " + newPhoneNumber + ")"		   , !bodyText.contains(newPhoneNumber));  
		testFuncs.myAssertTrue("Agent diaplay-name was still detected !! (newDisplayName - " + newDisplayName + ")", !bodyText.contains(newDisplayName));
	}
}
