package no.hamre.polet.service

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.WEEKS

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.{Price, Product, ProductLine}
import no.hamre.polet.parser.FileDownloaderImpl
import org.mockito.Mockito._
import org.scalatest.FunSuite

trait ServiceTestData{
  val dao = mock(classOf[Dao])
  val line = "2014-10-22T00:56:50;4596101;Ardbeg 10 Years Old;0,70;549,90;785,60;Whisky;Basisutvalget;Butikkategori 5;0;0;0;0;0;;;;;;;Skottland;Islay;Øvrige;;Maltet bygg, gjær, vann;Tradisjonell produksjon, bl.a. dobbeltdestillasjon i pot stills. Min. 10 års fatlagring.;46,00;Ukjent;Ukjent;;Ardbeg Dist.;Moët Hennessy Norge AS;SKANLOG VSD AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-4596101"
  val idProd: Long = 1
  val idPrice: Long = 100
  val productLine = ProductLine(line.split(";"))
  val price = Price(idPrice, productLine.datotid, productLine.varenummer,
    productLine.volum, productLine.pris, productLine.literpris,
    productLine.produktutvalg, productLine.butikkategori, LocalDateTime.now())
  val priceChanged = price.copy(pris = 345.0, updated = price.updated.plus(1, WEEKS))
  val service = new ProductDataServiceImpl(dao, new FileDownloaderImpl("vg.no"))
  val product = Product(idProd, productLine.datotid, productLine.varenummer, productLine.varenavn,
    productLine.varetype, productLine.volum,
    productLine.fylde,
    productLine.friskhet, productLine.garvestoffer, productLine.bitterhet,
    productLine.sodme, productLine.farge, productLine.lukt, productLine.smak,
    productLine.passertil01, productLine.passertil02, productLine.passertil03,
    productLine.land, productLine.distrikt, productLine.underdistrikt, productLine.aargang,
    productLine.raastoff, productLine.metode, productLine.alkohol, productLine.sukker,
    productLine.syre, productLine.lagringsgrad, productLine.produsent, productLine.grossist,
    productLine.distributor, productLine.emballasjetype, productLine.korktype,
    productLine.vareurl, active = true, productLine.updated, List()
  )
}

class ProductDataServiceImplTest extends FunSuite {

  test("Non existing product should be inserted with price"){
    new ServiceTestData {
      val productId = "4596101"
      when(dao.findByVarenummer(productId)).thenReturn(None)
      when(dao.insertProduct(productLine)).thenReturn(idProd)
      when(dao.insertPrice(productLine, idProd)).thenReturn(idPrice)
      service.update(productLine)

      verify(dao, times(1)).findByVarenummer(productId)
      verify(dao, times(1)).insertProduct(productLine)
      verify(dao, times(1)).insertPrice(productLine, idProd)
    }
  }

  test("Existing product with no price change should only update timestamp"){
    new ServiceTestData {
      val productId = "4596101"
      val productLineWithId = productLine.copy(id=1L)
      when(dao.findByVarenummer(productId)).thenReturn(Some(product))
      doNothing().when(dao).updateProductTimestamp(idProd)
      when(dao.getLatestPrice(idProd)).thenReturn(Some(price))

      service.update(productLine)

      verify(dao, times(1)).findByVarenummer(productId)
      verify(dao, times(0)).insertProduct(productLine)
      verify(dao, times(1)).updateProductTimestamp(idProd)
      verify(dao, times(1)).getLatestPrice(idProd)
      verify(dao, times(0)).insertPrice(productLineWithId, productLine.id)
    }
  }

  test("Existing product without price record should insert price"){
    new ServiceTestData {
      val productId = "4596101"
      when(dao.findByVarenummer(productId)).thenReturn(Some(product))
      doNothing().when(dao).updateProductTimestamp(idProd)
      when(dao.getLatestPrice(idProd)).thenReturn(Option.empty)

      service.update(productLine)

      verify(dao, times(1)).findByVarenummer(productId)
      verify(dao, times(0)).insertProduct(productLine)
      verify(dao, times(1)).updateProductTimestamp(idProd)
      verify(dao, times(1)).getLatestPrice(idProd)
      verify(dao, times(1)).insertPrice(productLine, idProd)
    }
  }

  test("Existing product with changed price should update timestamp and add ne wprice"){
    new ServiceTestData {
      val productLineWithId = productLine.copy(id=idProd)
      val productLineWithNewPrice = productLine.copy(pris=1200.0)
      when(dao.findByVarenummer(productLine.varenummer)).thenReturn(Some(product))
      doNothing().when(dao).updateProductTimestamp(idProd)
      when(dao.getLatestPrice(idProd)).thenReturn(Some(price))

      service.update(productLineWithNewPrice)

      verify(dao, times(1)).findByVarenummer(productLine.varenummer)
      verify(dao, times(0)).insertProduct(productLine)
      verify(dao, times(1)).updateProductTimestamp(idProd)
      verify(dao, times(1)).getLatestPrice(idProd)
      verify(dao, times(1)).insertPrice(productLineWithNewPrice, productLineWithId.id)
    }

  }

}
