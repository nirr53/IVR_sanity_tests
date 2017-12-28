package SourceClasses;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class Groups {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;
	String[] 		alertTimes 	   = {"10", "15", "20", "25", "30"}; 
	String[]		routingMethods = {"serial", "parallel" ,"roundRobin", "longestIdle"};
	Random rand;

    // Default constructor
    public Groups() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
        rand  	  = new Random();
    }
    
    // Get random Alert-time
	public String getRandAlertTime() {
		
		int  n = rand.nextInt(alertTimes.length - 1);
		testFuncs.myDebugPrinting("n - " + n						, testVars.logerVars.MINOR);
		testFuncs.myDebugPrinting("alertTimes[n] - " + alertTimes[n], testVars.logerVars.MINOR);
		return alertTimes[n];
	}
	
	// Get random Routing-Method
	public String getRandRoutingMethod() {
		
		int  n = rand.nextInt(alertTimes.length -1);
		testFuncs.myDebugPrinting("n - " + n								, testVars.logerVars.MINOR);
		testFuncs.myDebugPrinting("routingMethods[n] - " + routingMethods[n], testVars.logerVars.MINOR);
		return routingMethods[n];
	}
	
	// Create a Group with a given parameters withoud add Agents
	public void createGroup(WebDriver driver, String groupName, String groupDescription, String alertTime, String routingMethod) {
		
		// Fill data at Group
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a"), 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")	   , groupName		 , 1000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), groupDescription, 1000);
		driver.findElement(By.xpath("//*[@id='alertTime']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='alertTime']")  , alertTime		 , 1000);
		Select rMethod = new Select(driver.findElement(By.xpath("//*[@id='routingMethod']")));
		rMethod.selectByValue(routingMethod);
		driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
	  	  
		// Verify create
		driver.switchTo().frame(1); 
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		if (bodyText.contains(groupName)) {
	  		  
			testFuncs.myDebugPrinting("Group was detected !!", testVars.logerVars.MINOR);
		} else {
	  		  
			testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
	  		bodyText     = driver.findElement(By.tagName("body")).getText();
	  		if (bodyText.contains(groupName)) {  
	  			  
	  		 testFuncs.myDebugPrinting("Group was detected !!", testVars.logerVars.MINOR);
	  		} else {
	  			
	  			testFuncs.myFail("Group was not detected !!");  
	  		}
		}
		testFuncs.searchStr(driver, groupDescription);	
	}
	
	// Edit a given group details
	public void editGroup(WebDriver driver, String groupName, String newGroupName, String newDisplayName, String newAlertTime, String newRoutingMethod) {
		
		// Return to start of IVR-Agents list and find the created Group
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.acdGroups), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, groupName);
		
		// Edit the Group
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[4]/a"), 3000);
		driver.findElement(By.xpath("//*[@id='name']")).clear();
	  	testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")		 , newGroupName  , 1000);
	  	driver.findElement(By.xpath("//*[@id='description']")).clear();
	  	testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']")  , newDisplayName, 1000);
		driver.findElement(By.xpath("//*[@id='alertTime']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='alertTime']")    , newAlertTime	 , 1000);
		Select rMethod = new Select(driver.findElement(By.xpath("//*[@id='routingMethod']")));
		rMethod.selectByValue(newRoutingMethod);
	  	driver.switchTo().defaultContent();
	  	testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000); 
		driver.switchTo().frame(1);   
		
		// Verify the edit
	  	testFuncs.searchStr(driver, newGroupName);
	  	testFuncs.searchStr(driver, newDisplayName);
	}
	
	// Get a index for a row with a given Group name
	public int getIdx(WebDriver driver, String groupName) {
		  
		Boolean isFirstPage = true;
		int lastSearch 	  = 0;  
		  
		while (true) {
			  
		  // Search for Group in the menu records
		  String bodyText     = driver.findElement(By.tagName("body")).getText(); 
		  String[] rows = bodyText.split("\n");
		  for (int i = 0; i < rows.length; ++i) {
			  
			  testFuncs.myDebugPrinting("rows[" + i + "] - " + rows[i], testVars.logerVars.MINOR);
			  if (rows[i].contains(groupName)) {
				  
				  testFuncs.myDebugPrinting(groupName + " row is: " + (i - 2 + lastSearch), testVars.logerVars.MINOR);
				  return (i - 2 + lastSearch);
			  }  
		  }
		  lastSearch = rows.length - 3;
		  testFuncs.myDebugPrinting("lastSearch - " + lastSearch, testVars.logerVars.MINOR);
		  		  
		  // If Record was not found search for it in second page 
		  testFuncs.myDebugPrinting("Record was not found search for it in second page  !!", testVars.logerVars.MINOR);
		  if (!isFirstPage) {
				  
			  testFuncs.myFail("Group was not detected !! (" + groupName + ")");
			  break;
		  } else {
				  
			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);  
			  isFirstPage = false;  
		  }
		  
		}
	 
		return -1;
	}

	public void deleteGroup(WebDriver driver, String newGroupName, String newDisplayName) {
			
		// Return to start of Groups list
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.acdGroups), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, newGroupName);
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[5]/a"), 3000); 
		testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete group entry?");
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

		// Verify delete
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		testFuncs.myAssertTrue("Group name was still detected !! (newGroupName - " + newGroupName + ")"		   , !bodyText.contains(newGroupName));  
		testFuncs.myAssertTrue("Group diaplay-name was still detected !! (newDisplayName - " + newDisplayName + ")", !bodyText.contains(newDisplayName));	
	}
}
