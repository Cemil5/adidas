package com.demoblaze.tests;

import com.demoblaze.utilities.WebDriverFactory;
import com.github.javafaker.Faker;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class WebFE_Automation {

    WebDriver driver= WebDriverFactory.getDriver("chrome");
    WebDriverWait wait = new WebDriverWait(driver,10);

    @BeforeClass
    public void setUp(){
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    @AfterClass
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }

    @Test
    public void test() throws InterruptedException {
        driver.get("https://www.demoblaze.com/index.html");

        String [] purchaseList = {"Sony vaio i5", "Dell i7 8gb"};
        int expectedAmount =0;

        for (String item : purchaseList) {
            expectedAmount += addCart("Laptops", item);
            driver.findElement(By.xpath("//a[@href='index.html']")).click(); // go to home page
        }

        driver.findElement(By.id("cartur")).click();

        expectedAmount -= deleteItem(purchaseList[1]);

      //  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
      //  wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Delete")));
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[.='Place Order']")).click();

        customerFormFill();

        Integer actualAmount = getLogIDAndAmount();
        String actualPrice = ""+actualAmount;
        String expectedPrice = ""+expectedAmount;

        Assert.assertEquals(actualPrice,expectedPrice,"purchase amount is not as expected");

        driver.findElement(By.xpath("//button[.='OK']"));
    }

    private int deleteItem(String item) {
        String itemPath = "//td[.='" + item + "']";
        String itemPricePath = itemPath + "/../td[3]";
        String deleteLinkPath = itemPath + "/../td[4]/a";
        String itemPrice = driver.findElement(By.xpath(itemPricePath)).getText();

        driver.findElement(By.xpath(deleteLinkPath)).click();
        return Integer.parseInt(itemPrice);
    }

    private Integer getLogIDAndAmount() {
        String logPurchase = driver.findElement(By.cssSelector(".lead.text-muted")).getText();

        String logID = logPurchase.split("Amount")[0];
        System.out.println("logID = " + logID);

        String actualPrice = logPurchase.substring(logPurchase.indexOf("Amount")+8, logPurchase.indexOf(" USD"));
        return Integer.parseInt(actualPrice);
    }

    private int addCart(String category, String item) {

        driver.findElement(By.xpath("//a[text()='"+ category +"']")).click();
        String itemNamePath = "//a[text()='"+ item +"']";
        String itemPricePath = itemNamePath + "/../../h5";
        String itemPrice = driver.findElement(By.xpath(itemPricePath)).getText();
        itemPrice = itemPrice.substring(1);

        driver.findElement(By.xpath(itemNamePath)).click();
        driver.findElement(By.xpath("//a[text()='Add to cart']")).click();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();
        return Integer.parseInt(itemPrice);
    }

    private void customerFormFill() {

        Faker faker = new Faker();

    //    wait.until(ExpectedConditions.elementToBeClickable(By.id("year")));

        driver.findElement(By.xpath("//input[@id='name']")).sendKeys(faker.name().fullName());
        driver.findElement(By.xpath("//input[@id='country']")).sendKeys(faker.country().name());
        driver.findElement(By.xpath("//input[@id='city']")).sendKeys(faker.country().capital());
        driver.findElement(By.xpath("//input[@id='card']")).sendKeys(faker.business().creditCardNumber());
        driver.findElement(By.xpath("//input[@id='month']")).sendKeys("04");
        driver.findElement(By.xpath("//input[@id='year']")).sendKeys("2024");
        driver.findElement(By.xpath("//button[text()='Purchase']")).click();
    }
}
