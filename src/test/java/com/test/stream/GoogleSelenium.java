package com.test.stream;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class GoogleSelenium {

	private WebDriver driver;
	
	@Before
	public void setUp() {
		
		//La version de chrome debe ser igual a la instalada
		//System.setProperty("webdriver.chrome.driver", "./src/test/resources/googledriver/chromedriver_win32/chromedriver.exe");
		//driver = new ChromeDriver();
		
		WebDriverManager.chromedriver().setup(); // <-- This handles the download and setup
		driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		driver.get("https://dle.rae.es/y");
	}
	
	@Test
	public void testGoogle() {
		WebElement serchbox = driver.findElement(By.name("w"));
		serchbox.clear();
		serchbox.sendKeys("desatar");
		serchbox.submit();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		System.out.println(driver.getTitle());
		
		assertEquals("desatar | Definición | Diccionario de la lengua española | RAE - ASALE", driver.getTitle());

	}
	
	
}
