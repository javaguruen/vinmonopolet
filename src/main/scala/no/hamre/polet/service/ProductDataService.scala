package no.hamre.polet.service

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.ProductLine
import no.hamre.polet.parser.FileDownloader
import no.hamre.polet.util.Slf4jLogger

trait ProductDataService {
  def updateFromWeb(url: String): DownloadResult

  def findProduct(productId: String): Option[Product]
}

class ProductDataServiceImpl(dao: Dao, downloader: FileDownloader) extends ProductDataService with Slf4jLogger {


  override def updateFromWeb(url: String): DownloadResult = {
    val data = downloader.download(url)
    var whiskies = 0
    var total = 0
    var success = 0
    var failure = 0
    var added = 0
    var priceChanges = 0
    var errors: List[String] = List()
    val lines = data.split("\n")
    log.info(s"${lines.size} lines in file")
    lines.tail
      .map(l => ProductLine(l.split(";")))
      .filter(p => p.varetype.toUpperCase() == "WHISKY")
      .foreach { p => {
      whiskies += 1
      try {
        val stat = update(p)
        success += 1
        if( stat.added ){
          added += 1
        }
        if( stat.priceChanged ){
          priceChanges += 1
        }
        //if( processed > 5) throw StopException("Stopping")
      }catch{
        case e: Exception =>
          log.error(s"Error ${e.getMessage} when parsing: \n$p")
          errors = s"${e.getMessage} : [$p]" :: errors
          failure += 1
      }
    }
    }
    log.info(s"Done downloading and parsing file. ${lines.size} lines read, $whiskies whiskies $failure failed and $success succeeded")
    DownloadResult(lines.size-1, whiskies, success, failure, added, priceChanges, errors)
  }

  case class UpdateStat(added: Boolean, priceChanged: Boolean)

  def update(product: ProductLine): UpdateStat = {
    dao.findByVarenummer(product.varenummer) match {
      case Some(p) =>
        log.info(s"Found varenummer ${p.varenummer} with id ${p.id}")
        dao.updateProductTimestamp(p.id)
        val latestPrice = dao.getLatestPrice(p.id)
        log.info(s"Latest price=$latestPrice")
        if( latestPrice.isEmpty){
          dao.insertPrice(product, p.id)
          UpdateStat(added = false, priceChanged = true)
        } else if (latestPrice.get.pris == product.pris) {
          log.info(s"Price unchanged for product ${p.id}")
          UpdateStat(added = false, priceChanged = false)
        } else {
          log.info(s"Price changed from ${latestPrice.get.pris} to ${product.pris} for product ${p.id}")
          dao.insertPrice(product, p.id)
          UpdateStat(added = false, priceChanged = true)
        }
      case None =>
        val productId = dao.insertProduct(product)
        log.info(s"New product inserted. Varenummer ${product.varenummer} got id: $productId")
        dao.insertPrice(product, productId)
        UpdateStat(added = true, priceChanged = true)
      case null =>
        throw new RuntimeException(s"findByVarenummer returned null for ${product.varenummer}")
    }
  }

  override def findProduct(productId: String): Option[Product] = {
    val product = dao.findByVarenummer(productId)
    product.map( p => p.copy(prices = dao.findPrices(p.id)))
  }
}

case class DownloadResult
(
  total: Int,
  whiskies: Int,
  success: Int,
  failure: Int,
  added: Int,
  priceChanges: Int,
  errors: List[String]
)