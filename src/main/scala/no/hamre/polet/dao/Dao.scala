package no.hamre.polet.dao

import java.sql.ResultSet
import java.time.ZoneId
import javax.sql.DataSource

import no.hamre.polet.modell.{Price, Product, ProductLine}
import no.hamre.polet.util.Slf4jLogger
import org.sql2o.data.Row
import org.sql2o.quirks.PostgresQuirks
import org.sql2o.{Connection, ResultSetHandler, Sql2o}

import scala.collection.JavaConverters._

trait Dao {
  def findAll: List[Product]

  def findByVarenummer(varenummer: String): Option[Product]

  def updateProductTimestamp(id: Long): Unit

  def insertProduct(product: ProductLine): Long

  def getLatestPrice(productId: Long): Price

  def findPrices(productId: Long): List[Price]

  def insertPrice(product: ProductLine, product_id: Long): Long

}

class PoletDao(dataSource: DataSource) extends Dao with PriceResultSetHandler with Slf4jLogger{
  val sql2o = new Sql2o(dataSource, new PostgresQuirks)

  override def updateProductTimestamp(id: Long): Unit = {
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val sql =
        s"""
           | UPDATE t_product
           | SET UPDATED = CURRENT_TIMESTAMP
           | WHERE id = :id
      """.stripMargin
      val rows: Long = con.createQuery(sql)
        .addParameter("id", id).executeUpdate().getResult
      if( rows != 1){
        throw DatabaseException(s"Expected to update only 1 row - was $rows")
      }
      con.commit(true)
    } catch {
      case e: Exception =>
        con.rollback(true)
        throw DatabaseException(e.getMessage)
    }
  }

  override def findAll: List[Product] = {
    val sql =
      """
        | SELECT * from t_product
      """.stripMargin
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val products = con.createQuery(sql)
        .executeAndFetchTable().rows().asScala.map(r => mapToProduct(r))
/*
        Product(
          r.getLong("id"),
          r.getDate("datotid").toInstant.atZone(defaultZoneId).toLocalDateTime,
          r.getString("varenummer"),
          r.getString("varenavn"),
          r.getDouble("volum"),
          r.getDouble("pris"),
          r.getDouble("literpris"),
          r.getString("varetype"),
          r.getString("produktutvalg"),
          r.getString("butikkategori"),
          r.getInteger("fylde"),
          r.getInteger("friskhet"),
          r.getInteger("garvestoffer"),
          r.getInteger("bitterhet"),
          r.getString("sodme"),
          r.getString("farge"),
          r.getString("lukt"),
          r.getString("smak"),
          r.getString("passertil01"),
          r.getString("passertil02"),
          r.getString("passertil03"),
          r.getString("land"),
          r.getString("distrikt"),
          r.getString("underdistrikt"),
          r.getInteger("aargang"),
          r.getString("raastoff"),
          r.getString("metode"),
          r.getDouble("alkohol"),
          r.getString("sukker"),
          r.getString("syre"),
          r.getString("lagringsgrad"),
          r.getString("produsent"),
          r.getString("grossist"),
          r.getString("distributor"),
          r.getString("emballasjetype"),
          r.getString("korktype"),
          r.getString("vareurl"),
          r.getDate("updated").toInstant.atZone(defaultZoneId).toLocalDateTime
        )
      )
*/
      products.toList
    } catch {
      case e: Exception =>
        con.rollback(true)
        throw new RuntimeException(e.getMessage, e)
    }
  }

  private def mapToProduct(r: Row): Product = {
    Product(
      r.getLong("id"),
      r.getDate("datotid").toInstant.atZone(defaultZoneId).toLocalDateTime,
      r.getString("varenummer"),
      r.getString("varenavn"),
      r.getString("varetype"),
      r.getDouble("volum"),
      r.getInteger("fylde"),
      r.getInteger("friskhet"),
      r.getInteger("garvestoffer"),
      r.getInteger("bitterhet"),
      r.getString("sodme"),
      r.getString("farge"),
      r.getString("lukt"),
      r.getString("smak"),
      r.getString("passertil01"),
      r.getString("passertil02"),
      r.getString("passertil03"),
      r.getString("land"),
      r.getString("distrikt"),
      r.getString("underdistrikt"),
      r.getInteger("aargang"),
      r.getString("raastoff"),
      r.getString("metode"),
      r.getDouble("alkohol"),
      r.getString("sukker"),
      r.getString("syre"),
      r.getString("lagringsgrad"),
      r.getString("produsent"),
      r.getString("grossist"),
      r.getString("distributor"),
      r.getString("emballasjetype"),
      r.getString("korktype"),
      r.getString("vareurl"),
      r.getInteger("active") == 1,
      r.getDate("updated").toInstant.atZone(defaultZoneId).toLocalDateTime,
      List()
    )
  }

/*
  private def mapToPrice(r: Row): Price = {
    Price(
      r.getLong("id"),
      r.getDate("datotid").toInstant.atZone(defaultZoneId).toLocalDateTime,
      r.getString("varenummer"),
      r.getDouble("volum"),
      r.getDouble("pris"),
      r.getDouble("literpris"),
      r.getString("produktutvalg"),
      r.getString("butikkategori"),
      r.getDate("updated").toInstant.atZone(defaultZoneId).toLocalDateTime)
  }

*/
  override def findByVarenummer(varenummer: String): Option[Product] = {
    val sql = "SELECT * FROM t_product WHERE varenummer=:varenummer"
    var con: Connection = null
    try {
      //val defaultZoneId = ZoneId.systemDefault()
      con = sql2o.beginTransaction()
      val products: List[Product] = con.createQuery(sql)
        .addParameter("varenummer", varenummer)
        .executeAndFetchTable().rows().asScala.map(r => mapToProduct(r)).toList
      con.close()
      products match {
        case Nil => None
        case x :: Nil => Some(x)
        case _ => throw new RuntimeException(s"Too many products with varenummer $varenummer")
      }
    } catch {
      case e: Exception => throw new RuntimeException(e.getMessage, e)
    }
  }

  override def insertPrice(product: ProductLine, productId: Long): Long = {
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val sql =
        s"""
           | INSERT INTO t_price (id, product_id, datotid, varenummer, volum, pris, literpris, produktutvalg,
           |   butikkategori, updated)
           | VALUES ( nextval('price_id_seq'), :productId, :datotid, :varenummer, :volum, :pris, :literpris, :produktutvalg,
           |   :butikkategori, CURRENT_TIMESTAMP)
      """.stripMargin
      val id: Long = con.createQuery(sql, true)
        .bind(product).addParameter("productId", productId)
        .executeUpdate().getKey.asInstanceOf[Long]
      con.commit(true)
      id
    } catch {
      case e: Exception =>
        con.rollback(true)
        log.error(e.getMessage, e)
        throw new RuntimeException(e.getMessage, e)
    }
  }


  override def getLatestPrice(productId: Long): Price = {
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val sql =
        s"""
           | SELECT * FROM t_price WHERE product_id=:productid ORDER BY datotid DESC, updated DESC
      """.stripMargin
      val price: Price = con.createQuery(sql)
        .addParameter("productid", productId)
        .executeAndFetchFirst(this)
      con.commit(true)
      price
    } catch {
      case e: Exception =>
        con.rollback(true)
        throw new RuntimeException(e.getMessage, e)
    }
  }

  override def findPrices(productId: Long): List[Price] = {
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val sql =
        s"""
           | SELECT * FROM t_price WHERE product_id=:productid ORDER BY datotid DESC, updated DESC
      """.stripMargin
      val prices: List[Price] = con.createQuery(sql)
        .addParameter("productid", productId)
        .executeAndFetch(this).asScala.toList
      con.commit(true)
      prices
    } catch {
      case e: Exception =>
        con.rollback(true)
        throw new RuntimeException(e.getMessage, e)
    }
  }

  override def insertProduct(product: ProductLine): Long = {
    val sql =
      """
        | INSERT INTO t_product (id, datotid, varenummer, varenavn, varetype, volum,
        |   fylde, friskhet, garvestoffer, bitterhet, sodme, farge, lukt, smak, passertil01,
        |   passertil02, passertil03, land, distrikt, underdistrikt, aargang, raastoff, metode, alkohol, sukker,
        |   syre, lagringsgrad, produsent, grossist, distributor, emballasjetype, korktype, vareurl, active)
        | VALUES (nextval('product_id_seq'), :datotid, :varenummer, :varenavn, :varetype, :volum,
        |   :fylde, :friskhet, :garvestoffer, :bitterhet, :sodme, :farge, :lukt, :smak, :passertil01,
        |   :passertil02, :passertil03, :land, :distrikt, :underdistrikt, :aargang, :raastoff, :metode, :alkohol, :sukker,
        |   :syre, :lagringsgrad, :produsent, :grossist, :distributor, :emballasjetype, :korktype, :vareurl, 1)
      """.stripMargin
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val id = con.createQuery(sql, true).bind(product).executeUpdate().getKey(classOf[Long]) //getKey //.asInstanceOf[Long]
      log.info(s"Generated key:  type=${id.getClass.getName} key=$id")
      con.commit(true)
      id
    } catch {
      case e: Exception =>
        con.rollback(true)
        log.error(e.getMessage, e)
        throw new RuntimeException(e.getMessage, e)
    }
  }
}

trait PriceResultSetHandler extends ResultSetHandler[Price] {
  val defaultZoneId = ZoneId.systemDefault()

  override def handle(resultSet: ResultSet): Price = {
    Price(
      resultSet.getLong("id"),
      resultSet.getTimestamp("datotid").toLocalDateTime, //.atZone(defaultZoneId).toLocalDateTime,
      resultSet.getString("varenummer"),
      resultSet.getDouble("volum"),
      resultSet.getDouble("pris"),
      resultSet.getDouble("literpris"),
      resultSet.getString("produktutvalg"),
      resultSet.getString("butikkategori"),
      resultSet.getTimestamp("updated").toInstant.atZone(defaultZoneId).toLocalDateTime
    )
  }
}

case class DatabaseException(message: String) extends RuntimeException(message)