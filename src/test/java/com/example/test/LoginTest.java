package com.example.test;

import com.example.saucedemo.LoginPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends MainPageTest {
    @Test
    public void openLogin() {
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.userNameField.isDisplayed());
        assertTrue(loginPage.passwordField.isDisplayed());
        assertTrue(loginPage.loginButton.isDisplayed());
    }

    @Test
    public void loginWithValidCredential() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.userNameField.sendKeys("standard_user");
        loginPage.passwordField.sendKeys("secret_sauce");
        loginPage.loginButton.click();

        assertEquals("Swag Labs", driver.getTitle());
    }

    @Test
    public void loginWithInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.userNameField.sendKeys("standard_user");
        loginPage.passwordField.sendKeys("abcdefg");
        loginPage.loginButton.click();

        assertTrue(driver.findElement(By.xpath(
                        "//*[text()='Epic sadface: Username and password do not match any user in this service']"))
                .isDisplayed());
    }

    @Test
    public void loginWithLockedUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.userNameField.sendKeys("locked_out_user");
        loginPage.passwordField.sendKeys("secret_sauce");
        loginPage.loginButton.click();

        assertTrue(driver.findElement(By.xpath(
                        "//*[text()='Epic sadface: Sorry, this user has been locked out.']"))
                .isDisplayed());
    }

    @Test
    public void loginWithInvalidUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.userNameField.sendKeys("user_first");
        loginPage.passwordField.sendKeys("secret_sauce");
        loginPage.loginButton.click();

        assertTrue(driver.findElement(By.xpath(
                        "//*[text()='Epic sadface: Username and password do not match any user in this service']"))
                .isDisplayed());
    }
}
