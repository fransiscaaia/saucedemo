package com.example.saucedemo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductPage {

    @FindBy(id = "shopping_cart_container")
    public WebElement shoppingCart;

    @FindBy(xpath = "//span[@class='shopping_cart_badge']")
    public WebElement shoppingCartBadge;

    @FindBy(xpath = "//*[@data-test='product_sort_container']")
    public WebElement productFilter;

    @FindBy(xpath = "//*[@class='inventory_item_name']")
    public WebElement productName;

    public ProductPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
