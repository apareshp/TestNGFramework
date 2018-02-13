package ObjectRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RanorexOBJ {
	
	private static By element = null;
	private static String text;
	private static String multitext;
	private static int z;

	public static By logoImage () 
	{		
		element = By.xpath("//h1[@id='logo']/a");			
		return element;                                               
	}
	
	public static By txtFirstName () 
	{		
		element = By.id("FirstName");			
		return element;                                               
	}
	
	public static By txtLastName () 
	{		
		element = By.id("LastName");			
		return element;                                               
	}
	
	public static By selCategory () 
	{		
		element = By.id("Category");			
		return element;                                               
	}
	
	public static By radioGender (String text) 
	{		
		element = By.xpath("//input[@id='Gender'][@value='"+text+"']");			
		return element;                                               
	}
	
	public static By btnAdd () 
	{		
		element = By.id("Add");			
		return element;                                               
	}
	
	public static By extractTableData (String text) 
	{		
		element = By.xpath("//table[@id='VIPs']//tr[not(contains(@id,'heading'))]/td[count(//table[@id='VIPs']//td[descendant-or-self::text()='"+text+"']/preceding-sibling::td)+1]");			
		return element;                                               
	}
}
