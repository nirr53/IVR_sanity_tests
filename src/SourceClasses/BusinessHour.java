package SourceClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BusinessHour {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public BusinessHour() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }
    
    // Edit an exisiting Business hours record using a given name
    public void ediBusHours(WebDriver driver, String busHouName, String newBHName, String newBHDesc) {
  	
  	  //  Edit an exisiting Business hours record  
  	  int bhRow  = getIdx(driver, busHouName);
  	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[4]/a"), 3000);
  	  driver.findElement(By.xpath("//*[@id='name']")).clear();
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")		 , newBHName, 1000);
  	  driver.findElement(By.xpath("//*[@id='description']")).clear();
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), newBHDesc, 1000);
  	  driver.switchTo().defaultContent();	
  	  testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000); 
  	  
  	  // Verify edit
  	  driver.switchTo().frame(1); 
  	  String bodyText     = driver.findElement(By.tagName("body")).getText();
  	  testFuncs.myAssertTrue("Bussiness hours name was not edited !! (newBHName - " + newBHName + ")\n" 	   + bodyText, bodyText.contains(newBHName));
  	  testFuncs.myAssertTrue("Bussiness hours description was not edited !! (newBHDesc - " + newBHDesc + ")\n" + bodyText, bodyText.contains(newBHDesc));
    }

    // Delete a Business hours record using a given name
    public void deleteBusHours(WebDriver driver, String busHouName, String busHouDesc) {
  	  
  	  // Delete Business hours record	  
  	  int bhRow  = getIdx(driver, busHouName);
  	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[5]/a"), 3000);
  	  testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete business hours entry?");
  	  testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

  	  // Verify delete
  	  String bodyText     = driver.findElement(By.tagName("body")).getText();
  	  testFuncs.myAssertTrue("Bussiness hours name was still detected !! (mohName - " + busHouName + ")"		, !bodyText.contains(busHouName));
  	  testFuncs.myAssertTrue("Bussiness hours description was still detected !! (mohDesc - " + busHouDesc + ")", !bodyText.contains(busHouDesc));
    }
    
    // Get a index for a row with a given Business Hours name
    public int getIdx(WebDriver driver, String busHouName) {
  	  
  	  String bodyText     = driver.findElement(By.tagName("body")).getText(); 
  	  String[] rows = bodyText.split("\n");
  	  for (int i = 0; i < rows.length; ++i) {
  		  
  		  testFuncs.myDebugPrinting("rows[" + i + "] - " + rows[i], testVars.logerVars.MINOR);
  		  if (rows[i].contains(busHouName)) {
  			  
  			  testFuncs.myDebugPrinting(busHouName + " row is: " + (i - 2), testVars.logerVars.MINOR);
  			  return (i - 2);
  		  }  
  	  }
  	  testFuncs.myFail("MOH was not detected !! (" + busHouName + ")");
  	  
  	  return -1;
    }

    // Create a Business hours using a given name, description and days/start-end xpath arrays
    public void createBusHours(WebDriver driver, String busHouName, String busHouDesc, String daysXpath[], String startXpath[], String endXpath[]) {
  	
  	  // Create new Business hours
  	  driver.switchTo().frame(1); 
  	  testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a"), 3000);
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")		 , busHouName, 1000);
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), busHouDesc, 1000);
  	  for (int i = 0; i < 7; ++i) {
  		  
  		  if (Math.random() < 0.5) {
  			  
  			  testFuncs.myClick(driver, By.xpath(daysXpath[i]), 3000);
  			  testFuncs.mySendKeys(driver, By.xpath("//*[@id='" + startXpath[i] + "']"), "00:01", 2000);
  			  testFuncs.mySendKeys(driver, By.xpath("//*[@id='" + endXpath[i] + "']")  , "23:59", 2000);	  
  		  }
  	  }
  	  driver.switchTo().defaultContent();	
  	  testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
  	  
  	  // Verify create
  	  driver.switchTo().frame(1); 
  	  testFuncs.searchStr(driver, busHouName);
  	  testFuncs.searchStr(driver, busHouDesc);
    }
}
