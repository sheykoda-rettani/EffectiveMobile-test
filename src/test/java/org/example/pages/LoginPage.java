package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {

    private WebDriver driver;

    @FindBy(id="user-name")
    private WebElement usernameField;

    @FindBy(id="password")
    private WebElement passwordField;

    @FindBy(id="login-button")
    private WebElement loginButton;

    @FindBy(css=".error-message-container")
    private WebElement errorMessageContainer;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterUsername(String username) {
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public boolean isErrorMessageDisplayed() {
        return errorMessageContainer.isDisplayed();
    }

    public String getErrorMessage() {
        return errorMessageContainer.getText();
    }

    public void waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(loginButton));
    }
}
