package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class InventoryPage {
    private WebDriver driver;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(id = "shopping_cart_container")
    private WebElement shoppingCart;

    @FindBy(id = "menu_button_container")
    private WebElement menuButtonContainer;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isFullyLoaded() {
        return shoppingCart.isDisplayed() && !itemNames.isEmpty() && menuButtonContainer.isDisplayed();
    }
}
