package no.hamre.polet.service

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.Pris
import no.hamre.polet.modell.Whisky
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
  val price = Pris(idPrice, productLine.datotid, productLine.varenummer,
      productLine.volum, productLine.pris, productLine.literpris,
      productLine.produktutvalg, LocalDateTime.now())
  //val priceChanged = price.copy(pris = 345.0, updated = price.updated.plus(1, WEEKS))
  val product = Whisky(
    id = idProd,
    datotid = productLine.datotid,
    varenummer = productLine.varenummer,
    varenavn = productLine.varenavn,
    varetype = productLine.varetype,
    volum = productLine.volum,
    farge = productLine.farge,
    lukt = productLine.lukt,
    smak = productLine.smak,
    land = productLine.land,
    distrikt = productLine.distrikt,
    underdistrikt = productLine.underdistrikt,
    aargang = productLine.aargang,
    raastoff = productLine.raastoff,
    metode = productLine.metode,
    alkohol = productLine.alkohol,
    produsent = productLine.produsent,
    grossist = productLine.grossist,
    distributor = productLine.distributor,
    vareurl = productLine.vareurl, active = true, updated = productLine.updated
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
    `when`(dao.insertWhisky(productLine)).thenReturn(idProd)
    `when`(dao.insertPris(productLine, idProd, null)).thenReturn(idPrice)
    service.update(productLine)

    verify(dao, times(1)).findByVarenummer(productId)
    verify(dao, times(1)).insertWhisky(productLine)
    verify(dao, times(1)).insertPris(productLine, idProd, null)
  }

  @Test
  fun `Existing product with no price change should only update timestamp`() {
    val varenummer = "4596101"
    val productWithId = product.copy(id = 1L)
    val productLineWithId = productLine.copy(id = 1L)
    `when`(dao.findByVarenummer(varenummer)).thenReturn(productWithId)
    doNothing().`when`(dao).setWhiskyUpdated(idProd)
    `when`(dao.findGjeldendePris(idProd)).thenReturn(price)

    service.update(productLine)

    verify(dao, times(1)).findByVarenummer(varenummer)
    verify(dao, times(0)).insertWhisky(productLine)
    verify(dao, times(1)).setWhiskyUpdated(idProd)
    verify(dao, times(1)).findGjeldendePris(idProd)
    verify(dao, times(0)).insertPris(productLineWithId, productWithId.id, null)
  }

  @Test
  fun `Existing product without price record should insert price`() {
    val productId = "4596101"
    `when`(dao.findByVarenummer(productId)).thenReturn(product)
    doNothing().`when`(dao).setWhiskyUpdated(idProd)
    `when`(dao.findGjeldendePris(idProd)).thenReturn(null)

    service.update(productLine)

    verify(dao, times(1)).findByVarenummer(productId)
    verify(dao, times(0)).insertWhisky(productLine)
    verify(dao, times(1)).setWhiskyUpdated(idProd)
    verify(dao, times(1)).findGjeldendePris(idProd)
    verify(dao, times(1)).insertPris(productLine, idProd, null)
  }


  @Test
  fun `Existing product with changed price should update timestamp and add new price`() {
    val productLineWithId = productLine.copy(id = idProd)
    val productLineWithNewPrice = productLine.copy(pris = 1200.0)
    `when`(dao.findByVarenummer(productLine.varenummer)).thenReturn(product)
    doNothing().`when`(dao).setWhiskyUpdated(idProd)
    `when`(dao.findGjeldendePris(idProd)).thenReturn(price)

    service.update(productLineWithNewPrice)

    verify(dao, times(1)).findByVarenummer(productLine.varenummer)
    verify(dao, times(0)).insertWhisky(productLine)
    verify(dao, times(1)).setWhiskyUpdated(idProd)
    verify(dao, times(1)).setPrisendring(price.id, productLineWithNewPrice.datotid, productLineWithNewPrice.pris - price.pris)
    verify(dao, times(1)).findGjeldendePris(idProd)
    verify(dao, times(1)).insertPris(productLineWithNewPrice, productLineWithId.id!!, productLineWithNewPrice.pris)
  }

}
