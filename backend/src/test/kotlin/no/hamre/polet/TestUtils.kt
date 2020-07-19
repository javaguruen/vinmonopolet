package no.hamre.polet

import no.hamre.polet.vinmonopolet.model.Product
import java.io.FileNotFoundException
import java.net.URL

class TestUtils {
  val tmpFileName = "\\testdata\\bellsWhisky.json"

  fun fromFile() {
    val mapper = ObjectMapperFactory.create()
    tmpFileName.asResource {
      val products = mapper.readValue(it, Array<Product>::class.java)
      println(products)
    }

  }

  fun contentFromFile(): String {
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
    return javaClass.classLoader.getResource("testdata/$filename")?.readText()
        ?: throw FileNotFoundException("testdata/$filename")
  }
}