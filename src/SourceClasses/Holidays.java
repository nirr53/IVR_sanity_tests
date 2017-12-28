package SourceClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class Holidays {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public Holidays() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }
    
    // Edit an exisiting Holiday using old-name, new-name and description
    public void editHoliday(WebDriver driver, String hoName, String newHoName, String newHoDesc) {
  	
  	  //  Edit an exisiting Holiday 
  	  int bhRow  = getIdx(driver, hoName);
  	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[5]/a"), 3000);
  	  driver.findElement(By.xpath("//*[@id='name']")).clear();
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")		 , newHoName, 1000);
  	  driver.findElement(By.xpath("//*[@id='description']")).clear();
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), newHoDesc, 1000);
  	  driver.switchTo().defaultContent();	
  	  testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000); 
  	  
  	  // Verify edit
  	  driver.switchTo().frame(1); 
  	  String bodyText     = driver.findElement(By.tagName("body")).getText();
  	  testFuncs.myAssertTrue("Holiday name was not edited !! (newHoName - " + newHoName + ")\n" 	   + bodyText, bodyText.contains(newHoName));
  	  testFuncs.myAssertTrue("Holiday description was not edited !! (newHoDesc - " + newHoDesc + ")\n" + bodyText, bodyText.contains(newHoDesc));
    }

    // Delete an Holiday using a given name
    public void deleteHoliday(WebDriver driver, String hoName, String hoDesc) {
  	  
  	  // Delete an Holiday	  
  	  int bhRow  = getIdx(driver, hoName);
  	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + bhRow + "]/td[6]/a"), 3000);
  	  testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete entry?");
  	  testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

  	  // Verify delete
  	  String bodyText     = driver.findElement(By.tagName("body")).getText();
  	  testFuncs.myAssertTrue("Holiday name was still detected !! (hoName - " + hoName + ")"		, !bodyText.contains(hoName));
  	  testFuncs.myAssertTrue("Holiday description was still detected !! (hoDesc - " + hoDesc + ")", !bodyText.contains(hoDesc));
    }
    
    // Get a index for a row with a given Holiday name
    public int getIdx(WebDriver driver, String hoName) {
  	  
  	  String bodyText     = driver.findElement(By.tagName("body")).getText(); 
  	  String[] rows = bodyText.split("\n");
  	  for (int i = 0; i < rows.length; ++i) {
  		  
  		  testFuncs.myDebugPrinting("rows[" + i + "] - " + rows[i], testVars.logerVars.MINOR);
  		  if (rows[i].contains(hoName)) {
  			  
  			  testFuncs.myDebugPrinting(hoName + " row is: " + (i - 2), testVars.logerVars.MINOR);
  			  return (i - 2);
  		  }
  	  }
   
  	  testFuncs.myFail("Holiday was not detected !! (" + hoName + ")");
  	  return -1;
    }

    // Create an Holiday using a given name, description and start-end dates + flags
    public void createHoliday(WebDriver driver, String holidayName, String holidayDesc, String startDate, String endDate, Boolean isRecurrent, Boolean isActivate) {
  	
  	  String holidayRecName = "rec_" + holidayName;
  	  String holidayRecDesc = "rec_" + holidayDesc;
  	  
  	  // Create new Holiday set
  	  testFuncs.myDebugPrinting("Create new Holiday set", testVars.logerVars.NORMAL);
  	  driver.switchTo().frame(1); 
  	  testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a"), 3000);
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")		 , holidayName, 3000);
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), holidayDesc, 3000);
  	  
  	  // Add new Holiday
  	  testFuncs.myDebugPrinting("Add new Holiday", testVars.logerVars.NORMAL);
  	  testFuncs.myClick(driver, By.xpath("//*[@id='tr1']/td/a/span"), 5000);	
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='desc']"), holidayRecDesc, 3000);
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='desc']"), Keys.chord(Keys.SHIFT,Keys.TAB), 3000);
  	  driver.switchTo().activeElement().sendKeys(holidayRecName);
  	  if (isRecurrent) {  
  		  testFuncs.myClick(driver, By.xpath("//*[@id='isReccuring']"), 3000);	
  	  }
  	  if (isActivate) {  
  		  testFuncs.myClick(driver, By.xpath("//*[@id='isActivated']"), 3000);		
  	  }
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='startDate']")	 					 , startDate , 3000);  
  	  testFuncs.myClick(driver, By.xpath("//*[@id='ui-datepicker-div']/div[6]/button[2]"), 3000);	
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='endDate']")	 					 , endDate   , 3000);
  	  testFuncs.myClick(driver, By.xpath("//*[@id='ui-datepicker-div']/div[6]/button[2]"), 3000);			  
  	  testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonOk']")			     , 3000);

  	  // Verify create of Record
  	  testFuncs.myDebugPrinting("Verify create of Record", testVars.logerVars.NORMAL);
  	  testFuncs.searchStr(driver, holidayRecName);
  	  testFuncs.searchStr(driver, holidayRecDesc);
  	  testFuncs.searchStr(driver, startDate );
  	  testFuncs.searchStr(driver, endDate);
  	  
  	  // Submit and Verify create
  	  testFuncs.myDebugPrinting("Submit and Verify create", testVars.logerVars.NORMAL);
  	  driver.switchTo().defaultContent();	
  	  testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
  	  driver.switchTo().frame(1); 
  	  testFuncs.searchStr(driver, holidayName);
  	  testFuncs.searchStr(driver, holidayDesc);
  	  testFuncs.searchStr(driver, holidayRecName + " (" + holidayRecDesc + ")");
    }
}