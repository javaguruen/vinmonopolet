package no.hamre.polet.dao

import no.hamre.polet.modell.ProductLine
import org.scalatest.FunSuite

class DaoTest extends FunSuite {

  test("testUpdate") {
    new ProductLineTestData {
      val id = dao.update(product)
      assert(id > 0)
    }
  }

  test("Non-existing product gives None") {
    new ProductLineTestData {
      val connection = dao.sql2o.beginTransaction()
      val productId = dao.update(product)
      val foundId = dao.productExists(product.varenummer, connection)
      connection.rollback()
      assert(foundId.isDefined)
      assert(foundId.get == productId)
    }
  }

  test("Existing product gives Some(id)") {
    new ProductLineTestData {
      val connection = dao.sql2o.beginTransaction()
      val id = dao.productExists(product.varenummer, connection)
      connection.rollback()
      assert(id.isEmpty)
    }
  }

}

trait ProductLineTestData {
  val prodLine01 = "2014-10-22T00:56:50;1101;Løiten Linie;0,70;399,90;571,30;Akevitt;Basisutvalget;Butikkategori 3;0;0;0;0;0;;;;;;;Norge;Øvrige;Øvrige;;Poteter, krydder;16 mnd på fat;41,50;5,00;Ukjent;;;Arcus AS;Vectura AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-1101"
  val prodLine02 = "2014-10-22T00:56:50;12901;The Glenlivet Single Malt 12 Years Old;0,70;449,50;642,10;Whisky;Basisutvalget;Butikkategori 3;0;0;0;0;0;;;;;;;Skottland;Speyside;Øvrige;;100% maltet bygg.;Destilleres to ganger i tradisjonelle pot stills nøyaktig som for 150 år siden. 144 måneder lagring på eikefat.;40,00;Ukjent;Ukjent;;Chivas Brothers;Pernod Ricard Norway AS;SKANLOG VSD AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-12901"
  val product = ProductLine(prodLine01.split(";"))
  val dao: PoletDao = new PoletDao(H2LiquibaseDataSourceFactory.createDataSource("polet"))

}
