package com.demoblaze.tests;

import com.demoblaze.utilities.WebDriverFactory;
import com.github.javafaker.Faker;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class draft {

    WebDriver driver;

    @BeforeClass
    public void setUp(){
        driver = WebDriverFactory.getDriver("chrome");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    @AfterClass
    public void tearDown(){
        driver.close();
    }

    @Test
    public void test() throws InterruptedException {
        driver.get("https://www.demoblaze.com/index.html");

        String [] purchaseList = {"Sony vaio i5", "Dell i7 8gb"};

        addCart("Laptops", purchaseList[0]);

        driver.findElement(By.xpath("//a[@href='index.html']")).click();

        addCart("Laptops", purchaseList[1]);

        driver.findElement(By.id("cartur")).click();

        // delete an item
        WebElement deleteItem = driver.findElement(By.xpath("//td[.='" + purchaseList[1] + "']/../td[4]/a"));
        deleteItem.click();

        //Thread.sleep(3000);
        WebElement placeOrderButton = driver.findElement(By.xpath("//button[.='Place Order']"));

        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.visibilityOf(placeOrderButton));

        Thread.sleep(3000);
        placeOrderButton = driver.findElement(By.xpath("//button[.='Place Order']"));
        placeOrderButton.click();

        placeHolderFill();

        String logPurchase = driver.findElement(By.cssSelector(".lead.text-muted")).getText();
        System.out.println("logPurchase = " + logPurchase);

        String logID = logPurchase.split("Amount")[0];
        System.out.println("logID = " + logID);
        String actualAmount = logPurchase.substring(logPurchase.indexOf("Amount")+8, logPurchase.indexOf(" USD"));
        System.out.println("actualAmount = " + actualAmount);
  //      double actualPrice = Double.parseDouble(actualAmount);

        String expectedAmount = "790";
        Assert.assertEquals(actualAmount,expectedAmount,"purchase amount is not as expected");

    }

    public void addCart(String category, String item) throws InterruptedException {

        driver.findElement(By.xpath("//a[text()='"+ category +"']")).click();
        driver.findElement(By.xpath("//a[text()='"+ item +"']")).click();
        driver.findElement(By.xpath("//a[text()='Add to cart']")).click();
        driver.findElement(By.xpath("//a[text()='Add to cart']")).getText();
        Thread.sleep(2000);

        Alert alert = driver.switchTo().alert();
        alert.accept();

//        List<WebElement> categoryList = driver.findElements(By.className("list-group"));
//
//        for (WebElement cat : categoryList) {
//            System.out.println(cat.getText());
//        }

    }

    public void placeHolderFill() throws InterruptedException {
        Faker faker = new Faker();
        System.out.println("faker.date().future(3000,60,TimeUnit.DAYS) = " + faker.date().future(3000, 60, TimeUnit.DAYS));

        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement checkYear = driver.findElement(By.xpath("//input[@id='year']"));
        wait.until(ExpectedConditions.visibilityOf(checkYear));

        driver.findElement(By.xpath("//input[@id='name']")).sendKeys(faker.name().fullName());
        driver.findElement(By.xpath("//input[@id='country']")).sendKeys(faker.country().name());
        driver.findElement(By.xpath("//input[@id='city']")).sendKeys(faker.country().capital());
        driver.findElement(By.xpath("//input[@id='card']")).sendKeys(faker.business().creditCardNumber());
        driver.findElement(By.xpath("//input[@id='month']")).sendKeys("04");
        driver.findElement(By.xpath("//input[@id='year']")).sendKeys("2024");
        Thread.sleep(3000);
        driver.findElement(By.xpath("//button[text()='Purchase']")).click();


    }
@Test
    public void testFaker(){
    Faker faker = new Faker();
    System.out.println("faker.date().future(3000,60,TimeUnit) = " + faker.date().future(3000, 60, TimeUnit.DAYS));
    System.out.println("faker.business().creditCardExpiry() = " + faker.business().creditCardExpiry());

    Random rn = new Random();
    System.out.println("rn.nextInt(13) = " + rn.nextInt(13));
    int year = rn.nextInt(10)+2022;
    System.out.println("year = " + year);
}

}
