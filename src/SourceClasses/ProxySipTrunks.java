package SourceClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProxySipTrunks {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public ProxySipTrunks() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }
    
    // Create a Proxy Trunk using given data
	public void createProxySipTrunk(WebDriver driver, String proName, String proDesc, String proAddress, String proPort, String proUri) {
		
		// Create new Proxy SIP trunks  
		driver.switchTo().frame(1); 
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a")   , 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")        , proName   , 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='displayName']") , proDesc   , 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='proxyAddress']"), proAddress, 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='proxyPort']")   , proPort   , 3000);
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='checkUri']")    , proUri    , 3000);
		driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 5000);
		
		// Verify the create
		MenuPaths testPaths = new MenuPaths();
		testFuncs.myClick(driver, By.xpath(testPaths.proSipTrunks), 5000);
		driver.switchTo().frame(1);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		testFuncs.myAssertTrue("Sip Proxy trunk name was not detected !!"		 , bodyText.contains(proName));
		testFuncs.myAssertTrue("Sip Proxy trunk display-name was not detected !!", bodyText.contains(proDesc));
		testFuncs.myAssertTrue("Sip Proxy trunk adress was not detected !!"		 , bodyText.contains(proAddress));
		testFuncs.myAssertTrue("Sip Proxy trunk port was not detected !!"        , bodyText.contains(proPort));

	}
	  
	// Get a index for a row with a given a Proxy Trunk name number
	public int getIdx(WebDriver driver, String proName) {
		  
		Boolean isFirstPage = true;
		int lastSearch 	  = 0;  
		  
		while (true) {
			  
		  // Search for endpoint in the menu records
		  String bodyText     = driver.findElement(By.tagName("body")).getText(); 
		  String[] rows = bodyText.split("\n");
		  for (int i = 0; i < rows.length; ++i) {
			  
			  testFuncs.myDebugPrinting("rows[" + i + "] - " + rows[i], testVars.logerVars.MINOR);
			  if (rows[i].contains(proName)) {
				  
				  testFuncs.myDebugPrinting(proName + " row is: " + (i - 2 + lastSearch), testVars.logerVars.MINOR);
				  return (i - 2 + lastSearch);
			  }  
		  }
		  lastSearch = rows.length - 3;
		  testFuncs.myDebugPrinting("lastSearch - " + lastSearch, testVars.logerVars.MINOR);
		  		  
		  // If Record was not found search for it in second page 
		  testFuncs.myDebugPrinting("Record was not found search for it in second page  !!", testVars.logerVars.MINOR);
		  if (!isFirstPage) {
				  
			  testFuncs.myFail("Proxy SIP trunk was not detected !! (" + proName + ")");
			  break;
		  } else {
				  
			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);  
			  isFirstPage = false;  
		  }
		  
		}
	 
		return -1;
	}
	
	// Delete a Proxy SIP trunk using a given parameters
	public void deleteProxySipTrunk(WebDriver driver, String newProName, String newProDesc) {
		
		// Return to start of Proxy SIP trunks list
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.proSipTrunks), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, newProName);
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[7]/a"), 3000); 
		testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete " + newProName + " entry?");
		testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

		// Verify delete
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		testFuncs.myAssertTrue("Proxy SIP trunk name was still detected !! (newPhoneNumber - " 		  + newProName + ")", !bodyText.contains(newProName));  
		testFuncs.myAssertTrue("Proxy SIP trunk description was still detected !! (newDisplayName - " + newProDesc + ")", !bodyText.contains(newProDesc));
	}

	// Edit a Proxy SIP trunk by a given parameters
	public void editProxySipTrunk(WebDriver driver, String proName, String newProName, String newProDesc, String newProAddress, String newProPort, String newPortUri) {
		
		// Return to start of Proxy-SIP-trunks list
		MenuPaths testMenuPaths = new  MenuPaths();    
		driver.switchTo().defaultContent();	  	  
		testFuncs.myClick(driver, By.xpath(testMenuPaths.proSipTrunks), 6000);  
		driver.switchTo().frame(1);   
		int bhRow  = getIdx(driver, proName);
		testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[6]/a"), 3000); 
		driver.findElement(By.xpath("//*[@id='name']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")        , newProName   , 3000);
		driver.findElement(By.xpath("//*[@id='displayName']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='displayName']") , newProDesc   , 3000);
		driver.findElement(By.xpath("//*[@id='proxyAddress']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='proxyAddress']"), newProAddress, 3000);	
		driver.findElement(By.xpath("//*[@id='proxyPort']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='proxyPort']")   , newProPort   , 3000);
		driver.findElement(By.xpath("//*[@id='checkUri']")).clear();
		testFuncs.mySendKeys(driver, By.xpath("//*[@id='checkUri']")    , newPortUri    , 3000);
		driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 5000);
		
		// Verify the edit
		MenuPaths testPaths = new MenuPaths();
		testFuncs.myClick(driver, By.xpath(testPaths.proSipTrunks), 5000);
		driver.switchTo().frame(1);
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		if (bodyText.contains(newProName)) {
			
			  testFuncs.myDebugPrinting("Proxy SIP trunks was detected !!", testVars.logerVars.MINOR);
		} else {
			
			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
			  bodyText     = driver.findElement(By.tagName("body")).getText();
			  if (bodyText.contains(newProName)) {  
				  
				  testFuncs.myDebugPrinting("Proxy SIP trunk was detected !!", testVars.logerVars.MINOR);
			  } else {
				  
				  testFuncs.myFail("Proxy SIP trunks was not detected !!");
			  }
		}
		testFuncs.myAssertTrue("The Proxy SIP trunk description was not edited successfully !!", bodyText.contains(newProDesc));
		testFuncs.myAssertTrue("The Proxy SIP trunk address was not edited successfully !!"    , bodyText.contains(newProAddress));
		testFuncs.myAssertTrue("The Proxy SIP trunk port was not edited successfully !!"       , bodyText.contains(newProPort));
		testFuncs.myAssertTrue("The Proxy SIP trunk port-uri was not edited successfully !!"   , bodyText.contains(newPortUri));
	}
}
