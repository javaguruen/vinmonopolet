package no.hamre.polet.dao

import no.hamre.polet.modell.*
import org.slf4j.LoggerFactory
import org.sql2o.Connection
import org.sql2o.ResultSetHandler
import org.sql2o.Sql2o
import org.sql2o.data.Row
import org.sql2o.quirks.PostgresQuirks
import java.sql.Date
import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.sql.DataSource


interface Dao {
  fun findAll(): List<Product>
  fun findLatestReleases(): List<Product>
  fun findByVarenummer(varenummer: String): Product?
  fun findById(id: Long): Product?
  fun updateProductTimestamp(id: Long)

  fun insertProduct(product: Productline): Long
  fun getLatestPrice(productId: Long): Price?

  fun findPrices(productId: Long): List<Price>

  fun insertPrice(product: Productline, productId: Long, oldPrice: Double?): Long

  fun priceChanged(priceId: Long, updated: LocalDateTime, priceChange: Double)

  fun findReleaseDates(): List<LocalDate>

  fun findReleasesByDate(releaseDate: LocalDate): List<MiniProduct>
}


class PoletDao(dataSource: DataSource)
  : Dao, PriceResultSetHandler {
  private val sql2o = Sql2o(dataSource, PostgresQuirks())
  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun updateProductTimestamp(id: Long) {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
          """
           | UPDATE t_product
           | SET UPDATED = CURRENT_TIMESTAMP
           | WHERE id = :id
      """.trimMargin()
      val rows = con.createQuery(sql)
          .addParameter("id", id)
          .executeUpdate().result
      if (rows != 1) {
        throw DatabaseException("Expected to update only 1 row - was $rows")
      }
      con.commit(true)
    } catch (e: Exception) {
      con?.rollback(true)
      throw DatabaseException(e.message)
    }
  }

  override fun findAll(): List<Product> {
    val sql =
        """
        | SELECT * from t_product
      """.trimMargin()
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val products = con.createQuery(sql)
          .executeAndFetchTable().rows().map { r ->
            mapToProduct(r)
          }
      return products
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }

  override fun findLatestReleases(): List<Product> {
    val sql =
        """
        | SELECT p.*
        | FROM t_product p
        | INNER JOIN t_price pr ON pr.product_id=p.id
        | WHERE
        |	  pr.datotid::date = (
        |	    SELECT max( prod.datotid::date ) FROM t_product prod
        | 	)
      """.trimMargin()
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val products = con.createQuery(sql)
          .executeAndFetchTable().rows().map { r ->
            mapToProduct(r)
          }
      return products
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }

  private fun mapToProduct(r: Row): Product {
    return Product(
        r.getLong("id"),
        r.getDate("datotid")
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
        r.getString("varenummer"),
        r.getString("varenavn"),
        r.getString("varetype"),
        r.getDouble("volum"),
        r.getInteger("fylde"),
        r.getInteger("friskhet"),
        r.getInteger("garvestoffer"),
        r.getInteger("bitterhet"),
        r.getInteger("sodme"),
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
        r.getDate("updated")
            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
        listOf()
    )
  }

  override fun findByVarenummer(varenummer: String): Product? {
    val sql = "SELECT * FROM t_product WHERE varenummer=:varenummer"
    var con: Connection? = null
    try {
      //val defaultZoneId = ZoneId.systemDefault()
      con = sql2o.beginTransaction()
      val products: List<Product> = con.createQuery(sql)
          .addParameter("varenummer", varenummer)
          .executeAndFetchTable().rows()
          .map { r -> mapToProduct(r) }
      con.close()
      return when (products.size) {
        0 -> null
        1 -> products[0]
        else -> throw RuntimeException("Too many products with varenummer $varenummer")
      }
    } catch (e: Exception) {
      con?.close()
      throw RuntimeException(e.message, e)
    }
  }

  override fun findById(id: Long): Product? {
    val sql = "SELECT * FROM t_product WHERE id=:id"
    var con: Connection? = null
    try {
      //val defaultZoneId = ZoneId.systemDefault()
      con = sql2o.beginTransaction()
      val products: List<Product> = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchTable().rows()
          .map { r -> mapToProduct(r) }
      con.close()
      return when (products.size) {
        0 -> null
        1 -> products[0]
        else -> throw RuntimeException("Too many products with id $id")
      }
    } catch (e: Exception) {
      con?.close()
      throw RuntimeException(e.message, e)
    }
  }

  override fun insertPrice(product: Productline, productId: Long, oldPrice: Double?): Long {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
          """
           | INSERT INTO t_price (id, product_id, datotid, varenummer, volum, pris, literpris, produktutvalg,
           |   butikkategori, active_to, price_change)
           | VALUES ( nextval('price_id_seq'), :productId, :datotid, :varenummer, :volum, :pris, :literpris, :produktutvalg,
           |   :butikkategori, null, :priceChange)
      """.trimMargin()
      val priceChange = oldPrice?.let { op -> product.pris - op } ?: 0.0
      val id: Long = con.createQuery(sql, true)
          .bind(product)
          .addParameter("productId", productId)
          .addParameter("priceChange", priceChange)
          .executeUpdate().getKey(Long::class.java)
      con.commit(true)
      return id
    } catch (e: Exception) {
      con?.rollback(true)
      //log.error(e.message, e)
      throw RuntimeException(e.message, e)
    }
  }

  override fun priceChanged(priceId: Long, updated: LocalDateTime, priceChange: Double) {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
          "UPDATE t_price SET active_to = :updated, price_change=:priceChange WHERE id=:priceid"
      con.createQuery(sql)
          .addParameter("updated", updated)
          .addParameter("priceid", priceId)
          .addParameter("priceChange", priceChange)
          .executeUpdate()
      con.commit(true)
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }


  override fun getLatestPrice(productId: Long): Price? {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
          """
           | SELECT * FROM t_price 
           | WHERE 
           |    product_id=:productid 
           |    AND active_to IS NULL
      """.trimMargin()
      val price: Price? = con.createQuery(sql)
          .addParameter("productid", productId)
          .executeAndFetchFirst(this)
      con.commit(true)
      return price
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }

  override fun findPrices(productId: Long): List<Price> {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
          """
           | SELECT *
           | FROM t_price
           | WHERE product_id=:productid
           | ORDER BY datotid DESC
      """.trimMargin()
      val prices: List<Price> = con.createQuery(sql)
          .addParameter("productid", productId)
          .executeAndFetch(this)
      con.commit(true)
      return prices
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }

  override fun insertProduct(product: Productline): Long {
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
      """.trimMargin()
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val id: Long = con.createQuery(sql, true)
          .bind(product)
          .executeUpdate()
          .getKey(Long::class.java)
      log.info("Generated key:  type=${id.javaClass.getName()} key=$id")
      con.commit(true)
      return id
    } catch (e: Exception) {
      con?.rollback(true)
      //log.error(e.message, e)
      throw RuntimeException(e.message, e)
    }
  }

  override fun findReleaseDates(): List<LocalDate> {
    val sql =
        """
        | SELECT DISTINCT datotid::date as date FROM t_product ORDER BY 1 DESC
      """.trimMargin()
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val dates = con.createQuery(sql).executeAndFetchTable()
          .rows().map { row ->
            (row.getDate("date") as Date)
                .toLocalDate()
          }
      con.commit(true)
      return dates
    } catch (e: Exception) {
      con?.rollback(true)
      //log.error(e.message, e)
      throw RuntimeException(e.message, e)
    }

  }

  override fun findReleasesByDate(releaseDate: LocalDate): List<MiniProduct> {
    val sql =
        """
        | SELECT p.*, price.pris
        | FROM t_product p
        |   INNER JOIN t_price price ON price.product_id = p.id AND price.active_to IS NULL
        | WHERE p.datotid::date = :releaseDate
        | ORDER BY 1 DESC
      """.trimMargin()
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val dates = con.createQuery(sql)
          .addParameter("releaseDate", Date.valueOf(releaseDate))
          .executeAndFetchTable()
          .rows().map { row ->
            MiniProduct(
                row.getLong("id"),
                row.getString("varenummer"),
                row.getString("varenavn"),
                row.getDouble("volum"),
                row.getDouble("pris"),
                listOf()
            )
          }
      con.commit(true)
      return dates
    } catch (e: Exception) {
      con?.rollback(true)
      log.error(e.message, e)
      throw RuntimeException(e.message, e)
    }
  }
}

interface PriceResultSetHandler : ResultSetHandler<Price> {
  override fun handle(resultSet: ResultSet): Price {
    return Price(
        resultSet.getLong("id"),
        resultSet.getTimestamp("datotid").toLocalDateTime(), //.atZone(defaultZoneId).toLocalDateTime,
        resultSet.getString("varenummer"),
        resultSet.getDouble("volum"),
        resultSet.getDouble("pris"),
        resultSet.getDouble("literpris"),
        resultSet.getString("produktutvalg"),
        resultSet.getString("butikkategori"),
        resultSet.getTimestamp("updated")
            .toInstant().atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    )
  }
}

data class DatabaseException(override val message: String? = "") : RuntimeException(message)