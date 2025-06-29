package com.maxim.back

import org.junit.jupiter.api.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.assertTrue


class ClockTests {

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

    @Test
    @DisplayName("Проверка совпадения времени, id=1")
    fun test1() {

        val expectedTime = LocalTime.now()
        val actualTime = driver.getTime()

        assertTrue { Duration.between(actualTime, expectedTime).abs() <= Duration.ofSeconds(1) }
    }


    @Test
    @DisplayName("Проверка того, что количество секунд увеличивается, id=2")
    fun test2() {
        val timeOnStart = driver.getTime()
        Thread.sleep(1000)
        val timeOnFinish = driver.getTime()

        assertTrue(Duration.between(timeOnStart, timeOnFinish) > Duration.ZERO)
    }

    @Test
    @DisplayName("Проверка того, что часы идут корректно, id=3")
    fun test3() {
        val timeOnStart = driver.getTime()
        Thread.sleep(2000)
        val timeOnFinish = driver.getTime()

        val duration = Duration.between(timeOnStart, timeOnFinish)

        assertTrue { (Duration.ofSeconds(2) <= duration) && (duration < Duration.ofMillis(2100)) }

    }

    @AfterEach
    fun tearDown() {
        driver.quit()
    }

    private fun WebDriver.getElement(): String = this.findElement(By.tagName("header"))
        .findElement(By.tagName("span")).text

    private fun String.toLocalTime(): LocalTime = this.trim()
        .substringAfter("Текущее время: ").split(':')
        .let { LocalTime.of(it[0].toInt(), it[1].toInt(), it[2].toInt()) }

    private fun WebDriver.getTime(): LocalTime = this.getElement().toLocalTime()



}


