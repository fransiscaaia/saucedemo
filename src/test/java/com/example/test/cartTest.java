package com.example.test;

import com.example.saucedemo.CartPage;
import com.example.saucedemo.LoginPage;
import com.example.saucedemo.ProductPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class cartTest extends MainPageTest {

    private ProductPage productPage;

    private CartPage cartPage;
    LoginTest loginTest = new LoginTest();

    @Test
    public void addToCart() {
        loginTest.loginWithValidCredential();

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
        loginTest.loginWithValidCredential();

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
        LoginPage loginPage = new LoginPage(driver);

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
        cartPage.firstNameField.sendKeys("dummy");
        cartPage.continueButton.click();
        assertTrue(driver.findElement(By.xpath(
                "//*[text()='Error: Last Name is required']")).isDisplayed());

        //continue checkout with doesn't input postal code
        cartPage.lastNameField.sendKeys("user");
        cartPage.continueButton.click();
        assertTrue(driver.findElement(By.xpath(
                "//*[text()='Error: Postal Code is required']")).isDisplayed());

        //continue checkout with all fields
        cartPage.postalCodeField.sendKeys("28654");
        cartPage.continueButton.click();
        assertTrue(driver.findElement(By.className("cart_list")).getText().contains("Sauce Labs Backpack"));
        assertTrue(driver.findElement(By.className("cart_list")).getText().contains("Sauce Labs Bike Light"));
        assertTrue(driver.findElement(By.xpath("//*[@class='summary_info']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[@class='summary_info_label']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[@class='summary_value_label']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[@class='summary_subtotal_label']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[contains(@class, 'summary_total_label')]")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//*[contains(@class, 'summary_total_label')]")).getText().contains("43.18"));
        assertTrue(cartPage.finishButton.isDisplayed());

        cartPage.finishButton.click();

        WebElement backToHomeButton = driver.findElement(By.id("back-to-products"));

        assertTrue(driver.findElement(By.xpath("//*[text()='Thank you for your order!']")).isDisplayed());
        assertTrue(backToHomeButton.isDisplayed());

        //back to home
        backToHomeButton.click();
        assertTrue(productPage.productName.isDisplayed());
        assertTrue(productPage.hamburgerMenu.isDisplayed());

        //logout
        productPage.hamburgerMenu.click();
        productPage.logout.click();

        //loginpage
        assertTrue(loginPage.userNameField.isDisplayed());
        assertTrue(loginPage.passwordField.isDisplayed());
        assertTrue(loginPage.loginButton.isDisplayed());
    }
}
