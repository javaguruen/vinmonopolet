package no.hamre.polet.dao

import no.hamre.polet.modell.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
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
  fun findAll(): List<Whisky>
  fun search(q: String): List<Whisky>
  fun findBySisteSlipp(): List<Whisky>
  fun findByVarenummer(varenummer: String): Whisky?
  fun findById(id: Long): Whisky?
  fun setWhiskyUpdated(id: Long)

  fun insertWhisky(product: Productline): Long
  fun findGjeldendePris(whiskyId: Long): Pris?

  fun findPriser(whiskyId: Long): List<Pris>

  fun insertPris(product: Productline, whiskyId: Long, forrigePris: Double?): Long

  fun setPrisendring(prisId: Long, oppdatert: LocalDateTime, prisendring: Double)

  fun findSlippdatoer(): List<LocalDate>

  fun findBySlippdato(slippdato: LocalDate): List<MiniProduct>
}

@Repository
class PoletDao(dataSource: DataSource) : Dao, PrisResultSetHandler {
  private val sql2o = Sql2o(dataSource, PostgresQuirks())

  override fun setWhiskyUpdated(id: Long) {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
        """
           | UPDATE whisky
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

  override fun search(q: String): List<Whisky> {
    val safeParam = SqlInputCleaner.clean(q)
    val sql = """
      SELECT * FROM whisky WHERE lower(varenavn) LIKE '%$safeParam%'
      UNION
      SELECT * FROM whisky WHERE lower(produsent) LIKE '%$safeParam%'
    """.trimIndent().toString()
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val products = con.createQuery(sql)
        .executeAndFetchTable().rows().map { r ->
          mapToWhisky(r)
        }
      return products
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }

  override fun findAll(): List<Whisky> {
    val sql =
      """
        | SELECT * from whisky
      """.trimMargin()
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val products = con.createQuery(sql)
        .executeAndFetchTable().rows().map { r ->
          mapToWhisky(r)
        }
      return products
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }

  override fun findBySisteSlipp(): List<Whisky> {
    val sql =
      """
        | SELECT w.*
        | FROM whisky w
        | INNER JOIN pris p ON p.whisky_id = w.id
        | WHERE
        |	  p.datotid::date = (
        |	    SELECT max( p2.datotid::date ) FROM pris p2
        | 	)
      """.trimMargin()
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val products = con.createQuery(sql)
        .executeAndFetchTable().rows().map { r ->
          mapToWhisky(r)
        }
      return products
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }

  private fun mapToWhisky(r: Row): Whisky {
    return Whisky(
      r.getLong("id"),
      r.getDate("datotid")
        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
      r.getString("varenummer"),
      r.getString("varenavn"),
      r.getString("varetype"),
      r.getDouble("volum"),
      r.getString("farge"),
      r.getString("lukt"),
      r.getString("smak"),
      r.getString("land"),
      r.getString("distrikt"),
      r.getString("underdistrikt"),
      r.getInteger("aargang"),
      r.getString("raastoff"),
      r.getString("metode"),
      r.getDouble("alkohol"),
      r.getString("produsent"),
      r.getString("grossist"),
      r.getString("distributor"),
      r.getString("vareurl"),
      r.getInteger("active") == 1,
      r.getDate("updated")
        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
      listOf()
    )
  }

  override fun findByVarenummer(varenummer: String): Whisky? {
    val sql = "SELECT * FROM whisky WHERE varenummer = :varenummer"
    var con: Connection? = null
    try {
      //val defaultZoneId = ZoneId.systemDefault()
      con = sql2o.beginTransaction()
      val whiskies: List<Whisky> = con.createQuery(sql)
        .addParameter("varenummer", varenummer)
        .executeAndFetchTable().rows()
        .map { r -> mapToWhisky(r) }
      con.close()
      return when (whiskies.size) {
        0 -> null
        1 -> whiskies[0]
        else -> throw RuntimeException("Too many whiskies with varenummer $varenummer")
      }
    } catch (e: Exception) {
      con?.close()
      throw RuntimeException(e.message, e)
    }
  }

  override fun findById(id: Long): Whisky? {
    val sql = "SELECT * FROM whisky WHERE id=:id"
    var con: Connection? = null
    try {
      //val defaultZoneId = ZoneId.systemDefault()
      con = sql2o.beginTransaction()
      val whiskies: List<Whisky> = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchTable().rows()
        .map { r -> mapToWhisky(r) }
      con.close()
      return when (whiskies.size) {
        0 -> null
        1 -> whiskies[0]
        else -> throw RuntimeException("Too many whiskies with id $id")
      }
    } catch (e: Exception) {
      con?.close()
      throw RuntimeException(e.message, e)
    }
  }

  override fun insertPris(whisky: Productline, whiskyId: Long, gammelPris: Double?): Long {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
        """
           | INSERT INTO pris (id, whisky_id, datotid, varenummer, volum, pris, literpris, produktutvalg,
           |   butikkategori, active_to, price_change)
           | VALUES ( nextval('price_id_seq'), :productId, :datotid, :varenummer, :volum, :pris, :literpris, :produktutvalg,
           |   :butikkategori, null, :priceChange)
      """.trimMargin()
      val priceChange = gammelPris?.let { op -> whisky.pris - op } ?: 0.0
      val id: Long = con.createQuery(sql, true)
        .bind(whisky)
        .addParameter("productId", whiskyId)
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

  override fun setPrisendring(prisId: Long, oppdatert: LocalDateTime, prisendring: Double) {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
        "UPDATE pris SET active_to = :updated, price_change=:priceChange WHERE id=:priceid"
      con.createQuery(sql)
        .addParameter("updated", oppdatert)
        .addParameter("priceid", prisId)
        .addParameter("priceChange", prisendring)
        .executeUpdate()
      con.commit(true)
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }


  override fun findGjeldendePris(whiskyId: Long): Pris? {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
        """
           | SELECT * FROM pris 
           | WHERE 
           |    whisky_id = :productid 
           |    AND active_to IS NULL
      """.trimMargin()
      val pris: Pris? = con.createQuery(sql)
        .addParameter("productid", whiskyId)
        .executeAndFetchFirst(this)
      con.commit(true)
      return pris
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }

  override fun findPriser(whiskyId: Long): List<Pris> {
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val sql =
        """
           | SELECT *
           | FROM pris
           | WHERE whisky_id = :productid
           | ORDER BY datotid DESC
      """.trimMargin()
      val priser: List<Pris> = con.createQuery(sql)
        .addParameter("productid", whiskyId)
        .executeAndFetch(this)
      con.commit(true)
      return priser
    } catch (e: Exception) {
      con?.rollback(true)
      throw RuntimeException(e.message, e)
    }
  }

  override fun insertWhisky(product: Productline): Long {
    val sql =
      """
        | INSERT INTO whisky (id, datotid, varenummer, varenavn, varetype, volum,
        |   farge, lukt, smak, land, distrikt, underdistrikt, aargang, raastoff, metode, alkohol, 
        |   produsent, grossist, distributor, vareurl, active)
        | VALUES (nextval('product_id_seq'), :datotid, :varenummer, :varenavn, :varetype, :volum,
        |   :farge, :lukt, :smak, :land, :distrikt, :underdistrikt, :aargang, :raastoff, :metode, :alkohol,
        |   :produsent, :grossist, :distributor, :vareurl, 1)
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

  override fun findSlippdatoer(): List<LocalDate> {
    val sql =
      """
        | SELECT DISTINCT datotid::date as date FROM whisky ORDER BY 1 DESC
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

  override fun findBySlippdato(slippdato: LocalDate): List<MiniProduct> {
    val sql =
      """
        | SELECT w.*, p.pris
        | FROM whisky w
        |   INNER JOIN pris p ON p.whisky_id = w.id AND p.active_to IS NULL
        | WHERE p.datotid::date = :releaseDate
        | ORDER BY 1 DESC
      """.trimMargin()
    var con: Connection? = null
    try {
      con = sql2o.beginTransaction()
      val dates = con.createQuery(sql)
        .addParameter("releaseDate", Date.valueOf(slippdato))
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

  companion object {
    private val log = LoggerFactory.getLogger(this.javaClass)
  }

}

interface PrisResultSetHandler : ResultSetHandler<Pris> {
  override fun handle(resultSet: ResultSet): Pris {
    return Pris(
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