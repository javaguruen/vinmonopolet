package no.hamre.polet.dao

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import no.hamre.polet.modell.ProductLine
import org.scalatest.FunSuite

class DaoTest extends FunSuite {

  test("Insert new product") {
    new ProductLineTestData {
      val id = dao.insertProduct(product)
      assert(id > 0)
      val storedProduct = dao.findByVarenummer(product.varenummer)
      assert(storedProduct.isDefined)
      assert(storedProduct.get.datotid == product.datotid)
      assert(storedProduct.get.varenummer == product.varenummer)
      assert(storedProduct.get.varenavn == product.varenavn)
      assert(storedProduct.get.varetype == product.varetype)
      assert(storedProduct.get.volum == product.volum)
      assert(storedProduct.get.fylde == product.fylde)
      assert(storedProduct.get.friskhet == product.friskhet)
      assert(storedProduct.get.garvestoffer == product.garvestoffer)
      assert(storedProduct.get.bitterhet == product.bitterhet)
      assert(storedProduct.get.sodme == product.sodme)
      assert(storedProduct.get.farge == product.farge)
      assert(storedProduct.get.lukt == product.lukt)
      assert(storedProduct.get.smak == product.smak)
      assert(storedProduct.get.passertil01 == product.passertil01)
      assert(storedProduct.get.passertil02 == product.passertil02)
      assert(storedProduct.get.passertil03 == product.passertil03)
      assert(storedProduct.get.land == product.land)
      assert(storedProduct.get.distrikt == product.distrikt)
      assert(storedProduct.get.underdistrikt == product.underdistrikt)
      assert(storedProduct.get.aargang == product.aargang)
      assert(storedProduct.get.raastoff == product.raastoff)
      assert(storedProduct.get.metode == product.metode)
      assert(storedProduct.get.alkohol == product.alkohol)
      assert(storedProduct.get.sukker == product.sukker)
      assert(storedProduct.get.syre == product.syre)
      assert(storedProduct.get.lagringsgrad == product.lagringsgrad)
      assert(storedProduct.get.produsent == product.produsent)
      assert(storedProduct.get.grossist == product.grossist)
      assert(storedProduct.get.distributor == product.distributor)
      assert(storedProduct.get.emballasjetype == product.emballasjetype)
      assert(storedProduct.get.korktype == product.korktype)
      assert(storedProduct.get.vareurl == product.vareurl)
      assert(storedProduct.get.active)
      assert(storedProduct.get.updated != null)
    }
  }

  test("Update timestamp") {
    new ProductLineTestData {
      val id = dao.insertProduct(product)
      val inserted = dao.findByVarenummer(product.varenummer)
      Thread.sleep(10)
      dao.updateProductTimestamp(id)
      val updated = dao.findByVarenummer(product.varenummer)
      assert(updated.get.updated.isAfter(inserted.get.updated))
    }
  }

  test("Get timestamps") {
    new ProductLineTestData {
      val id = dao.insertProduct(product)
      val dates = dao.findReleaseDates()
      assert(dates.size == 1)
      assert(dates.head.equals(product.datotid.toLocalDate))
    }
  }

  test("Get mini products") {
    new ProductLineTestData {
      val productId = dao.insertProduct(product)
      val id = dao.insertPrice(product, productId, None)
      val products = dao.findReleasesByDate(product.datotid.toLocalDate)
      assert(products.size == 1)
      val prod = products.head
      assert( prod.price == product.pris)
    }
  }

  test("Insert price") {
    new ProductLineTestData {
      val productId = dao.insertProduct(product)
      val id = dao.insertPrice(product, productId, None)
      assert(id > 0)
    }
  }

  test("Get latets price") {
    new ProductLineTestData {
      val productId = dao.insertProduct(product)
      val idFirst = dao.insertPrice(product, productId, None)
      Thread.sleep(10)
      val newPrice = 1000.50
      dao.priceChanged(idFirst, product.datotid.plus(1, ChronoUnit.DAYS))
      val idSecond = dao.insertPrice(product.copy(datotid = LocalDateTime.now(), pris = newPrice), productId, Some(product.pris))
      val latetsPrice = dao.getLatestPrice(productId)
      assert(latetsPrice.get.id == idSecond)
      assert(latetsPrice.get.pris == newPrice)
    }
  }

  test("findAll should return all rows") {
    new ProductLineTestData {
      dao.insertProduct(product)
      dao.insertProduct(product.copy(varenummer = "321"))
      val products = dao.findAll
      assert(products.size == 2)
    }
  }

  /*
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
  */

}

trait ProductLineTestData {
  val prodLine01 = "2014-10-22T00:56:50;1101;Løiten Linie;0,70;399,90;571,30;Akevitt;Basisutvalget;Butikkategori 3;0;0;0;0;0;;;;;;;Norge;Øvrige;Øvrige;;Poteter, krydder;16 mnd på fat;41,50;5,00;Ukjent;;;Arcus AS;Vectura AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-1101"
  val prodLine02 = "2014-10-22T00:56:50;12901;The Glenlivet Single Malt 12 Years Old;0,70;449,50;642,10;Whisky;Basisutvalget;Butikkategori 3;0;0;0;0;0;;;;;;;Skottland;Speyside;Øvrige;;100% maltet bygg.;Destilleres to ganger i tradisjonelle pot stills nøyaktig som for 150 år siden. 144 måneder lagring på eikefat.;40,00;Ukjent;Ukjent;;Chivas Brothers;Pernod Ricard Norway AS;SKANLOG VSD AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-12901"
  val product = ProductLine(prodLine01.split(";"))
  val dao: PoletDao = new PoletDao(H2LiquibaseDataSourceFactory.createDataSource("polet"))

}
