package no.hamre.polet.service

import java.time.LocalDateTime

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.{Price, ProductLine}
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.FunSuite

trait ServiceTestData{
  val dao = mock(classOf[Dao])
  val line = "2014-10-22T00:56:50;4596101;Ardbeg 10 Years Old;0,70;549,90;785,60;Whisky;Basisutvalget;Butikkategori 5;0;0;0;0;0;;;;;;;Skottland;Islay;Øvrige;;Maltet bygg, gjær, vann;Tradisjonell produksjon, bl.a. dobbeltdestillasjon i pot stills. Min. 10 års fatlagring.;46,00;Ukjent;Ukjent;;Ardbeg Dist.;Moët Hennessy Norge AS;SKANLOG VSD AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-4596101"
  val idProd: Long = 1
  val idPrice: Long = 100
  val productLine = ProductLine(line.split(";"))
  val price = Price(idPrice, productLine.datotid, productLine.varenummer, productLine.volum, productLine.pris, productLine.literpris, productLine.varetype, productLine.produktutvalg, productLine.butikkategori, LocalDateTime.now())
  val service = new ProductDataServiceImpl(dao)
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
      val productLingWithId = productLine.copy(id=1L)
      when(dao.findByVarenummer(productId)).thenReturn(Some(productLingWithId))
      doNothing().when(dao).updateProductTimestamp(idProd)
      when(dao.getLatestPrice(idProd)).thenReturn(price)

      service.update(productLine)

      verify(dao, times(1)).findByVarenummer(productId)
      verify(dao, times(0)).insertProduct(productLine)
      verify(dao, times(1)).updateProductTimestamp(idProd)
      verify(dao, times(1)).getLatestPrice(idProd)
      verify(dao, times(0)).updatePrice(productLingWithId)
    }

  }
}
