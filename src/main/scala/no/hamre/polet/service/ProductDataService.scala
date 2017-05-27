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
    var processed = 0
    var success = 0
    var failure = 0
    var errors: List[String] = List()
    val lines = data.split("\n")
    log.info(s"${lines.size} lines in file")
    lines.tail.foreach { line => {
      val p = ProductLine(line.split(";"))
      processed += 1
      try {
        update(p)
        success += 1
        if( processed > 5) throw StopException("Stopping")
      }catch{
        case e: StopException =>
          return   throw new RuntimeException("stopped")//  DownloadResult(processed, success, failure, errors)

        case e: Exception =>
          log.error(s"Error ${e.getMessage} when parsing line: \n$line")
          errors = s"${e.getMessage} : [$line]" :: errors
          failure += 1
      }
    }
    }
    log.info(s"Done downloading and parsing file. $processed lines read, $failure failed and $success succeeded")
    DownloadResult(processed, success, failure, errors)
  }

  case class StopException(msg: String ) extends RuntimeException(msg)

  def update(product: ProductLine): Unit = {
    dao.findByVarenummer(product.varenummer) match {
      case Some(p) =>
        log.info(s"Found varenummer ${p.varenummer} with id ${p.id}")
        dao.updateProductTimestamp(p.id)
        val latestPrice = dao.getLatestPrice(p.id)
        log.info(s"Latest price=$latestPrice")
        if (latestPrice.pris == product.pris) {
          log.info(s"Price unchanged for product ${p.id}")
        } else {
          log.info(s"Price changed from ${latestPrice.pris} to ${product.pris} for product ${p.id}")
          dao.insertPrice(product, p.id)
        }
      case None =>
        val productId = dao.insertProduct(product)
        log.info(s"New product inserted. Varenummer ${product.varenummer} got id: $productId")
        dao.insertPrice(product, productId)
    }
  }

  override def findProduct(productId: String): Option[Product] = {
    val product = dao.findByVarenummer(productId)
    product.map( p => p.copy(prices = dao.findPrices(p.id)))
  }
}

case class DownloadResult
(
  processed: Int,
  success: Int,
  failure: Int,
  errors: List[String]
)