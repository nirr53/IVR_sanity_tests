package SourceClasses;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Prompts {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public Prompts() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }
    
    // Create Prompt based on text
    public void createTextPrompt(WebDriver driver, String promptName, String promptDesc) {
    	
    	driver.switchTo().frame(1); 
    	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td[2]/a/span"), 3000);
    	driver.switchTo().defaultContent(); 
    	testFuncs.searchStr(driver, "Add Prompt");
    	driver.switchTo().frame(1);
    	testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[2]/td[2]/input"), promptName, 1000);
    	testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[3]/td[2]/input"), promptDesc, 1000);
    	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[7]/td[2]/label[1]/input"), 3000);
    	testFuncs.mySendKeys(driver, By.xpath("//*[@id='promptText']"), "Hello, this is a text Prompt", 1000);
    	driver.switchTo().defaultContent();
    	testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 3000);
    	driver.switchTo().frame(1);
    	testFuncs.searchStr(driver, promptName);
    	testFuncs.searchStr(driver, promptDesc);	
    }
    
    // Create Prompt based on file
    public void createFilePrompt(WebDriver driver, String promptName, String promptDesc) {
    	
    	driver.switchTo().frame(1); 
    	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td[2]/a/span"), 3000);
    	driver.switchTo().defaultContent(); 
    	testFuncs.searchStr(driver, "Add Prompt");
    	driver.switchTo().frame(1);
    	testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[2]/td[2]/input"), promptName, 1000);
    	testFuncs.mySendKeys(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[3]/td[2]/input"), promptDesc, 1000);
    	testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[7]/td[2]/label[2]/input"), 3000);
    	String fullPath = testVars.getSrcFilesPath() + "//" + testVars.getImportFile("prompt_based_file");
    	testFuncs.myDebugPrinting("fullPath - " + fullPath, testVars.logerVars.MINOR);
    	testFuncs.mySendKeys(driver, By.id("fileToUpload"), fullPath, 1000);	
    	driver.switchTo().defaultContent();
    	testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 3000);
    	driver.switchTo().frame(1);
    	testFuncs.searchStr(driver, promptName);
    	testFuncs.searchStr(driver, promptDesc);
    	
    }
    
    // Delete a prompt with a given name
    public void deletePrompt(WebDriver driver, String promptName, String promptDesc) {
    	
    	List<WebElement> el = driver.findElements(By.cssSelector("*"));
    	for ( WebElement e : el ) {
    		
    		// Press the 'Details' button
    		if (e.getAttribute("class").contains(promptName)) {
    			
    			e.click();
    			testFuncs.myWait(2000);
    			testFuncs.searchStr(driver, "FILE");
    			List<WebElement> el2 = driver.findElements(By.cssSelector("*"));
    			for ( WebElement e2 : el2 ) {
    				
    				// Press the 'Delete' Button
    				if (e2.getText().equals("Delete")) {
    					
    					e2.click();
    					testFuncs.myWait(2000);
    					testFuncs.verifyStrByXpathContains(driver, "//*[@id='promt_div_id']", "Are you sure you want to delete " + promptName + " prompt entry?");
    					testFuncs.myClick(driver, By.xpath("//*[@id='jqi_state0_buttonDelete']"), 5000);					
    					break;
    				}	
    			}
    			break;
    		}
    	}
    	
    	// Verify delete
    	testFuncs.myAssertTrue("Prompt was not deleted successfully !!", !driver.findElement(By.tagName("body")).getText().contains(promptName));
    	testFuncs.myAssertTrue("Prompt was not deleted successfully !!", !driver.findElement(By.tagName("body")).getText().contains(promptDesc));
    }
}
