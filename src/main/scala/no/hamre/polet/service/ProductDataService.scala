package no.hamre.polet.service

import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.ProductLine

trait ProductDataService {
  def update(product: ProductLine)
}

class ProductDataServiceImpl(dao: Dao) extends ProductDataService {

  override def update(product: ProductLine): Unit = {
    val id = dao.findByVarenummer(product.varenummer) match {
      case Some(p) =>
        dao.updateProductTimestamp(p.id)
        p.id
      case None    =>
        dao.insertProduct(product)
    }

  }
}
