package no.hamre.polet.service

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.Product
import no.hamre.polet.modell.ProductLineHelper
import no.hamre.polet.modell.ProductRelease
import no.hamre.polet.modell.Productline
import no.hamre.polet.parser.FileDownloader
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime


interface ProductDataService {
  fun findLatestReleases(): List<Product>

  fun updateFromWeb(url: String): DownloadResult

  fun findProduct(productId: Long): Product?
  fun findAllProduct(): List<Product>
  fun findProductByReleaseDate(): List<ProductRelease>
}

class ProductDataServiceImpl(
    private val dao: Dao,
    private val downloader: FileDownloader) : ProductDataService {

  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun findLatestReleases(): List<Product> {
    val products: List<Product> = dao.findLatestReleases()
    return products.map { p -> p.copy(prices = dao.findPrices(p.id)) }
  }

  override fun updateFromWeb(url: String): DownloadResult {
    val data = downloader.download(url)
    var whiskies = 0
    var success = 0
    var failure = 0
    var added = 0
    var priceChanges = 0
    val errors = mutableListOf<String>()
    val lines = data.split("\n")
    log.info("${lines.size} lines in file")
    lines.subList(1, lines.size)
        .asSequence()
        .also { log.info("Parsing: $it") }
        .map { l -> ProductLineHelper.create(l.split(";")) }
        .filter { p -> p.varetype.toUpperCase() == "WHISKY" }
        .forEach { p ->
          whiskies += 1
          try {
            val stat = update(p)
            success += 1
            if (stat.added) {
              added += 1
            }
            if (stat.priceChanged) {
              priceChanges += 1
            }
            //if( processed > 5) throw StopException("Stopping")
          } catch (e: Exception) {
            log.error("Error ${e.message} when parsing: \n$p")
            errors.add("${e.message} : <$p>")
            failure += 1
          }
        }
    log.info("Done downloading and parsing file. ${lines.size} lines read, $whiskies whiskies $failure failed and $success succeeded")
    return DownloadResult(lines.size - 1, whiskies, success, failure, added, priceChanges, errors)
  }

  data class UpdateStat(val added: Boolean, val priceChanged: Boolean)

  fun update(product: Productline): UpdateStat {
    val p = dao.findByVarenummer(product.varenummer)
    if (p != null) {
      log.info("Found varenummer ${p.varenummer} with id ${p.id}")
      dao.updateProductTimestamp(p.id)
      val latestPrice = dao.getLatestPrice(p.id)
      log.info("Latest price=$latestPrice")
      if (latestPrice == null) {
        dao.insertPrice(product, p.id, null)
        return UpdateStat(added = false, priceChanged = true)
      } else if (latestPrice.pris == product.pris) {
        log.info("Price unchanged for product ${p.id}")
        return UpdateStat(added = false, priceChanged = false)
      } else {
        log.info("Price changed from ${latestPrice.pris} to ${product.pris} for product ${p.id}")
        dao.priceChanged(latestPrice.id, product.datotid, product.pris - latestPrice.pris)
        dao.insertPrice(product, p.id, product.pris)
        return UpdateStat(added = false, priceChanged = true)
      }
    } else { //p == null
      val productId = dao.insertProduct(product)
      log.info("New product inserted. Varenummer ${product.varenummer} got id: $productId")
      dao.insertPrice(product, productId, null)
      return UpdateStat(added = true, priceChanged = true)
    }
  }

  override fun findProduct(productId: Long): Product? {
    val product = dao.findById(productId)
    return product?.let { p -> p.copy(prices = dao.findPrices(p.id)) }
  }

  override fun findProductByReleaseDate(): List<ProductRelease> {
    val releaseDates: List<LocalDate> = dao.findReleaseDates()
    val miniProducts = dao.findReleasesByDate(releaseDates.first())
    return listOf(ProductRelease(releaseDates.first(), miniProducts, listOf())) +
        releaseDates.subList(1, releaseDates.size).map { date -> ProductRelease(date, listOf(), listOf()) }
  }

  override fun findAllProduct(): List<Product> {
    return dao.findAll().map { p -> p.copy(prices = dao.findPrices(p.id)) }
  }
}

data class DownloadResult
(
    val total: Int,
    val whiskies: Int,
    val success: Int,
    val failure: Int,
    val added: Int,
    val priceChanges: Int,
    val errors: List<String>
)