package week5.day1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ServiceNowApp extends BaseLogin {

	public String incident;

	@Test
	public void newIncident() throws InterruptedException {

		driver.findElement(By.id("sysverb_new")).click();
		driver.findElement(By.id("lookup.incident.caller_id")).click();

		Set<String> handles = driver.getWindowHandles();
		List<String> handlesList = new ArrayList<String>();
		handlesList.addAll(handles);
		driver.switchTo().window(handlesList.get(1));
		driver.findElement(By.className("glide_ref_item_link")).click();
		driver.switchTo().window(handlesList.get(0));
		driver.switchTo().frame("gsft_main");
		driver.findElement(By.id("incident.short_description")).sendKeys("Added Description");
		String incident = driver.findElement(By.id("incident.number")).getAttribute("value");
		driver.findElement(By.id("sysverb_insert_bottom")).click();

	}

	@Test(dependsOnMethods = "week5.day1.ServiceNowApp.newIncident")
	public void updateIncident() throws InterruptedException {

		driver.findElement(By.xpath("(//label[text()='Search']/following::input)[1]")).sendKeys("INC0010035");
		driver.findElement(By.xpath("(//label[text()='Search']/following::input)[1]")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//td[@class='vt'])[1]/a")).click();

		// Change urgency to high
		WebElement urgency = driver.findElement(By.xpath("//select[@name='incident.urgency']"));
		Select s1 = new Select(urgency);
		s1.selectByVisibleText("1 - High");

		// change state to In progress
		WebElement state = driver.findElement(By.xpath("//select[@name='incident.state']"));
		Select s2 = new Select(state);
		s2.selectByVisibleText("In Progress");

		// click on update
		driver.findElement(By.xpath("//button[@value='sysverb_update']"));

	}

	@Test(dependsOnMethods = "week5.day1.ServiceNowApp.updateIncident")
	public void assignIncident() throws InterruptedException {

		driver.findElement(By.xpath("(//label[text()='Search']/following::input)[1]")).sendKeys("INC0010035");
		driver.findElement(By.xpath("(//label[text()='Search']/following::input)[1]")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//td[@class='vt'])[1]/a")).click();
		driver.findElement(By.xpath("//button[@id='lookup.incident.assignment_group']/span")).click();

		Set<String> handles = driver.getWindowHandles();
		List<String> handlesList = new ArrayList<String>();
		handlesList.addAll(handles);
		driver.switchTo().window(handlesList.get(1));

		driver.findElement(By.xpath("(//label[@class='sr-only'])[2]/following-sibling::input")).sendKeys("Software");
		driver.findElement(By.xpath("(//label[@class='sr-only'])[2]/following-sibling::input")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//a[@class='glide_ref_item_link']")).click();

		driver.switchTo().window(handlesList.get(0));
		driver.switchTo().frame("gsft_main");
		driver.findElement(By.xpath("(//textarea[@placeholder='Work notes'])[2]")).sendKeys("Updating the notes");
		driver.findElement(By.xpath("//button[@ng-hide='!false && doesFormHasMandatoryJournalFields()']")).click();
		driver.findElement(By.id("sysverb_update_bottom")).click();

		String group = driver.findElement(By.xpath("(//table[@role='presentation']//tbody)[2]/tr/td[10]")).getText();
		System.out.println(group);
		Assert.assertEquals(group, "Software");
		String assigned = driver.findElement(By.xpath("(//table[@role='presentation']//tbody)[2]/tr/td[11]")).getText();
		System.out.println("The ticket is assigned to " + assigned);

	}
	
	@Test(dependsOnMethods = "week5.day1.ServiceNowApp.assignIncident")
	public void deleteIncident() throws InterruptedException {
		driver.findElement(By.xpath("(//label[text()='Search']/following::input)[1]")).sendKeys("INC0010035");
		driver.findElement(By.xpath("(//label[text()='Search']/following::input)[1]")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//td[@class='vt'])[1]/a")).click();
		driver.findElement(By.id("sysverb_delete_bottom")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[@id='ok_button']")).click();
		String msg = driver.findElement(By.xpath("(//table[@role='presentation']//tbody)[2]/tr/td")).getText();
		System.out.println(msg);

	}

}
