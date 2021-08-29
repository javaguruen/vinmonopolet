package no.hamre.polet.vinmonopolet

import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxDriverService
import org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URL
import java.time.Duration
import kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.unit





class ScrapeTest {

  @Test
  internal fun `scrape one product`() {
//    val html = URL("https://www.vinmonopolet.no/Land/Portugal/Graham%27s-Late-Bottled-Vintage-2015/p/2002/p/2002")

    val html = URL("https://www.vinmonopolet.no/p/2002")
      .openConnection().inputStream.bufferedReader().readText()
    println(
      html
    )
  }

  @Test
  internal fun `html unit scraping`() {
    val driver: org.openqa.selenium.WebDriver = ChromeDriver()
    val wait = WebDriverWait(driver, Duration.ofSeconds(10))
    try {
      driver.get("https://www.vinmonopolet.no/p/2002")
      val webElement = driver.findElement(By.id("js-product"))
      val price = driver.findElement(By.className("product__price")).text
      val facts = driver.findElement(By.className("product__tab-list"))
      val lis: List<WebElement> = facts.findElements(By.tagName("li"))
      println("First: ${lis.first().text}  first")
      lis.forEach { li ->
        println(li.text)
      }
      val firstResult: WebElement = wait.until(presenceOfElementLocated(By.cssSelector("h3")))
      System.out.println(firstResult.getAttribute("textContent"))
    } finally {
      driver.quit()
    }  }
}

