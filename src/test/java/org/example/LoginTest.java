package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.example.pages.InventoryPage;
import org.example.pages.LoginPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Feature("Авторизация пользователей")
public class LoginTest {

    public static final String CORRECT_PASSWORD = "secret_sauce";
    private WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);
    private LoginPage loginPage;

    @Step("Инициализация веб-драйвера")
    @BeforeEach
    public void setUp() {
        String browser = System.getProperty("browser", "chrome");
        switch (browser) {
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
        }

        String baseUrl = "https://www.saucedemo.com";

        driver.get(baseUrl);
        logger.info("Открытие страницы {}", baseUrl);
        driver.manage().window().maximize();
        logger.info("Запущен веб-драйвер.");

        loginPage = new LoginPage(driver);
        loginPage.waitForPageLoad();
    }


    @Step("Завершение работы веб-драйвера")
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Закрыт веб-драйвер.");
        }
    }

    /**
     * Test case: Успешный логин на saucedemo.com
     */
    @Test
    @Description("Проверка возможности входа с валидными данными.")
    @DisplayName("Успешный логин.")
    @Story("1. Вход стандартного пользователя.")
    @Severity(SeverityLevel.CRITICAL)
    @Order(1)
    public void testSuccessfulLogin() {
        loginAsUser("standard_user", CORRECT_PASSWORD);
        checkLogin();
        logSuccessfulTest();
    }

    @Test
    @Description("Проверка ошибки входа с неверным паролем.")
    @DisplayName("Логин с неверным паролем.")
    @Story("2. Попытка входа с неверным паролем.")
    @Severity(SeverityLevel.NORMAL)
    @Order(2)
    public void testInvalidPassword() {
        loginAsUser("standard_user", "wrong");
        checkErrorPopup("Username and password do not match any user in this service");
        logSuccessfulTest();
    }

    @Test
    @DisplayName("Логин заблокированного пользователя.")
    @Description("Проверка ошибки входа с заблокированным пользователем.")
    @Story("3. Вход заблокированного пользователя.")
    @Severity(SeverityLevel.NORMAL)
    @Order(3)
    public void testLockedOutUser() {
        loginAsUser("locked_out_user", CORRECT_PASSWORD);
        checkErrorPopup("Sorry, this user has been locked out");
        logSuccessfulTest();
    }

    @ParameterizedTest(name = "4.{index}: Вход с полями user-name={0}, password={1}")
    @MethodSource("provideEmptyLoginData")
    @Description("Проверка ошибки входа с пустыми полями")
    @Story("4. Попытка входа с пустыми полями")
    @Severity(SeverityLevel.MINOR)
    @Order(4)
    public void testEmptyFields(String username, String password, String errorMessage) {
        loginAsUser(username, password);
        checkErrorPopup(errorMessage);
        logSuccessfulTest();
    }

    @Test
    @Description("Проверка возможности входа с валидными данными для пользователя с проблемами производительности.")
    @DisplayName("Вход пользователя с проблемами производительности")
    @Story("5. Попытка входа пользователя с проблемами производительности")
    @Severity(SeverityLevel.NORMAL)
    @Order(5)
    public void testSuccessfulLoginPerformanceGlitch() {
        loginAsUser("performance_glitch_user", CORRECT_PASSWORD);
        checkLogin();
        logSuccessfulTest();
    }

    private void logSuccessfulTest() {
        logger.info("Тест пройден успешно.");
    }

    private void loginAsUser(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        logger.info("Введены учетные данне пользователя");
        loginPage.clickLoginButton();
        logger.info("Нажата кнопка входа.");
    }

    private void checkErrorPopup(String expectedErrorMessage) {
        assertTrue(loginPage.isErrorMessageDisplayed(),"Нет сообщения об ошибке");
        assertTrue(loginPage.getErrorMessage().contains(expectedErrorMessage), String.format("В сообщении об ошибке нет текста '%s'", "expectedErrorMessage"));
    }

    private static Arguments[] provideEmptyLoginData() {
        return new Arguments[]{
                Arguments.of("", "", "Username is required"),
                Arguments.of("standard_user", "", "Password is required")
        };
    }

    private void checkLogin() {
        InventoryPage inventoryPage = new InventoryPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> inventoryPage.isFullyLoaded());
        assertTrue(inventoryPage.isFullyLoaded(), "Ошибка: пользователю не удалось войти.");
    }
}
