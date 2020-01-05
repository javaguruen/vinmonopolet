package no.hamre.polet.vinmonopolet

import no.hamre.polet.ObjectMapperFactory
import no.hamre.polet.TestUtils
import no.hamre.polet.vinmonopolet.model.Product
import org.glassfish.jersey.client.JerseyClientBuilder
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class VinmonopoletClientImplTest{
  @Test
  @Disabled("Only for manual testing")
  internal fun `call vinmonopolet api`() {
    val client = VinmonopoletClientImpl(
        url = "https://apis.vinmonopolet.no",
        apiKey = "removed",
        jerseyClient = JerseyClientBuilder().build())
    val max = 1000
    var start = 10
    var foundWhisky = false
    while (!foundWhisky) {
      println("Requesting products. Start = $start, max=$max")
      val products = client.doRequest(start = start, maxResults = max)
      println(products)
      foundWhisky = products.any { "whisky" == it.classification.subProductTypeName }
      start += max
    }
  }

  @Test
  internal fun `parse 400 first products`() {
    val mapper = ObjectMapperFactory.create()
    val products = mapper.readValue(TestData.products, Array<Product>::class.java)
    products.filter{ it.isWhisky() }
        .forEach { println(it.basic.productLongName) }
  }

  @Test
  fun `read from file`() {
    TestUtils().fileContent("")
  }

  @Test
  internal fun `recognize whisky`() {
    val products = ObjectMapperFactory.create().readValue(TestData.bellsWhisky, Array<Product>::class.java)
    assert( products.any { it.isWhisky() }){"Should contain a whisky"}
  }
}

object TestData{
  const val bellsWhisky = """"""

  const val products = """"""
}