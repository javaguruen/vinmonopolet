package no.hamre.polet.service

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.Whisky
import no.hamre.polet.modell.ProductRelease
import no.hamre.polet.modell.Productline
import no.hamre.polet.vinmonopolet.VinmonopoletClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate


interface ProductDataService {
  fun findLatestReleases(): List<Whisky>

  fun updateFromApi(): Oppdateringsstatus

  fun findWhiskyById(productId: Long): Whisky?
  fun findWhiskies(q: String): List<Whisky>
  fun findAllProduct(): List<Whisky>
  fun findProductByReleaseDate(): List<ProductRelease>
}
@Service
class ProductDataServiceImpl(
    private val dao: Dao,
    private val apiClient: VinmonopoletClient) : ProductDataService {

  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun findLatestReleases(): List<Whisky> {
    val products: List<Whisky> = dao.findBySisteSlipp()
    return products.map { p -> p.copy(prices = dao.findPriser(p.id)) }
  }

  override fun updateFromApi(): Oppdateringsstatus {
    var whiskies = 0
    var success = 0
    var failure = 0
    var added = 0
    var priceChanges = 0
    val errors = mutableListOf<String>()
    var totalProducts = -1
    var start = 0
    val batchSize = 400
    var continueBatch = true
    while (continueBatch) {
      val data = apiClient.doRequest(start, batchSize)
      log.info("${data.whiskies.size} whiskies found in request")
      log.info("Metadata: ${data.metaData}")
      data.whiskies
          //.asSequence()
          .forEach { productline ->
            try {
              val stat = update(productline)
              success += 1
              if (stat.added) {
                added += 1
              }
              if (stat.priceChanged) {
                priceChanges += 1
              }
            } catch (e: Exception) {
              log.error("Error ${e.message} when parsing: \n$productline")
              errors.add("${e.message} : <$productline")
              failure += 1
            }
          }
      log.info("Processed one batch")
      whiskies += data.whiskies.size
      totalProducts = data.metaData.totalCount
      continueBatch = !data.metaData.isDone
      start += batchSize
    }
    log.info("Done downloading from API, $whiskies whiskies $failure failed and $success succeeded")
    return Oppdateringsstatus(
        totalt = totalProducts,
        vellykket = success,
        whiskier = whiskies,
        feil = failure,
        lagtTil = added,
        prisendringer = priceChanges,
        feilmeldinger = errors)
  }

  data class UpdateStat(val added: Boolean, val priceChanged: Boolean)

  fun update(product: Productline): UpdateStat {
    val p = dao.findByVarenummer(product.varenummer)
    if (p != null) {
      log.info("Found varenummer ${p.varenummer} with id ${p.id}")
      dao.setWhiskyUpdated(p.id)
      val latestPrice = dao.findGjeldendePris(p.id)
      log.info("Latest price=$latestPrice")
      if (latestPrice == null) {
        dao.insertPris(product, p.id, null)
        return UpdateStat(added = false, priceChanged = true)
      } else if (latestPrice.pris == product.pris) {
        log.info("Price unchanged for product ${p.id}")
        return UpdateStat(added = false, priceChanged = false)
      } else {
        log.info("Price changed from ${latestPrice.pris} to ${product.pris} for product ${p.id}")
        dao.setPrisendring(latestPrice.id, product.datotid, product.pris - latestPrice.pris)
        dao.insertPris(product, p.id, product.pris)
        return UpdateStat(added = false, priceChanged = true)
      }
    } else { //p == null
      val productId = dao.insertWhisky(product)
      log.info("New product inserted. Varenummer ${product.varenummer} got id: $productId")
      dao.insertPris(product, productId, null)
      return UpdateStat(added = true, priceChanged = true)
    }
  }

  override fun findWhiskyById(productId: Long): Whisky? {
    val product = dao.findById(productId)
    return product?.let { p -> p.copy(prices = dao.findPriser(p.id)) }
  }

  override fun findWhiskies(q: String): List<Whisky> {
    return dao.search(q)
  }

  override fun findProductByReleaseDate(): List<ProductRelease> {
    val releaseDates: List<LocalDate> = dao.findSlippdatoer()
    val miniProducts = dao.findBySlippdato(releaseDates.first())
    return listOf(ProductRelease(releaseDates.first(), miniProducts, listOf())) +
        releaseDates.subList(1, releaseDates.size).map { date -> ProductRelease(date, listOf(), listOf()) }
  }

  override fun findAllProduct(): List<Whisky> {
    return dao.findAll().map { p -> p.copy(prices = dao.findPriser(p.id)) }
  }
}

data class Oppdateringsstatus
(
  val totalt: Int,
  val whiskier: Int,
  val vellykket: Int,
  val feil: Int,
  val lagtTil: Int,
  val prisendringer: Int,
  val feilmeldinger: List<String>
)