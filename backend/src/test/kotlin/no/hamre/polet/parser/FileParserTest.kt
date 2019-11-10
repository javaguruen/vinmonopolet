package no.hamre.polet.parser

import no.hamre.polet.dao.H2LiquibaseDataSourceFactory
import no.hamre.polet.dao.PoletDao
import no.hamre.polet.modell.ProductLineHelper
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.charset.Charset

class FileParserTest {

  @Test
  fun `Parse file to lines`() {
    val filename = "produkter.csv"
    val readmeText = File("src/main/resources/$filename").readLines(Charset.forName("windows-1252"))
    val p = ProductLineHelper.create(readmeText[1].split(";"))
    val ds = H2LiquibaseDataSourceFactory.createDataSource("polet")
    val dao = PoletDao(ds)
    dao.insertProduct(p)
  }
}
