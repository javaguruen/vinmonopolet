package no.hamre.polet.service

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.ProductLine
import org.mockito.Mockito.{mock, when}
import org.scalatest.FunSuite

trait ServiceTestData{
  val dao = mock(classOf[Dao])
  val line = "2014-10-22T00:56:50;4596101;Ardbeg 10 Years Old;0,70;549,90;785,60;Whisky;Basisutvalget;Butikkategori 5;0;0;0;0;0;;;;;;;Skottland;Islay;Øvrige;;Maltet bygg, gjær, vann;Tradisjonell produksjon, bl.a. dobbeltdestillasjon i pot stills. Min. 10 års fatlagring.;46,00;Ukjent;Ukjent;;Ardbeg Dist.;Moët Hennessy Norge AS;SKANLOG VSD AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-4596101"
  val productLine = ProductLine(line.split(";"))
  val service = new ProductDataServiceImpl(dao)
}

class ProductDataServiceImplTest extends FunSuite {

  test("Non existing product should be inserted with price"){
    new ServiceTestData {
      val productId = "4596101"
      val idProd: Long = 1
      val idPrice: Long = 100
      when(dao.findByVarenummer(productId)).thenReturn(None)
      when(dao.insertProduct(productLine)).thenReturn(idProd)
      when(dao.insertPrice(productLine, idProd)).thenReturn(idPrice)
      service.update(productLine)
    }

  }
}
