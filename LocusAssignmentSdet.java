package newpack;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/*
1.  Search - open www.flipkart.com (it would show a pop up, you need to cancel the pop up), perform a search for term 'shoes', 
    you now need to verify that the values on the search page are actually searched for shoes. 
2. Apply Filters - You can choose two filters - eg - price & brand. You need to ensure the filters are selected. 
   Again this is left a bit open-ended for you to come up with the asserts yourself. 
3. Product Detail Page - Open the detail page of the first result, select the size of the item & then click 'buy now'. 
   You should be on login page now. 
   
   PreRequisite: 1. User has to provide the minimum and max value from the given options.
                 2. User has to provide the correct spelling of brand name.
*/

public class LocusAssignmentSdet {


	
	public static WebDriver driver;
	public WebElement elem;
	boolean flag;
	
	
	@BeforeTest
	public void loginWeb() {
		
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		driver = new ChromeDriver();	
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get("https://www.flipkart.com/");
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		try {
		      driver.findElement(By.xpath("//button[@class='_2AkmmA _29YdH8']")).click();
		}catch(Exception e) {
			
		  //System.out.println(e);
		  
		}
		
		   
	}
	
	@Test
	public void searchShoes() throws InterruptedException {
		
		elem = driver.findElement(By.xpath("//input[@title='Search for products, brands and more']"));
		assertNotNull(elem);
		elem.sendKeys("shoes");
		elem = driver.findElement(By.xpath("//button[@type='submit']"));
		elem.click();
		try {
		      driver.findElement(By.xpath("//button[@class='_2AkmmA _29YdH8']")).click();
		}catch(Exception e) {
			
		 // System.out.println(e);
		}
		
		Thread.sleep(5000);
		String ActualSearchResult = driver.findElement(By.xpath("//span[@class='_2yAnYN']")).getText();
		System.out.println(ActualSearchResult);
		String [] result = ActualSearchResult.split(" ");
		System.out.println(result[8]);
		assertEquals(result[8],"\"shoes\"");
		
		//Code to provide list of min Price
		 List <WebElement> minvalues = driver.findElements(By.xpath("//div[@class='_1qKb_B']//select[@class='fPjUPw']"));
		 int size = minvalues.size();
		 for(int i=0; i<size; i++) { 
			System.out.println("min values avaialble is " + minvalues.get(i).getText());
		 }
		 
		 
		 //Take user input for min value
		 Scanner input = new Scanner(System.in);
		 System.out.println("Enter the min value: " );
		 String minimumvalue = input.nextLine();
		 
		 Select minvalue = new Select(driver.findElement(By.xpath("//div[@class='_1qKb_B']//select[@class='fPjUPw']")));
		 minvalue.selectByValue(minimumvalue);
		 int minprice = Integer.parseInt(minimumvalue);
		 
		 
		//Code to provide list of max Price 
		 List <WebElement> maxvalues = driver.findElements(By.xpath("//div[@class='_1YoBfV']//select[@class='fPjUPw']"));
		 size = maxvalues.size();
		 for(int i=0; i<size; i++) { 
			System.out.println("max values avaialble is " + maxvalues.get(i).getText());
		 }
		 
		//Take user input for max value
		 System.out.println("Enter the max value: " );
		 String maximumvalue = input.nextLine();
		 int maxprice = Integer.parseInt(maximumvalue);
		 
		 Select maxvalue = new Select(driver.findElement(By.xpath("//div[@class='_1YoBfV']//select[@class='fPjUPw']")));
		 maxvalue.selectByValue(maximumvalue);
		 
		 //Code to apply the Brand filter 
		System.out.println("Enter the brand name to be selected: " ); 
		String Brandvalue = input.nextLine();
		elem = driver.findElement(By.xpath("//input[@placeholder='Search Brand']"));
		elem.clear();
		elem.sendKeys(Brandvalue);
		elem = driver.findElement(By.xpath("//div[@class='_1GEhLw'][text()='"+Brandvalue+"']"));
		flag = elem.isSelected();
		if(flag==false) {
			elem.click();
		}else {
			System.out.println("given brand is already selected");
		}
		
		Thread.sleep(5000);
		
       // Code to verify brand filter is applied for the searched result		
		List<WebElement> actualBrand= driver.findElements(By.xpath("//div[@class='_2B_pmu']"));
		
		for(int i=1; i<2;i++) {
			   
			   String str = actualBrand.get(i).getText();
			   System.out.println(str);
			   assertEquals(str, Brandvalue);			  		  			   
	     }
			  		   				   
		
		   
	   // Code to verify price filter is applied for the searched result	
	   List<WebElement> elem = driver.findElements(By.xpath("//div[@class='_1vC4OE']"));
	   
	   for(int i=1; i<2;i++) {
		   
		   String str = elem.get(i).getText();
		   System.out.println(str);
		   String actualPrice = str.replaceAll("[₹,]", "");
		   int price = Integer.parseInt(actualPrice);
		   System.out.println(price);
			
	         if(price>minprice && price<maxprice) {
		   
		            System.out.println(price + " is correct as per the filter applied");
	          }else {
		   
		          System.out.println(price + " is notcorrect as per the filter applied");
	          }	
 
		  		  			   
		  }
	   
	   // Code to open the detail page of the first item 
	   String Mainwindow = driver.getWindowHandle();
	   WebElement firstresult = driver.findElement(By.xpath("(//img[@class='_3togXc'])[1]"));
	   firstresult.click();
	   for(String childWindow:driver.getWindowHandles()){
		   
		   driver.switchTo().window(childWindow);
	   }
			   
	   assertTrue(driver.findElement(By.xpath("//button[text()='BUY NOW']")).isEnabled());
	   assertTrue(driver.findElement(By.xpath("//button[text()='ADD TO CART']")).isEnabled());
   
	   // Code to select the size 
		 System.out.println("Enter the size value: " );
		 int sizevalue = input.nextInt(); 
		 driver.findElement(By.xpath("//a[@class='_1TJldG _2I_hq9 _2UBURg'][text()='"+sizevalue+"']")).click();
		 flag = driver.findElement(By.xpath("//button[text()='BUY NOW']")).isEnabled();
		 if(flag == true) {
			 driver.findElement(By.xpath("//button[text()='BUY NOW']")).click();
		}else {
			System.out.println("Item is sold out, please try another size");
			 sizevalue = input.nextInt();
			 driver.findElement(By.xpath("//a[@class='_1TJldG _2I_hq9 _2UBURg'][text()='"+sizevalue+"']")).click();
			 driver.findElement(By.xpath("//button[text()='BUY NOW']")).click();
		  
		 }
		 
		 Thread.sleep(5000);
		 
	   // Code to verify the user is on login page	 
		assertTrue( driver.findElement(By.xpath("//span[text()='Login or Signup']")).isDisplayed());
		
	}
	
	@AfterTest
	public void tearDown() {
	    driver.close();	
	}
	
	

}
