package BusinessComponent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ObjectRepository.RanorexOBJ;
import supportLibraries.*;

public class Ranorex extends ReusableLibrary{

	public static WebDriver driver;

	@Parameters("browser")
	@BeforeMethod
	  public void beforeMethod(String browser) {
		driver = initiateBrowser(browser);
		}
	
	@Test
	public void registrationRanorex()
	{
		//report.initialize();
		
		try
		{
			//if(validateHttpPageError(driver.getCurrentUrl()))
			//{

				this.pageSync(driver);
				System.out.println("second commit");
				if(isUIObjectReady(driver, RanorexOBJ.logoImage(), impTime))
				{				
					Reporter.log("Landed in Ranorex page");
					
					String firstName=getValue("FirstName");
					System.out.println("First Name: "+firstName);
					String lastName=getValue("LastName");
					System.out.println("Last Name: "+lastName);
					String category=getValue("Category");
					System.out.println("Category"+category);
					String gender=getValue("Gender");
					System.out.println("Gender"+gender);

					
					HashMap<String, String> hshRegisteredData = new HashMap<String, String>();
					hshRegisteredData.put("First Name", firstName);
					hshRegisteredData.put("Last Name", lastName);
					hshRegisteredData.put("Category", category);
					hshRegisteredData.put("Gender", gender);
					
					logSendKeys(driver, RanorexOBJ.txtFirstName(), firstName, "First Name");
					logSendKeys(driver, RanorexOBJ.txtLastName(), lastName, "Last Name");
					logClick(driver, RanorexOBJ.radioGender(gender.toLowerCase()), "Gender");
					logSelect(driver, RanorexOBJ.selCategory(), category, "Category");

					logClick(driver, RanorexOBJ.btnAdd(), "Add button");
					
					Reporter.log("<b>"+"VALIDATE REGISTRATION DATA IN TABLE"+"</b>");
					
					 Iterator it1 = hshRegisteredData.entrySet().iterator();
					
					    while (it1.hasNext()) 
					    {
					        Map.Entry pair1 = (Map.Entry)it1.next();
				        	
					        String key = pair1.getKey().toString();
					        String value = pair1.getValue().toString();
				        
					        String actualValue=driver.findElement(RanorexOBJ.extractTableData(key)).getText().trim();
					        
					        Assert.assertEquals(value, actualValue);
					        
					        if(value.equals(actualValue))
					        {
					        	Reporter.log("Value for the field:: "+key +" is correctly reflected as:: "+actualValue);
					        }
					        else
					        {
					        	Reporter.log("Value for the field:: "+key +" is reflected as:: "+actualValue+" instead of:: "+value);
					        }
					    }
					    
					   
				}
				else
				{
					Reporter.log("<b>"+"NOT LANDED IN RANOREX PAGE"+"</b>");
				}
				
			
			//}
		}
		catch(Exception e)
		{
			Assert.assertTrue(false);
			Reporter.log("ISSUE with REGISTRATION: "+e.getMessage());
		}
	}
	
	
	@Test
	public void verifyUI()
	{
		//report.initialize();
		
		try
		{
			//if(validateHttpPageError(driver.getCurrentUrl()))
			//{

				this.pageSync(driver);
				if(isUIObjectReady(driver, RanorexOBJ.logoImage(), impTime))
				{				
					Reporter.log("<b>"+"VERIFY UI"+"</b>");
					
					String firstName=getValue("FirstName");
					System.out.println("First Name: "+firstName);
					String lastName=getValue("LastName");
					System.out.println("Last Name: "+lastName);
					String category=getValue("Category");
					System.out.println("Category"+category);
					String gender=getValue("Gender");
					System.out.println("Gender"+gender);

					HashMap<String, By> hshWebUI = new HashMap<String, By>();
					hshWebUI.put("First Name", RanorexOBJ.txtFirstName());
					hshWebUI.put("Last Name", RanorexOBJ.txtLastName());
					hshWebUI.put("Category", RanorexOBJ.selCategory());
					hshWebUI.put("Gender", RanorexOBJ.radioGender(gender.toLowerCase()));
					hshWebUI.put("Add button", RanorexOBJ.btnAdd());
					
					checkElementPresence(driver, hshWebUI);
					
					
				}
				else
				{
					Reporter.log("<b>"+"NOT LANDED IN RANOREX PAGE"+"</b>");
				}
				
			
			//}
		}
		catch(Exception e)
		{
			Assert.assertTrue(false);
			Reporter.log("ISSUE with UI LEVEL VALIDATION: "+e.getMessage());
		}
	}
	
	@AfterMethod
	public void cleanUP()
	{
		driver.quit();
	}
	
	@AfterSuite
	public void mail()
	{
		sendEmail();
	}
	
}
