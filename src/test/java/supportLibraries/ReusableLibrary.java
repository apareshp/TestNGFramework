package supportLibraries;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.xml.sax.InputSource;

public class ReusableLibrary {

String sParLoc=System.getProperty("user.dir")+"/src/test/java";
public WebDriver driver;
protected int impTime = Integer.parseInt(readProp("waitTime"));


	public String getValue(String Node)
	{
		String value = getValueXML(sParLoc+"/DataTable/Ranorex.xml","//Ranorex//name[normalize-space(text())='"+Node+"']/following-sibling::value");
		return value;
	}
	
	public synchronized String getValueXML(String sFilePath, String sNodeXpath){

		String sNodeValue="";
		try{
			InputSource in = new InputSource(sFilePath);
			XPath xp=XPathFactory.newInstance().newXPath();
			sNodeValue=(String)xp.evaluate(sNodeXpath,in, XPathConstants.STRING);
		}
		catch(Exception e){
			System.out.println("getValueXML"+e.getMessage());
		}
		return sNodeValue;
	}
	
	public String readProp(String val)
	{
		String url = "";
		try{
			Properties prop = new Properties();
			InputStream  input = new FileInputStream(sParLoc+"/GlobalSetting.properties");
			prop.load(input);
			url = prop.getProperty(val);

		}catch(Exception e){}
		
		return url;
	}
	
	public boolean isUIObjectReady(WebDriver driver, By by, int iMaxTimeout ) {
		boolean bReturn=false;
		//this.driver=driver;
		try {
			WebElement myDynamicElement = (new WebDriverWait(driver, iMaxTimeout/1000))
			.until(ExpectedConditions.presenceOfElementLocated(by));
			bReturn=true;   
		} catch (NoSuchElementException ne) {
			//report.updateTestLog("Exception:: UI object not found", by.toString()+" is not present" ,Status.FAIL);
			//System.out.println("Exception::"+ne.getMessage());
			bReturn= false;
		}
		catch(Exception e){
			//report.updateTestLog("Exception::", e.getMessage() ,Status.FAIL);
			//System.out.println("Exception::"+e.getMessage());
			bReturn= false;
		}
		return bReturn;
	}
	
	
	public WebDriver initiateBrowser(String browser)
	{
		try
		{

			  if(browser.equalsIgnoreCase("Firefox"))
			  {
				  	System.setProperty("webdriver.gecko.driver",sParLoc+"/BrowserDriversEXE/geckodriver.exe");
					driver = new FirefoxDriver();
				  	String URL= readProp("URL");	
					driver.get(URL);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			  }
			  else if(browser.equalsIgnoreCase("chrome"))
			  {
				  System.setProperty("webdriver.chrome.driver",sParLoc+"\\BrowserDriversEXE\\chromedriver.exe");
		          driver = new ChromeDriver();
				  	String URL= readProp("URL");	
					driver.get(URL);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			  }
			  else if(browser.equalsIgnoreCase("IE"))
			  {
				  System.setProperty("webdriver.ie.driver",sParLoc+"\\BrowserDriversEXE\\IEDriverServer.exe");
		          driver = new InternetExplorerDriver();
				  	String URL= readProp("URL");	
					driver.get(URL);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			  }
			  else
			  {
				  System.out.println("Invalid browser");
			  }
			  
			 
		
		}
		catch(Exception e)
		{
			Assert.assertTrue(false);
		}
		
		 return driver;
	}
	
	public void logClick(WebDriver driver, By by, String name)
	{
		try
		{
			Reporter.log("Click on the button:: "+name);
			driver.findElement(by).click();
		}
		catch(Exception e)
		{
			System.out.println("Unable to click:: "+e.getMessage());
		}
	}
	
	public void logSendKeys(WebDriver driver, By by, String message, String name)
	{
		try
		{
			Reporter.log("Enter text:: "+message +" in the textbox:: "+name);
			driver.findElement(by).clear();
			driver.findElement(by).sendKeys(message);
		}
		catch(Exception e)
		{
			System.out.println("Unable to enter text:: "+e.getMessage());
		}
	}
	
	public void logSelect(WebDriver driver, By by, String option, String name)
	{
		try
		{
			Reporter.log("Select option"+option +" from the dropdown list:: "+name);
			Select sel = new Select(driver.findElement(by));
			sel.selectByVisibleText(option);
		}
		catch(Exception e)
		{
			System.out.println("Unable to select the option:: "+e.getMessage());
		}
	}
	
	
	public void checkElementPresence(WebDriver driver, HashMap<String, By> hsh)
	{
		Iterator it = hsh.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        
	        if(isUIObjectReady(driver, (By) pair.getValue(), impTime))
	        {
	        	Assert.assertTrue(true);
	        	Reporter.log("WEB Element: "+pair.getKey() + " is displayed");
	        }
	        else
	        {
	        	Assert.assertTrue(false);
	        	Reporter.log("WEB Element: "+pair.getKey() + " is not displayed");
	        }
	       
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public boolean pageSync(WebDriver driver){

		boolean bActionStatus = false;
		int icount = impTime/1000;
		try	{
			
			for (int i = 0; i < icount; i++) {
				boolean iresult = webpageState(driver);
				Thread.sleep(1000);
				if (iresult){
					bActionStatus = true;
					break;
				}
			}			
		}
		catch (Exception e){
			bActionStatus = false;
		}
		return bActionStatus;

	}

	public boolean webpageState(WebDriver driver){
		boolean bActionStatus = false;
		try{
			String state =  ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();

			if (state.equals("complete")){
				bActionStatus = true;
			}
			else{
				bActionStatus = false;
			}
		}
		catch(Exception e){
			bActionStatus = false;
		}
		return bActionStatus;
	}
	
	public boolean validateHttpPageError(String geturl) {
		boolean toReturn = true;
		// ########## 400 ################
		int responsecode = 0;
		try {

			URL url = new URL(geturl);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.connect();
			int responseCode = urlConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				//report.updateTestLog("NO HTTP ERROR in the page for:: " + geturl, "PASS");
				toReturn = true;

			} else {

				toReturn = false;
				Reporter.log("PAGE ERROR encountered. HTTP error is: " + responseCode);
				// report.updateTestLog("400 OR 500 Error", "There is no 400/500
				// category Error displayed in the page",Status.PASS);
				Assert.assertTrue(false);
			}

		} catch (Exception e) {
			toReturn = false;
			Reporter.log("PAGE ERROR" + e.getMessage());
			// report.updateTestLog("400 OR 500 Error", "There is no 400/500
			// category Error displayed in the page",Status.PASS);
			Assert.assertTrue(false);
		}

		return toReturn;
	}
	
	public void sendEmail(){
		
	    final String hostEmailID = readProp("hostEmailID");
	    final String hostpassword = readProp("hostPassword");
	    String recipientMailID= readProp("recipientMailID");
	    String subject = readProp("subject");

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", true);
	    props.put("mail.smtp.starttls.enable", true);
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");

	    try {
	    	
	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(hostEmailID, hostpassword);
	                }
	            });

	    

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(hostEmailID));
	        message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(recipientMailID));
	        message.setSubject(subject);
	        message.setText("PFA");

	        MimeBodyPart messageBodyPart = new MimeBodyPart();

	        Multipart multipart = new MimeMultipart();

	        messageBodyPart = new MimeBodyPart();
	        String file = System.getProperty("user.dir")+"/test-output/emailable-report.html";
	        System.out.println("File Location:: "+file);
	        String fileName = "EmailReport.html";
	        DataSource source = new FileDataSource(file);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(fileName);
	        multipart.addBodyPart(messageBodyPart);

	        message.setContent(multipart);

	        System.out.println("Sending");

	        Transport.send(message);

	        System.out.println("Done");

	    } catch (MessagingException e) {
	    	System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
	}
}
