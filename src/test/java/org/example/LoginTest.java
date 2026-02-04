package org.example;

import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest {

    private WebDriver driver;

    @Step("Инициализация веб-драйвера")
    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Step("Завершение работы веб-драйвера")
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Test case: Успешный логин на saucedemo.com
     */
    @Test
    @Description("Проверка возможности входа с валидными данными.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Авторизация пользователей")
    public void testSuccessfulLogin() {
        String baseUrl = "https://www.saucedemo.com";

        driver.get(baseUrl);

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");

        driver.findElement(By.id("login-button")).click();

        //ожидание на всякий случай
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/inventory.html"));

        String currentURL = driver.getCurrentUrl();
        org.junit.jupiter.api.Assertions.assertEquals(
                "https://www.saucedemo.com/inventory.html",
                currentURL,
                "Пользователь должен попасть на страницу инвентаря."
        );
    }
}
