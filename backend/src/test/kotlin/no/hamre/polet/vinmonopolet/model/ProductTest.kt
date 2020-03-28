package no.hamre.polet.vinmonopolet.model

import no.hamre.polet.ObjectMapperFactory
import no.hamre.polet.vinmonopolet.TestData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ProductTest{
  private val mapper = ObjectMapperFactory.create()

  @Test
  internal fun `parse signel product should work`() {
    val products = mapper.readValue(TestData.bellsWhisky(), Array<Product>::class.java)
    assertNotNull(products)
    assert(products.size == 1)
  }
}