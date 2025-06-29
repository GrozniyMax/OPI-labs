package com.maxim.back

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.openqa.selenium.By
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class RegistrationTest {

    private lateinit var driver: WebDriver

    private lateinit var toRegister: User

    private lateinit var nonExisting: User

    @BeforeEach
    fun setUp() {
        // Путь к chromedriver, если он не в системном PATH
        System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver")

        val options = ChromeOptions()
        // Без headless — чтобы GUI был виден
        options.addArguments("--start-maximized")

        driver = ChromeDriver(options)
        driver.get("http://localhost:5173/")

        toRegister = User.users[0]
        nonExisting = User.users[1]
    }

    @AfterEach
    fun tearDown() {
        driver.quit()
    }


    @Test
    @DisplayName("Регистрируем еще не существующего пользователя, id=4")
    fun test4() {
        // переход к регистрации
        driver.findElements(By.tagName("button")).find { x -> x.text == "Еще не зарегистрирован" }?.click()

        driver.fill(toRegister)

        driver.register()

        try {
            val wait = WebDriverWait(driver, Duration.ofSeconds(5))
            val header = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h2")))
            assertEquals("Добро пожаловать, ${toRegister.login}!", header.text)
            toRegister.exists = true
        } catch (e: TimeoutException) {
            throw AssertionError(e)
        }
    }

    @Test
    @DisplayName("Повторная регистрация пользователя, id=5")
    fun test5() {
        // переход к регистрации
        driver.findElements(By.tagName("button")).find { x -> x.text == "Еще не зарегистрирован" }?.click()

        if (!toRegister.exists) {
            throw AssertionError("User doesn't exists still")
        }
        try {
            driver.fill(toRegister)
            driver.register()
            val wait = WebDriverWait(driver, Duration.ofSeconds(5))
            val errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("p")))
            assertEquals("Пользователь уже существует", errorMessage.text)
        } catch (e: TimeoutException) {
            throw AssertionError(e)
        }

    }

    @Test
    @DisplayName("Вход уже существующего пользователя, id=6")
    fun test6() {
        try {
            driver.login(toRegister)
            driver.login()
            val wait = WebDriverWait(driver, Duration.ofSeconds(5))
            val element = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h2")))
            kotlin.test.assertEquals("Добро пожаловать, ${toRegister.login}!", element.text)
        } catch(e: TimeoutException) {
            throw AssertionError(e)
        }
    }

    @Test
    @DisplayName("Вход еще не существующего пользователя, id=7")
    fun test7() {
        try {
            driver.login(nonExisting)
            driver.login()
            val wait = WebDriverWait(driver, Duration.ofSeconds(5))
            val errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("p")))
            Assertions.assertEquals("Ошибка авторизации", errorMessage.text)
        } catch(e: TimeoutException) {
            throw AssertionError(e)
        }
    }

    fun WebDriver.fillLogin(value: String) {
        this.findElement(By.id("login")).sendKeys(value)
    }

    fun WebDriver.fillPassword(value: String) {
        this.findElement(By.id("password")).sendKeys(value)
    }

    fun WebDriver.fill(user: User) {


        fun WebDriver.fillRepeatPassword(value: String) {
            this.findElement(By.id("repeatedPassword")).sendKeys(value)
        }

        this.fillLogin(user.login)
        this.fillPassword(user.password)
        this.fillRepeatPassword(user.password)
    }

    fun WebDriver.login() {
        this.findElement(By.tagName("button")).click()
    }

    fun WebDriver.login(user: User) {
        this.fillLogin(user.login)
        this.fillPassword(user.password)
    }

    fun WebDriver.register() = this.findElements(By.tagName("button"))
        .find { webElement -> webElement.text == "Зарегистрироваться" }
        ?.click()
}
