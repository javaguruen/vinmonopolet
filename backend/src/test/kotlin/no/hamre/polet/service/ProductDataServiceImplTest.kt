package no.hamre.polet.service

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.Price
import no.hamre.polet.modell.Product
import no.hamre.polet.modell.ProductLineHelper
import no.hamre.polet.service.ServiceTestData.dao
import no.hamre.polet.service.ServiceTestData.idPrice
import no.hamre.polet.service.ServiceTestData.idProd
import no.hamre.polet.service.ServiceTestData.price
import no.hamre.polet.service.ServiceTestData.product
import no.hamre.polet.service.ServiceTestData.productLine
import no.hamre.polet.vinmonopolet.VinmonopoletClientImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime

object ServiceTestData {
  val dao: Dao = mock(Dao::class.java)
  private const val line = "2014-10-22T00:56:50;4596101;Ardbeg 10 Years Old;0,70;549,90;785,60;Whisky;Basisutvalget;Butikkategori 5;0;0;0;0;0;;;;;;;Skottland;Islay;Øvrige;;Maltet bygg, gjær, vann;Tradisjonell produksjon, bl.a. dobbeltdestillasjon i pot stills. Min. 10 års fatlagring.;46,00;Ukjent;Ukjent;;Ardbeg Dist.;Moët Hennessy Norge AS;SKANLOG VSD AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-4596101"
  const val idProd: Long = 1
  const val idPrice: Long = 100
  val productLine = ProductLineHelper.create(line.split(";"))!!
  val price = Price(idPrice, productLine.datotid, productLine.varenummer,
      productLine.volum, productLine.pris, productLine.literpris,
      productLine.produktutvalg, productLine.butikkategori, LocalDateTime.now())
  //val priceChanged = price.copy(pris = 345.0, updated = price.updated.plus(1, WEEKS))
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
      productLine.vareurl, active = true, updated = productLine.updated
  )
}

class ProductDataServiceImplTest {
  private val service = ProductDataServiceImpl(dao, VinmonopoletClientImpl("", ""))

  @BeforeEach
  fun beforeEach(){
    reset(dao)
  }

  @Test
  fun `Non existing product should be inserted with price`() {
    val productId = "4596101"
    `when`(dao.findByVarenummer(productId)).thenReturn(null)
    `when`(dao.insertProduct(productLine)).thenReturn(idProd)
    `when`(dao.insertPrice(productLine, idProd, null)).thenReturn(idPrice)
    service.update(productLine)

    verify(dao, times(1)).findByVarenummer(productId)
    verify(dao, times(1)).insertProduct(productLine)
    verify(dao, times(1)).insertPrice(productLine, idProd, null)
  }

  @Test
  fun `Existing product with no price change should only update timestamp`() {
    val varenummer = "4596101"
    val productWithId = product.copy(id = 1L)
    val productLineWithId = productLine.copy(id = 1L)
    `when`(dao.findByVarenummer(varenummer)).thenReturn(productWithId)
    doNothing().`when`(dao).updateProductTimestamp(idProd)
    `when`(dao.getLatestPrice(idProd)).thenReturn(price)

    service.update(productLine)

    verify(dao, times(1)).findByVarenummer(varenummer)
    verify(dao, times(0)).insertProduct(productLine)
    verify(dao, times(1)).updateProductTimestamp(idProd)
    verify(dao, times(1)).getLatestPrice(idProd)
    verify(dao, times(0)).insertPrice(productLineWithId, productWithId.id, null)
  }

  @Test
  fun `Existing product without price record should insert price`() {
    val productId = "4596101"
    `when`(dao.findByVarenummer(productId)).thenReturn(product)
    doNothing().`when`(dao).updateProductTimestamp(idProd)
    `when`(dao.getLatestPrice(idProd)).thenReturn(null)

    service.update(productLine)

    verify(dao, times(1)).findByVarenummer(productId)
    verify(dao, times(0)).insertProduct(productLine)
    verify(dao, times(1)).updateProductTimestamp(idProd)
    verify(dao, times(1)).getLatestPrice(idProd)
    verify(dao, times(1)).insertPrice(productLine, idProd, null)
  }


  @Test
  fun `Existing product with changed price should update timestamp and add new price`() {
    val productLineWithId = productLine.copy(id = idProd)
    val productLineWithNewPrice = productLine.copy(pris = 1200.0)
    `when`(dao.findByVarenummer(productLine.varenummer)).thenReturn(product)
    doNothing().`when`(dao).updateProductTimestamp(idProd)
    `when`(dao.getLatestPrice(idProd)).thenReturn(price)

    service.update(productLineWithNewPrice)

    verify(dao, times(1)).findByVarenummer(productLine.varenummer)
    verify(dao, times(0)).insertProduct(productLine)
    verify(dao, times(1)).updateProductTimestamp(idProd)
    verify(dao, times(1)).priceChanged(price.id, productLineWithNewPrice.datotid, productLineWithNewPrice.pris - price.pris)
    verify(dao, times(1)).getLatestPrice(idProd)
    verify(dao, times(1)).insertPrice(productLineWithNewPrice, productLineWithId.id!!, productLineWithNewPrice.pris)
  }

}
