package com.automation.framework.web;


import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class WebCommand {

	private WebDriver driver;

	public WebCommand(WebDriver driver) {
		this.driver=driver;
	}

	public WebElement getWebElement(PageElement pageElement){
		WebElement webElement=null;

		switch (pageElement.locatorType) {
		case XPATH:
			webElement=driver.findElement(By.xpath(pageElement.getValue()));
			break;
		case ID:
			webElement=driver.findElement(By.id(pageElement.getValue()));
			break;
		case NAME:
			webElement=driver.findElement(By.name(pageElement.getValue()));
			break;
		case CLASS_NAME:
			webElement=driver.findElement(By.className(pageElement.getValue()));
			break;
		case LINK_TEXT:
			webElement=driver.findElement(By.linkText(pageElement.getValue()));
			break;
		case PARTIAL_LINK_TEXT:
			webElement=driver.findElement(By.partialLinkText(pageElement.getValue()));
			break;
		case CSS_SELECTOR:
			webElement=driver.findElement(By.cssSelector(pageElement.getValue()));
			break;
		case TAG_NAME:
			webElement=driver.findElement(By.tagName(pageElement.getValue()));
			break;
		default:
			webElement=driver.findElement(By.xpath(pageElement.getValue()));
		}
		return webElement;
	}

	public void click(PageElement pageElement){
		try{
			getWebElement(pageElement).click();
		}catch(StaleElementReferenceException staleException){
			getWebElement(pageElement).click();
		}
		catch(ElementNotVisibleException elemNotVisibleExc){
			Assert.fail(pageElement.getName()+" was not visible to click");
		}		
		catch(TimeoutException timeoutExc){			
			//Dont do anything for timeout exception. Just continue with remaining test
		}
	}

	public void type(PageElement pageElement,String value){
		getWebElement(pageElement).sendKeys(value);
	}

	public void goTo(String url){
		driver.get(url);
	}

	public void maximizeWindow(){
		driver.manage().window().maximize();
	}

	public void selectUsingIndex(PageElement pageElement, int index) {
		WebElement webElement=getWebElement(pageElement);
		Select select = new Select(webElement);
		select.selectByIndex(index);
	}

	public void switchToFrame(PageElement pageElement) {
		WebElement webElement=getWebElement(pageElement);
		driver.switchTo().frame(webElement);
	}

	public String getText(PageElement pageElement) {
		String text="";
		try{
			text=getWebElement(pageElement).getText();	
		}catch (Exception e) {
			Assert.fail("Couldn't get text for locator"+pageElement);
		}	
		return text;	
	}

	public  WebElement scrollToElementAndClick(PageElement pageElement) throws Exception{
		WebElement element = null;
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
		try{
			element = getWebElement(pageElement);
			int y = element.getLocation().getY();
			executor.executeScript("window.scrollTo(0," + (y-20) + ");");
			executor.executeScript("arguments[0].click();", element);
		}
		catch(Exception e){
		}
		return element;
	}
	public void waitForElementToVisible(PageElement pageElement,long interval) throws Exception {
		long counter = 0;
		while(isElementDisplayed(pageElement)){
			counter++;
			if(!isElementDisplayed(pageElement) || counter == interval)
				break;
		}
		Thread.sleep(interval*100);
	}
	public void waitFor(long milliseconds){   
		try{
			Thread.sleep(milliseconds);
		}catch(Exception e){
			System.out.println("Could not wait for "+milliseconds/1000+" Seconds!!");              
		}
	}
	public void scrollTo(int x,int y){
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		System.out.println("window.scrollTo("+x +"," +y+");");
		try{
			executor.executeScript("window.scrollTo("+x +"," +y+");");
		}catch(Exception e){

		}
	}
	public boolean isElementDisplayed(PageElement pageElement) throws Exception{
		try{
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			WebElement element = getWebElement(pageElement);
			driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
			if (element.isDisplayed());
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
}