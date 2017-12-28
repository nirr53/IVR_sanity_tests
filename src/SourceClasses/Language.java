package SourceClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Language {
		  
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public Language() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }

    // Edit an exisiting Language record using a given name
    public void editLang(WebDriver driver, String langName, String newLangDesc) {
		
	  //  Edit an exisiting Business hours record  
	  int bhRow  = getIdx(driver, langName);
	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody[2]/tr[" + bhRow + "]/td[3]/a"), 3000);	  
	  driver.findElement(By.xpath("//*[@id='description']")).clear();
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), newLangDesc, 1000);
	  driver.switchTo().defaultContent();	
	  testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000); 
	  
	  // Verify edit
	  driver.switchTo().frame(1); 
	  String bodyText     = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("Language was not edited successfully !! (newBHDesc - " + newLangDesc + ")\n" + bodyText, bodyText.contains(newLangDesc));  
    }

    // Delete a Language record using a given name  
    public void deleteLang(WebDriver driver, String langName, String langDesc) {
		  
	  // Delete a Language record  
	  int bhRow  = getIdx(driver, langName);
	  testFuncs.myClick(driver, By.xpath("//*[@id='results']/tbody[2]/tr[" + bhRow + "]/td[4]/a"), 3000);	  
	  testFuncs.verifyStrByXpath(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete language entry?");
	  testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 7000);

	  // Verify delete
	  String bodyText     = driver.findElement(By.tagName("body")).getText();
	  testFuncs.myAssertTrue("Language name was still detected !! (langName - " + langName + ")"	   , !bodyText.contains(langName));
	  testFuncs.myAssertTrue("Language description was still detected !! (langDesc - " + langDesc + ")", !bodyText.contains(langDesc));  
    }
	  
    // Get a index for a row with  a given MOH name  
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

    // Create a Language record a given name, description and days/start-end xpath arrays  
    public void createLanguage(WebDriver driver, String langName, String langDesc) {
		
	  // Create new Business hours
	  driver.switchTo().frame(1); 
	  testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a/span"), 3000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='name']")		 , langName, 1000);
	  testFuncs.mySendKeys(driver, By.xpath("//*[@id='description']"), langDesc, 1000);
	  driver.switchTo().defaultContent();	
	  testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
	  
	  // Verify create
	  driver.switchTo().frame(1); 
	  testFuncs.searchStr(driver, langName);
	  testFuncs.searchStr(driver, langDesc); 
    }
}
