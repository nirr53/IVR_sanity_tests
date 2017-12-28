package SourceClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MusicOnHold {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public MusicOnHold() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }
    
    // Edit a MOH description using a given MOH name
    public void editMOH(WebDriver driver, String mohName, String newMohDesc) {
  	
  	  // Edit MOH	  
  	  int mohRow  = getIdx(driver, mohName);
  	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + mohRow + "]/td[7]/a"), 3000);
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[3]/td[2]/input"), newMohDesc, 1000);
  	  driver.switchTo().defaultContent();	
  	  testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
  	  
  	  // Verify create
  	  driver.switchTo().frame(1); 
  	  testFuncs.searchStr(driver, mohName);
  	  testFuncs.searchStr(driver, newMohDesc);
    }

    // Delete a MOH using a given MOH name
    public void deleteMOH(WebDriver driver, String mohName, String mohDesc) {
  	  
  	  // Delete MOH	  
  	  int mohRow  = getIdx(driver, mohName);
  	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr[" + mohRow + "]/td[8]/a"), 3000);
  	  testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete " + mohName + "music on hold entry?");
  	  testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

  	  // Verify delete
  	  String bodyText     = driver.findElement(By.tagName("body")).getText();
  	  testFuncs.myAssertTrue("MOH name was still detected !! (mohName - " + mohName + ")"		, !bodyText.contains(mohName));
  	  testFuncs.myAssertTrue("MOH description was still detected !! (mohDesc - " + mohDesc + ")", !bodyText.contains(mohDesc));
    }

    // Get a index for a row with  a given MOH name
    public int getIdx(WebDriver driver, String mohName) {
  	  
  	  String bodyText     = driver.findElement(By.tagName("body")).getText(); 
  	  String[] rows = bodyText.split("\n");
  	  for (int i = 0; i < rows.length; ++i) {
  		  
  		  testFuncs.myDebugPrinting("rows[" + i + "] - " + rows[i], testVars.logerVars.MINOR);
  		  if (rows[i].contains(mohName)) {
  			  
  			  testFuncs.myDebugPrinting(mohName + " row is: " + (i-1), testVars.logerVars.MINOR);
  			  return (i - 1);
  		  }  
  	  }
  	  testFuncs.myFail("MOH was not detected !! (" + mohName + ")");
  	
  	  return -1;
    }

    // Create a MOH using a given name, description and path to wma-file
    public void createMOH(WebDriver driver, String mohName, String mohDesc, String mohFile) {
  	
  	  // Create new MOH
  	  driver.switchTo().frame(1); 
  	  testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a"), 3000);
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[2]/td[2]/input"), mohName, 1000);
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[3]/td[2]/input"), mohDesc, 1000);
  	  testFuncs.mySendKeys(driver, By.id("fileToUploadmoh"), mohFile, 1000);		
  	  driver.switchTo().defaultContent();	
  	  testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
  	  
  	  // Verify create
  	  driver.switchTo().frame(1); 
  	  testFuncs.searchStr(driver, mohName);
  	  testFuncs.searchStr(driver, mohDesc);
    }
}
