package com.example.test;

import com.example.saucedemo.CartPage;
import com.example.saucedemo.LoginPage;
import com.example.saucedemo.ProductPage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MainPageTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductPage productPage;

    private CartPage cartPage;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");

        loginPage = new LoginPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void openLogin() {
        assertTrue(loginPage.userNameField.isDisplayed());
        assertTrue(loginPage.passwordField.isDisplayed());
        assertTrue(loginPage.loginButton.isDisplayed());
    }

    @Test
    public void loginWithValidCredential() {
        loginPage.userNameField.sendKeys("standard_user");
        loginPage.passwordField.sendKeys("secret_sauce");
        loginPage.loginButton.click();

        assertEquals("Swag Labs", driver.getTitle());
    }

    @Test
    public void loginWithInvalidPassword() {
        loginPage.userNameField.sendKeys("standard_user");
        loginPage.passwordField.sendKeys("abcdefg");
        loginPage.loginButton.click();

        assertTrue(driver.findElement(By.xpath(
                        "//*[text()='Epic sadface: Username and password do not match any user in this service']"))
                .isDisplayed());
    }

    @Test
    public void loginWithInvalidUsername() {
        loginPage.userNameField.sendKeys("user_first");
        loginPage.passwordField.sendKeys("secret_sauce");
        loginPage.loginButton.click();

        assertTrue(driver.findElement(By.xpath(
                        "//*[text()='Epic sadface: Username and password do not match any user in this service']"))
                .isDisplayed());
    }

    @Test
    public void addToCart() {
        loginWithValidCredential();

        productPage = new ProductPage(driver);

        assertTrue(productPage.shoppingCart.isDisplayed());
        assertTrue(productPage.productFilter.isDisplayed());
        assertTrue(productPage.productName.isDisplayed());

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        WebElement addToCartButton2 = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));

        addToCartButton.click();
        assertTrue(productPage.shoppingCartBadge.isDisplayed());
        assertEquals(productPage.shoppingCartBadge.getText(), "1");

        addToCartButton2.click();
        assertEquals(productPage.shoppingCartBadge.getText(), "2");
    }

    @Test
    public void removeCart() {
        loginWithValidCredential();

        productPage = new ProductPage(driver);

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));

        addToCartButton.click();
        assertTrue(productPage.shoppingCartBadge.isDisplayed());
        assertEquals(productPage.shoppingCartBadge.getText(), "1");

        WebElement removeButton = driver.findElement(By.id("remove-sauce-labs-backpack"));
        assertTrue(removeButton.isDisplayed());

        removeButton.click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        assertTrue(driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).isDisplayed());
    }

    @Test
    public void checkoutCart() {
        addToCart();

        productPage.shoppingCart.click();

        cartPage = new CartPage(driver);

        assertTrue(cartPage.shoppingCart.isDisplayed());
        assertTrue(cartPage.shoppingCartBadge.isDisplayed());
        assertTrue(cartPage.cartItem.isDisplayed());
        assertTrue(cartPage.continueShoppingButton.isDisplayed());
        assertTrue(cartPage.checkoutButton.isDisplayed());
        assertTrue(driver.findElement(By.xpath("//button[text()='Remove']")).isDisplayed());

        cartPage.checkoutButton.click();

        assertTrue(cartPage.firstNameField.isDisplayed());
        assertTrue(cartPage.lastNameField.isDisplayed());
        assertTrue(cartPage.postalCodeField.isDisplayed());

        //continue checkout without input any field
        cartPage.continueButton.click();
        assertTrue(driver.findElement(By.xpath(
                "//*[text()='Error: First Name is required']")).isDisplayed());

        //continue checkout with only input firstname
        cartPage.firstNameField.sendKeys("Testing");
        cartPage.continueButton.click();
        assertTrue(driver.findElement(By.xpath(
                "//*[text()='Error: Last Name is required']")).isDisplayed());

        //continue checkout with doesn't input postal code
        cartPage.lastNameField.sendKeys("Aja");
        cartPage.continueButton.click();
        assertTrue(driver.findElement(By.xpath(
                "//*[text()='Error: Postal Code is required']")).isDisplayed());

        //continue checkout with all fields
        cartPage.postalCodeField.sendKeys("20231");
        cartPage.continueButton.click();
        assertTrue(driver.findElement(By.xpath("//*[@class='summary_info']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[@class='summary_info_label']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[@class='summary_value_label']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[@class='summary_subtotal_label']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[contains(@class, 'summary_total_label')]")).isDisplayed());
        assertTrue(cartPage.finishButton.isDisplayed());

        cartPage.finishButton.click();

        WebElement backToHomeButton = driver.findElement(By.id("back-to-products"));

        assertTrue(driver.findElement(By.xpath("//*[text()='Thank you for your order!']")).isDisplayed());
        assertTrue(backToHomeButton.isDisplayed());

        //back to home
        backToHomeButton.click();
        assertTrue(productPage.productName.isDisplayed());
    }
}
