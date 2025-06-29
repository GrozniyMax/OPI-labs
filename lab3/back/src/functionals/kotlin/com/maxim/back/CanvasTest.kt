package com.maxim.back

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.interactions.Action
import org.openqa.selenium.interactions.Actions
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CanvasTest {


    @Test
    @DisplayName("Клик по графику добавляет строчку id=8")
    fun test8() {
        val before = driver.findElements(By.tagName("tr")).size
        val canvas = driver.findElement(By.id("canvas"))
        Actions(driver).moveToElement(canvas).click().perform()
        Thread.sleep(1000)
        val after = driver.findElements(By.tagName("tr")).size

        assertNotEquals(before, after)
    }

    @Test
    @DisplayName("Добавление зеленой точки id=9")
    fun test9() {
        val before = driver.findElements(By.tagName("td")).filter { it.getAttribute("class") == "success" }.size
        val canvas = driver.findElement(By.id("canvas"))

        Actions(driver)
            .moveToElement(canvas, 10, 10)
            .click().perform()

        Thread.sleep(1000)
        val actual = driver.findElements(By.tagName("td")).filter { it.getAttribute("class") == "success" }.size

        assertNotEquals(before, actual)

    }

    @Test
    @DisplayName("добавление красной точки, id=10")
    fun test10() {
        val canvas = driver.findElement(By.tagName("canvas"))
        Actions(driver)
        .moveToElement(canvas, canvas.size.width/2, canvas.size.height/2)
            .click().perform()

        Thread.sleep(5000)

        val actual = driver.findElements(By.tagName("td")).filter { it.getAttribute("class") == "success" }

        assertEquals(0, actual.size)
    }

    @Test
    @DisplayName("Ввод корректных данных в x id=11")
    fun test11() {
        driver.findElement(By.id("x-input-field")).sendKeys("1")

        val actualSize = driver.findElements(By.tagName("div")).filter { it.getAttribute("class") == "text-field-error-message" }.size
        assertEquals(0, actualSize)
    }

    @Test
    @DisplayName("ВВод некорректных данных в x id=12")
    fun test12() {
        driver.findElement(By.id("x-input-field")).sendKeys("7")
        val actualSize = driver.findElements(By.tagName("div")).filter { it.getAttribute("class") == "text-field-error-message" }.size
        assertEquals(1, actualSize)
    }

    @Test
    @DisplayName("Проверка кнопки почистить точки, id=13")
    fun test13() {
        Actions(driver)
            .click(driver.findElement(By.id("clear")))
            .perform()
        val tableSize = driver.findElements(By.tagName("tr")).size
        Thread.sleep(1000)

        assertEquals(2, tableSize)
    }







    companion object {
        private lateinit var driver: WebDriver

        @JvmStatic
        @BeforeAll
        fun setUp(): Unit {
            // Путь к chromedriver, если он не в системном PATH
            System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver")

            val options = ChromeOptions()
            // Без headless — чтобы GUI был виден
            options.addArguments("--start-maximized")

            driver = ChromeDriver(options)
            driver.get("http://localhost:5173")

            register(User("totalNew", "password", true), driver)

        }

        fun register(user: User, driver: WebDriver) {
            Thread.sleep(5000)
            driver.findElements(By.tagName("button")).find { x -> x.text == "Еще не зарегистрирован" }?.click()

            driver.findElement(By.id("login")).sendKeys(user.login)
            driver.findElement(By.id("password")).sendKeys(user.password)
            driver.findElement(By.id("repeatedPassword")).sendKeys(user.password)
            Thread.sleep(5000)
            driver.findElements(By.tagName("button"))
                .find { webElement -> webElement.text == "Зарегистрироваться" }
                ?.click()
            Thread.sleep(5000)
            user.exists = true
        }

        @JvmStatic
        @AfterAll
        fun tearDown(): Unit {
            driver.quit()
        }
    }
}
