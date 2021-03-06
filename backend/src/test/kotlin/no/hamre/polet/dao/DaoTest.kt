package no.hamre.polet.dao

import no.hamre.polet.dao.ProductLineTestData.product
import no.hamre.polet.modell.ProductLineHelper
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class DaoTest {
  val dao: PoletDao = PoletDao(FlywayDataSourceFactory.create()) //PoletDao(H2LiquibaseDataSourceFactory.createDataSource("polet"))

  @Test
  fun `Insert new product`() {
    val id = dao.insertWhisky(product)
    assert(id > 0)
    val storedProduct = dao.findByVarenummer(product.varenummer)
    assert(storedProduct != null)
    assert(storedProduct?.datotid == product.datotid)
    assert(storedProduct?.varenummer == product.varenummer)
    assert(storedProduct?.varenavn == product.varenavn)
    assert(storedProduct?.varetype == product.varetype)
    assert(storedProduct?.volum == product.volum)
    assert(storedProduct?.farge == product.farge)
    assert(storedProduct?.lukt == product.lukt)
    assert(storedProduct?.smak == product.smak)
    assert(storedProduct?.land == product.land)
    assert(storedProduct?.distrikt == product.distrikt)
    assert(storedProduct?.underdistrikt == product.underdistrikt)
    assert(storedProduct?.aargang == product.aargang)
    assert(storedProduct?.raastoff == product.raastoff)
    assert(storedProduct?.metode == product.metode)
    assert(storedProduct?.alkohol == product.alkohol)
    assert(storedProduct?.produsent == product.produsent)
    assert(storedProduct?.grossist == product.grossist)
    assert(storedProduct?.distributor == product.distributor)
    assert(storedProduct?.vareurl == product.vareurl)
    assert(storedProduct?.active!!)
    assert(storedProduct.updated != null)
  }


  @Test
  fun `Update timestamp`() {
    val id = dao.insertWhisky(product)
    val inserted = dao.findByVarenummer(product.varenummer)
    Thread.sleep(10)
    dao.setWhiskyUpdated(id)
    val updated = dao.findByVarenummer(product.varenummer)
    assert(updated?.updated?.isAfter(inserted?.updated) ?: false)
  }

  @Test
  fun `Get timestamps`() {
    dao.insertWhisky(product)
    val dates = dao.findSlippdatoer()
    assert(dates.size == 1)
    assert(dates.first() == product.datotid.toLocalDate())
  }

  @Test
  fun `Get mini products`() {
    val productId = dao.insertWhisky(product)
    dao.insertPris(product, productId, null)
    val products = dao.findBySlippdato(product.datotid.toLocalDate())
    assert(products.size == 1)
    val prod = products.first()
    assert(prod.price == product.pris)
  }

  @Test
  fun `Insert price`() {
    val productId = dao.insertWhisky(product)
    val id = dao.insertPris(product, productId, null)
    assert(id > 0)
  }

  @Test
  fun `Get latets price`() {
    val productId = dao.insertWhisky(product)
    val idFirst = dao.insertPris(product, productId, null)
    Thread.sleep(10)
    val newPrice = 1000.50
    dao.setPrisendring(idFirst, product.datotid.plus(1, ChronoUnit.DAYS), newPrice - product.pris)
    val idSecond = dao.insertPris(product.copy(datotid = LocalDateTime.now(), pris = newPrice), productId, product.pris)
    val latetsPrice = dao.findGjeldendePris(productId)
    assert(latetsPrice?.id == idSecond)
    assert(latetsPrice?.pris == newPrice)
  }


  @Test
  fun `findAll should return all rows`(){
      dao.insertWhisky(product)
      dao.insertWhisky(product.copy(varenummer = "321"))
      val products = dao.findAll()
      assert(products.size == 2)
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

object ProductLineTestData {
  val prodLine01 = "2014-10-22T00:56:50;1101;Løiten Linie;0,70;399,90;571,30;Akevitt;Basisutvalget;Butikkategori 3;0;0;0;0;0;;;;;;;Norge;Øvrige;Øvrige;;Poteter, krydder;16 mnd på fat;41,50;5,00;Ukjent;;;Arcus AS;Vectura AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-1101"
  val prodLine02 = "2014-10-22T00:56:50;12901;The Glenlivet Single Malt 12 Years Old;0,70;449,50;642,10;Whisky;Basisutvalget;Butikkategori 3;0;0;0;0;0;;;;;;;Skottland;Speyside;Øvrige;;100% maltet bygg.;Destilleres to ganger i tradisjonelle pot stills nøyaktig som for 150 år siden. 144 måneder lagring på eikefat.;40,00;Ukjent;Ukjent;;Chivas Brothers;Pernod Ricard Norway AS;SKANLOG VSD AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-12901"
  val product = ProductLineHelper.create(prodLine01.split(";"))!!
}
