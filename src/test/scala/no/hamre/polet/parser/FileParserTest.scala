package no.hamre.polet.parser

import io.dropwizard.testing.FixtureHelpers
import no.hamre.polet.dao.{H2LiquibaseDataSourceFactory, PoletDao}
import no.hamre.polet.modell.ProductLine
import org.scalatest.FunSuite

import scala.io.Source

class FileParserTest extends FunSuite {


  ignore("Hello world") {
    val content = FixtureHelpers.fixture("produkter20141022-005843-775.csv")
    val lines = content.split("\n")
    println(s"${lines.size} lines in file")
    lines.tail.foreach{line => {
      val p = ProductLine(line.split(";"))
      //println( p )
    }}
    assert(1 == 1)
  }

  test("Parse file to lines") {
    val filename = "produkter.csv"
    val readmeText =
      Source.fromFile(s"src/main/resources/$filename", "windows-1252").getLines.toList
    val p = ProductLine(readmeText.tail.head.split(";"))
    val ds = H2LiquibaseDataSourceFactory.createDataSource("polet")
    val dao = new PoletDao(ds)
    val id = dao.update(p)
    println(id)
/*
    readmeText.foreach {
      line =>
        println(line)
    }
*/
  }
}
