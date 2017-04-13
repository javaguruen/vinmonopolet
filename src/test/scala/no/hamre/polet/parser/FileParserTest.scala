package no.hamre.polet.parser

import java.nio.{ByteBuffer, CharBuffer}
import java.nio.charset.{Charset, CharsetEncoder}

import io.dropwizard.testing.FixtureHelpers
import no.hamre.polet.dao.{H2LiquibaseDataSourceFactory, ProductDao, ProductDaoImpl}
import no.hamre.polet.modell.Product
import org.scalatest.FunSuite

import scala.io.Source

class FileParserTest extends FunSuite {

  test("Hello world") {
    val content = FixtureHelpers.fixture("produkter20141022-005843-775.csv")
    val lines = content.split("\n")
    println(s"${lines.size} lines in file")
    lines.tail.foreach{line => {
      val p = Product(line.split(";"))
      println( p )
    }}
    assert(1 == 1)
  }

  test("Parse file to lines") {
    val filename = "produkter.csv"
    val readmeText =
      Source.fromFile(s"src/main/resources/$filename", "windows-1252").getLines.toList
    val p = Product(readmeText.tail.head.split(";"))
    val ds = H2LiquibaseDataSourceFactory.createDataSource("polet")
    val dao = new ProductDaoImpl(ds)
    val id = dao.create(p)
    println(id)
/*
    readmeText.foreach {
      line =>
        println(line)
    }
*/
  }
}
