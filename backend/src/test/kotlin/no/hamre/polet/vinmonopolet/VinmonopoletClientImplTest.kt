package no.hamre.polet.vinmonopolet

import no.hamre.polet.ObjectMapperFactory
import no.hamre.polet.TestUtils
import no.hamre.polet.vinmonopolet.model.Product
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class VinmonopoletClientImplTest {
  private val log = LoggerFactory.getLogger(VinmonopoletClientImplTest::class.java)

  @Test
  @Disabled("Only for manual testing")
  internal fun `call vinmonopolet api`() {
    val client = VinmonopoletClientImpl(
        url = "https://apis.vinmonopolet.no",
        apiKey = "")
    val max = 100
    val start = 0
    log.info("Calling API. start: $start, max: $max")
    val products: BatchData = client.doRequest(start = start, maxResults = max)
    val foundWhisky = products.whiskies.count { "whisky" == it.varetype.toLowerCase() }
    log.info("Number of whiskies Found: $foundWhisky")
  }


  @Test
  internal fun `parse 400 first products`() {
    val mapper = ObjectMapperFactory.create()
    val products = mapper.readValue(TestData.proucts(), Array<Product>::class.java)
    products.filter { it.isWhisky() }
        .forEach { println(it.basic.productLongName) }
  }

  @Test
  fun `read from file`() {
    val bells: String = TestUtils().fileContent("bellsWhisky.json")
    log.info("Read from file: \n$bells")
    assert(bells.contains("Bell's"))
  }

  @Test
  internal fun `recognize whisky`() {
    val products = ObjectMapperFactory.create().readValue(TestData.bellsWhisky(), Array<Product>::class.java)
    assert(products.any { it.isWhisky() }) { "Should contain a whisky" }
  }

  @Test
  internal fun `default metadata`() {
    val meta = MetaData()
    assert(meta.start == 0)
    assert(meta.max == 400)
    assertFalse(meta.isDone)
    assert(meta.totalCount == Int.MAX_VALUE)

  }
}

object TestData {
  fun bellsWhisky() = TestUtils().fileContent("bellsWhisky.json")
  fun proucts() = TestUtils().fileContent("products.json")
}