package com.maxim.back

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import kotlin.test.assertEquals

class SwitchTest {

    private lateinit var driver: WebDriver

    @BeforeEach
    fun setUp() {
        // Путь к chromedriver, если он не в системном PATH
        System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver")

        val options = ChromeOptions()
        // Без headless — чтобы GUI был виден
        options.addArguments("--start-maximized")

        driver = ChromeDriver(options)
        driver.get("http://localhost:5173/")
    }

    @AfterEach
    fun tearDown() {
        driver.quit()
    }

    @Test
    @DisplayName("Смена на регистрацию, id=14")
    fun test14() {
        driver.registerSwitch()
        assertEquals("Регистрация", driver.getHeader())
    }

    @Test
    @DisplayName("Смена на вход, id=15")
    fun test15() {
        driver.registerSwitch()
        driver.findElements(By.tagName("button")).filter { it.text == "Уже есть аккаунт"}.get(0).click()
        assertEquals("Вход", driver.getHeader())
    }

    fun WebDriver.registerSwitch() {
        driver.findElements(By.tagName("button")).filter { it.text == "Еще не зарегистрирован"}.get(0).click()
    }

    fun WebDriver.getHeader():String =
        this.findElement(By.tagName("h1")).text

}
