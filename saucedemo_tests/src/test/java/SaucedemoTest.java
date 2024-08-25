import junit.framework.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;
public class SaucedemoTest {

    private static WebDriver driver;
    @BeforeClass
    public static void setup(){  //вход на сайт
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.saucedemo.com");
    }

    @Test
    public void authorization(){ //вход
        driver.findElement(By.id("user-name")).sendKeys("standard_user");  //standard_user
        driver.findElement(By.id("password")).sendKeys("secret_sauce");  //secret_sauce
        driver.findElement(By.className("btn_action")).click();

        try {
            Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
            System.out.println("Удалось авторизоваться!");
        }catch(AssertionError e){
            System.out.println("Что-то пошло не так...");
            throw e;
        }
    }

    @Test
    public void adding_products() //добавление продукта
    {
        authorization();
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        try {
        Assert.assertTrue(driver.findElements(By.className("cart_item")).size()!= 0);
        System.out.println("Удалось добавить продукт в корзину!");
        }catch(AssertionError e){
            System.out.println("Что-то пошло не так...");
            throw e;
        }
    }

    @Test
    public void removing_products() //удаление продукта
    {
        adding_products();
        driver.findElement(By.className("btn_secondary")).click();
        try {
            Assert.assertFalse(driver.findElements(By.className("cart_item")).size()!= 0);
            System.out.println("Убираем из корзины...");
            Assert.assertTrue(driver.findElements(By.className("removed_cart_item")).size()!= 0);
            System.out.println("Удалось убрать продукт из корзины!");
        }catch(AssertionError e){
            System.out.println("Что-то пошло не так...");
            throw e;
        }
    }

    @Test
    public void ordering() //заказ
    {
        adding_products();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.className("btn_action")).click();
        driver.findElement(By.id("first-name")).sendKeys("Тест");
        driver.findElement(By.id("last-name")).sendKeys("Тестович");
        driver.findElement(By.id("postal-code")).sendKeys("011037");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();

        try {
        Assert.assertTrue(driver.getPageSource().contains("Checkout: Complete!"));
        System.out.println("Удалось сделать заказ!");
        }catch(AssertionError e){
            System.out.println("Что-то пошло не так...");
            throw e;
        }
    }

    @AfterClass
    public static void tearDown() { //закрытие браузера
        driver.quit();
    }
}
