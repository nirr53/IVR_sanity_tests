package SourceClasses;

import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class Queues {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;
	public String[]	timeoutActions  = {"Disconnect" ,"VoiceMail", "TelNumber", "SipAddress", "OtherQueue"};
	public String[]	overflowActions = {"Disconnect" ,"VoiceMail", "TelNumber", "SipAddress", "OtherQueue"};
	public String[]	overflowForMode = {"NewCall"    , "OldCall"};
	Random rand;

    // Default constructor
    public Queues() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
        rand  	  = new Random();
    }
    
    // Get random Alert-time
	public String getRandomAction(String[] array) {
		
		int  n = rand.nextInt(array.length - 1);
		testFuncs.myDebugPrinting("n - " + n	   		  , testVars.logerVars.MINOR);
		testFuncs.myDebugPrinting("array[n] - " + array[n], testVars.logerVars.MINOR);
		return array[n];
	}
	
	// Create a Queue with a given parameters withoud add Groups
	public void createQueue(WebDriver driver, String queueName, String queueDescription, Map<String, String> flagDetails) {
		
		// Fill data at Queue
		driver.switchTo().frame(1);
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a"), 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")	   , queueName		 , 1000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), queueDescription, 1000);
		
		// If Enable queue time-out data is needed to be filled 
		if (flagDetails.containsKey("timeOutIsEnabled")) {
			
			testFuncs.myClick(driver, By.xpath("//*[@id='enableTimeout']"), 1000);
			driver.findElement(By.xpath("//*[@id='timeOut']")).clear();
			testFuncs.mySendKeys(driver, By.xpath("//*[@id='timeOut']"), flagDetails.get("timeOutPeriod"), 2000);
			Select timeoutCallAction = new Select(driver.findElement(By.xpath("//*[@id='timeOutPeriod']")));
			timeoutCallAction.selectByValue(flagDetails.get("timeOutAction"));
			
			// Set a target for transfer
			String action = flagDetails.get("timeOutAction");
			if (action.contains("VoiceMail") || action.contains("TelNumber") || action.contains("SipAddress")) {
				
				driver.findElement(By.xpath("//*[@id='timeoutDest']")).clear();
				testFuncs.mySendKeys(driver, By.xpath("//*[@id='timeoutDest']"), flagDetails.get("timeOutTarget"), 2000);
			} else if (action.contains("OtherQueue")) {
				
				Select timeoutQueueTarget = new Select(driver.findElement(By.xpath("//*[@id='timeoutQueueId']")));
				timeoutQueueTarget.selectByIndex(1);
			}
		}
		
		// If Enable queue overflow data is needed to be filled 
		if (flagDetails.containsKey("overflowIsEnabled")) {
			
			testFuncs.myClick(driver, By.xpath("//*[@id='enableQueueOverflow']"), 1000);
			driver.findElement(By.xpath("//*[@id='maxNumOfCalls']")).clear();
			testFuncs.mySendKeys(driver, By.xpath("//*[@id='maxNumOfCalls']"), flagDetails.get("overflowCallsNumber"), 2000);
			Select overflowForwardMode = new Select(driver.findElement(By.xpath("//*[@id='forwardCall']")));
			overflowForwardMode.selectByValue(flagDetails.get("overflowForwardMode"));
			Select overflowAction = new Select(driver.findElement(By.xpath("//*[@id='overFlowAction']")));		
			overflowAction.selectByValue(flagDetails.get("overflowAction"));
			
			// Set a target for transfer
			String action = flagDetails.get("overflowAction");
			if (action.contains("VoiceMail") || action.contains("TelNumber") || action.contains("SipAddress")) {
				
				driver.findElement(By.xpath("//*[@id='overflowDest']")).clear();
				testFuncs.mySendKeys(driver, By.xpath("//*[@id='overflowDest']"), flagDetails.get("overflowTarget"), 2000);
			} else if (action.contains("OtherQueue")) {
				
				Select overflowQueueTarget = new Select(driver.findElement(By.xpath("//*[@id='overflowQueueId']")));
				overflowQueueTarget.selectByIndex(1);
			}
		}
	  	  
		// Submit and verify create
		driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
		driver.switchTo().frame(1); 
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		if (bodyText.contains(queueName)) {
	  		  
			testFuncs.myDebugPrinting("Queue was detected !!", testVars.logerVars.MINOR);
		} else {
	  		  
			testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
	  		bodyText     = driver.findElement(By.tagName("body")).getText();
	  		if (bodyText.contains(queueName)) {  
	  			  
	  		 testFuncs.myDebugPrinting("Queue was detected !!", testVars.logerVars.MINOR);
	  		} else {
	  			
	  			testFuncs.myFail("Queue was not detected !!");  
	  		}
		}
		testFuncs.searchStr(driver, queueDescription);	
	}
	
	// Delete a Queue with a given parameters
	public void deleteQueue(WebDriver driver, String queueName, String queueDesc) {
		
		// Return to start of Queues list
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.acdQueues), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, queueName);
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[5]/a"), 3000); 
		testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete queue entry?");
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

		// Verify delete
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		testFuncs.myAssertTrue("Queue name was still detected !! (queueName - " 			+ queueName + ")", !bodyText.contains(queueName));  
		testFuncs.myAssertTrue("Queue description was still detected !! (newDisplayName - " + queueDesc + ")", !bodyText.contains(queueDesc));	
	}
	
	// Get a index for a row with a given Queue phone number
	public int getIdx(WebDriver driver, String queueName) {
		  
		Boolean isFirstPage = true;
		int lastSearch 	  = 0;  
		  
		while (true) {
			  
		  // Search for Queue in the menu records
		  String bodyText     = driver.findElement(By.tagName("body")).getText(); 
		  String[] rows = bodyText.split("\n");
		  for (int i = 0; i < rows.length; ++i) {
			  
			  testFuncs.myDebugPrinting("rows[" + i + "] - " + rows[i], testVars.logerVars.MINOR);
			  if (rows[i].contains(queueName)) {
				  
				  testFuncs.myDebugPrinting(queueName + " row is: " + (i - 2 + lastSearch), testVars.logerVars.MINOR);
				  return (i - 2 + lastSearch);
			  }  
		  }
		  lastSearch = rows.length - 3;
		  testFuncs.myDebugPrinting("lastSearch - " + lastSearch, testVars.logerVars.MINOR);
		  		  
		  // If Record was not found search for it in second page 
		  testFuncs.myDebugPrinting("Record was not found search for it in second page  !!", testVars.logerVars.MINOR);
		  if (!isFirstPage) {
				  
			  testFuncs.myFail("Queue was not detected !! (" + queueName + ")");
			  break;
		  } else {
				  
			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);  
			  isFirstPage = false;  
		  }
		}
	 
		return -1;
	}
	
	// Edit Queue main data
	public void editQueue(WebDriver driver, String queueName, String newQueueName, String newQueueDesc) {
		
		// Return to start of Queues list
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.acdQueues), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, queueName);
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[4]/a"), 3000); 
		
		// Fill data at Queue
		driver.findElement(By.xpath("//*[@id='name']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")	   , newQueueName		 , 1000);
		driver.findElement(By.xpath("//*[@id='description']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), newQueueDesc, 1000);
		
		// Submit and verify edit
		driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
		driver.switchTo().frame(1); 
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		if (bodyText.contains(newQueueName)) {
	  		  
			testFuncs.myDebugPrinting("Queue was edited successfully !!", testVars.logerVars.MINOR);
		} else {
	  		  
			testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
	  		bodyText     = driver.findElement(By.tagName("body")).getText();
	  		if (bodyText.contains(queueName)) {  
	  			  
	  		 testFuncs.myDebugPrinting("Queue was edited unsuccessfully !!", testVars.logerVars.MINOR);
	  		} else {
	  			
	  			testFuncs.myFail("Queue was not detected !!");  
	  		}
		}
		testFuncs.searchStr(driver, newQueueDesc);
	}
}
