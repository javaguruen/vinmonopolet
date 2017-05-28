package no.hamre.polet.parser

import no.hamre.polet.dao.{H2LiquibaseDataSourceFactory, PoletDao}
import no.hamre.polet.modell.ProductLine
import org.scalatest.FunSuite

import scala.io.Source

class FileParserTest extends FunSuite {

  test("Parse file to lines") {
    val filename = "produkter.csv"
    val readmeText =
      Source.fromFile(s"src/main/resources/$filename", "windows-1252").getLines.toList
    val p = ProductLine(readmeText.tail.head.split(";"))
    val ds = H2LiquibaseDataSourceFactory.createDataSource("polet")
    val dao = new PoletDao(ds)
    dao.insertProduct(p)
  }
}
