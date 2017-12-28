package SourceClasses;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class AcdFlows {
	
	GlobalFuncs		testFuncs;  
	GlobalVars 		testVars;

    // Default constructor
    public AcdFlows() {
    	
        testFuncs = new GlobalFuncs(); 
        testVars  = new GlobalVars();
    }

    // Create an ACD using given data
	public void createAcdFlow(WebDriver driver, String acdName, String acdDesc, Map<String, String> data) {
		
		// Create a new ACD  
		driver.switchTo().frame(1); 
		testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr[1]/td/a")   , 7000);
		
		//driver.switchTo().frame(1); // no 
		//driver.switchTo().frame(2); // no
		//driver.switchTo().frame(3); // no

		
		//List<WebElement> iframes = driver.findElements(By.xpath("//iframe"));
        // print your number of frames
       // System.out.println(iframes.size());
        
        
		//driver.switchTo().defaultContent(); // no 

		
//		<select class="HTML_INPUT_TEXT" name="lang" id="lang">
//		<option value="CA-ES">CA-ES</option>
//		<option value="EN-US" selected="">EN-US</option>
//		<option value="ES-ES">ES-ES</option>
//		<option value="FR-FR">FR-FR</option>
//		<option value="JA-JP">JA-JP</option>
//		<option value="HEBREW">HEBREW</option>
//		<option value="2.5.3">2.5.3</option>
//		<option value="VIRTUAL">VIRTUAL</option>
//		<option value="RUSSIAN">RUSSIAN</option>
//		<option value="MYLANG103506">MYLANG103506</option>
//		<option value="MYLANG104035">MYLANG104035</option>
//	</select>
		
	//	Select langsSelect = new Select(driver.findElement(By.xpath("//*[@id='lang']")));
	//	langsSelect.selectByValue(data.get("CA-ES"));


		//testFuncs.mySendKeys(driver, By.id("name"), acdName, 3000);
		//testFuncs.mySendKeys(driver, By.id("desc"), acdDesc, 3000);
		
		//driver.findElement(By.xpath("//*[@id='name']")).sendKeys("acdName"); // no
		//driver.findElement(By.id("name")).sendKeys("acdName"); //no
		//driver.findElement(By.name("name")).sendKeys("acdName"); //no

		
		// Set language
		if (data.containsKey("langEnabled")) {
			
			testFuncs.myDebugPrinting("Set language", testVars.logerVars.MINOR);
			testFuncs.myAssertTrue("langValue is not set at data<><> !!", data.containsKey("langValue"));
//			Select langsSelect = new Select(driver.findElement(By.xpath("//*[@id='lang']")));
//			langsSelect.selectByValue(data.get("langValue"));
		}
		
		// Add Prompts
		if (data.containsKey("promptEnabled")) {
			
			testFuncs.myDebugPrinting("Add Prompts", testVars.logerVars.MINOR);
			testFuncs.myClick(driver, By.xpath("//*[@id='tab1']/table/tbody/tr[9]/td/button")   , 3000);
			testFuncs.myAssertTrue("promptValue is not set at data<><> !!", data.containsKey("promptValue"));
			Select promptSelect = new Select(driver.findElement(By.xpath("//*[@id='tab1']/table/tbody/tr[10]/td/table/tbody/tr/td[1]/select")));
			promptSelect.selectByValue(data.get("promptValue"));	
		}
		
		// Outside of Business hours
		if (data.containsKey("busHoursEnabled")) {
			
			testFuncs.myDebugPrinting("Add Outside of Business hours", testVars.logerVars.MINOR);
			testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/ul/li[2]/a")   , 3000);
		}
		
		// Holidays
		if (data.containsKey("holidaysEnabled")) {
			
			testFuncs.myDebugPrinting("Add Holidays", testVars.logerVars.MINOR);
			testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr/td/ul/li[2]/a")   , 3000);
		}
		
		// Queues
		if (data.containsKey("queuesEnabled")) {
			
			testFuncs.myDebugPrinting("Add Queues", testVars.logerVars.MINOR);
			testFuncs.myClick(driver, By.xpath("//*[@id='trunkTBL']/table/tbody/tr/td/table/tbody/tr[4]/td/table/tbody/tr/td/ul/li[2]/a")   , 3000);
		}
		
		// Submit and verify create
		//driver.switchTo().defaultContent();	
		testFuncs.myClick(driver, By.xpath("//*[@id='submit_img']"), 10000);
		driver.switchTo().frame(1); 
		String bodyText     = driver.findElement(By.tagName("body")).getText();
		if (bodyText.contains(acdName)) {
	  		  
			testFuncs.myDebugPrinting("ACD was detected !!", testVars.logerVars.MINOR);
		} else {
	  		  
			testFuncs.myClick(driver, By.xpath("//*[@id='pageNavPosition']/span[4]"), 3000);
	  		bodyText     = driver.findElement(By.tagName("body")).getText();
	  		if (bodyText.contains(acdName)) {  
	  			  
	  		 testFuncs.myDebugPrinting("ACD was detected !!", testVars.logerVars.MINOR);
	  		} else {
	  			
	  			testFuncs.myFail("ACD was not detected !!");  
	  		}
		}
		testFuncs.searchStr(driver, acdDesc);
	}
}
