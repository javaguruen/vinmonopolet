package no.hamre.polet

import no.hamre.polet.vinmonopolet.model.Product
import java.io.InputStream
import java.net.URL

class TestUtils {
  val tmpFileName = "\\testdata\\bellsWhisky.json"

  fun fromFile(filename: String): Unit {
    val mapper = ObjectMapperFactory.create()
    tmpFileName.asResource {
      val products = mapper.readValue(it, Array<Product>::class.java)
      println(products)
    }

  }

  fun contentFromFile(filename: String): String {
    val mapper = ObjectMapperFactory.create()
    var products = ""
    tmpFileName.asResource {
      products = mapper.readValue(it, String::class.java)
    }
    return products
  }

  private fun String.asResource(work: (URL) -> Unit) {
    val content = this.javaClass::class.java.getResource(this)
    work(content)
  }

  fun fileContent(filename: String): String {
    val content = javaClass.classLoader.getResource("testdata/$filename").readText()
    return content
  }
}