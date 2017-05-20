package no.hamre.polet.service

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.ProductLine
import no.hamre.polet.util.Slf4jLogger

trait ProductDataService {
  def update(product: ProductLine)
}

class ProductDataServiceImpl(dao: Dao) extends ProductDataService with Slf4jLogger{

  override def update(product: ProductLine): Unit = {
    dao.findByVarenummer(product.varenummer) match {
      case Some(p) =>
        dao.updateProductTimestamp(p.id)
        val latestPrice = dao.getLatestPrice(p.id)
        if( latestPrice.pris == product.pris ){
          log.info(s"Price unchanged for product ${p.id}")
        } else {
          log.info(s"Price changed from ${latestPrice.pris} to ${product.pris} for product ${p.id}")
          dao.insertPrice(product, p.id)
        }
      case None    =>
        val productId = dao.insertProduct(product)
        dao.insertPrice(product, productId)
    }
  }
}
