package SourceClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IvrEndPoints {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public IvrEndPoints() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }
    
    
    // Delete a IVR-endpoint using a given number
    public void deleteIVREndPoint(WebDriver driver, String ivrNumber) {
  	  
  	  // Return to start of IVR-endpoints list
  	  MenuPaths testMenuPaths = new  MenuPaths();  
  	  driver.switchTo().defaultContent();
  	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementIvrEndPnts), 6000);
  	  driver.switchTo().frame(1); 
  	  
  	  // Delete a IVR-endpoint
  	  int bhRow  = getIdx(driver, ivrNumber);  	  
  	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr/td[" + bhRow + "]/a"), 3000);
  	  testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete " + bhRow + " entry?");
  	  testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

  	  // Verify delete
  	  String bodyText     = driver.findElement(By.tagName("body")).getText();
  	  testFuncs.myAssertTrue("IVR number was still detected !! (ivrNumber - " + ivrNumber + ")"		, !bodyText.contains(ivrNumber));
    }
    
    // Get a index for a row with a given IVR-endpoint name
    private int getIdx(WebDriver driver, String ivrNumber) {
  	  
  	  Boolean isFirstPage = true;
  	  int lastSearch 	  = 0;
  	  
  	  while (true) {
  		  
  		  // Search for endpoint in the menu records
  		  String bodyText     = driver.findElement(By.tagName("body")).getText(); 
  		  String[] rows = bodyText.split("\n");
  		  for (int i = 0; i < rows.length; ++i) {
  			  
  			  testFuncs.myDebugPrinting("rows[" + i + "] - " + rows[i], testVars.logerVars.MINOR);
  			  if (rows[i].contains(ivrNumber)) {
  				  
  				  testFuncs.myDebugPrinting(ivrNumber + " row is: " + (i + 1 + lastSearch), testVars.logerVars.MINOR);
  				  return (i + 1 + lastSearch);
  			  }  
  		  }
  		  lastSearch = rows.length - 3;
  		  		  
  		  // If Record was not found search for it in second page 
  		  testFuncs.myDebugPrinting("Record was not found search for it in second page  !!", testVars.logerVars.MINOR);
  		  if (!isFirstPage) {
  				  
  			  testFuncs.myFail("IVR end-point was not detected !! (" + ivrNumber + ")");
  			  break;
  		  } else {
  				  
  			  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);  
  			  isFirstPage = false;  
  		  }
  	  }
   
  	  return -1;
    }

    // Create an IVR-endpoint using a given number
    public void createIVREndPoint(WebDriver driver, String ivrNumber) {
  	
  	  // Create new IVR-endpoint
  	  driver.switchTo().frame(1); 
  	  testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a[1]"), 3000);
  	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='search_box']"), ivrNumber, 1000);  
  	  testFuncs.myClick(driver, By.xpath("//*[@id='search_form']/div/fieldset/table/tbody/tr[1]/td/table/tbody/tr/td[6]/a/span"), 7000);
  	  testFuncs.verifyStrByXpath(driver, "//*[@id='searchword']", "Search results for:" + ivrNumber);
  	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody/tr/td[6]/a/span"), 2000);
  	  driver.switchTo().defaultContent();	
  	  testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 6000);
  	  
  	  // Verify create
  	  testFuncs.myDebugPrinting("Verify create", testVars.logerVars.MINOR);
  	  MenuPaths testMenuPaths = new  MenuPaths();
  	  testFuncs.myClick(driver, By.xpath(testMenuPaths.aaManagementIvrEndPnts), 6000);
  	  driver.switchTo().frame(1); 
  	  String bodyText     = driver.findElement(By.tagName("body")).getText();
  	  if (bodyText.contains(ivrNumber)) {
  		  
  		  testFuncs.myDebugPrinting("IVR endpoint was detected !!", testVars.logerVars.MINOR);
  	  } else {
  		  
  		  testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
  		  bodyText     = driver.findElement(By.tagName("body")).getText();
  		  if (bodyText.contains(ivrNumber)) {  
  			  
  			  testFuncs.myDebugPrinting("IVR endpoint was detected !!", testVars.logerVars.MINOR);
  		  } else {
  			  
  			  testFuncs.myFail("IVR endpoint was not detected !!");
  		  }
  	  }
    }
}