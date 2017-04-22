package no.hamre.polet.dao

import java.time.ZoneId
import javax.sql.DataSource

import no.hamre.polet.modell.ProductLine
import org.sql2o.quirks.PostgresQuirks
import org.sql2o.{Connection, Sql2o}
import scala.collection.JavaConverters._

trait Dao {
  def findAll: List[ProductLine]

  def productExists(varenummer: String, con: Connection): Option[Long]

  def update(product: ProductLine): Long

  def updateProduct(product: ProductLine, con: Connection): Long

  def insertProduct(product: ProductLine, con: Connection): Long

  def insertPrice(product: ProductLine, product_id: Long, con: Connection): Long

  def updatePrice(product: ProductLine, con: Connection): Boolean
}

class PoletDao(dataSource: DataSource) extends Dao {
  val sql2o = new Sql2o(dataSource, new PostgresQuirks)

  override def findAll: List[ProductLine] = {
    val sql =
      """
        | SELECT * from t_product
      """.stripMargin
    var con: Connection = null
    try {
      val defaultZoneId = ZoneId.systemDefault()
      con = sql2o.beginTransaction()
      val products = con.createQuery(sql)
        .executeAndFetchTable().rows().asScala.map(r =>
        ProductLine(
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
      products.toList
    } catch {
      case e: Exception =>
        con.rollback(true)
        throw new RuntimeException(e.getMessage, e)
    }
  }

  /*
      val filename = "produkter.csv"
      val path = "src/main/resources"
  //    val readmeText = Source.fromFile(s"$path/$filename", "utf-8").getLines().toList
      val readmeText = Source.fromFile(s"$path/$filename", "Windows-1252").getLines().toList
      //readmeText.foreach(line => println(line))
      readmeText.tail
          .map(l => CharsetConverter(l))
        .map(l=>Product(l.split(";")))
  */


  override def update(product: ProductLine): Long = {
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val maybeId = productExists(product.varenavn, con)
      val id = maybeId match {
        case Some(productId) =>
          updateProduct(product, con)
          productId
        case None =>
          val newId = insertProduct(product, con)
          insertPrice(product, newId, con)
          newId
      }
      con.commit()
      id
    } catch {
      case e: Exception =>
        con.rollback(true)
        throw new RuntimeException(e.getMessage, e)
    }
  }

  override def productExists(varenummer: String, con: Connection): Option[Long] = {
    var sql = """SELECT id FROM t_product WHERE varenummer=:varenummer"""
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val id: Long = con.createQuery(sql)
        .addParameter("varenummer", varenummer)
        .executeScalar(classOf[Long])
      con.commit(true)
      id match {
        case 0 => None
        case a: Long => Some(a)
      }
    } catch {
      case e: Exception =>
        con.rollback(true)
        throw new RuntimeException(e.getMessage, e)
    }
  }

  override def updateProduct(product: ProductLine, con: Connection): Long = {
    val sql =
      """
        | INSERT INTO t_product ( datotid, varenummer, varenavn, volum, pris, literpris, varetype, produktutvalg,
        |   butikkategori, fylde, friskhet, garvestoffer, bitterhet, sodme, farge, lukt, smak, passertil01,
        |   passertil02, passertil03, land, distrikt, underdistrikt, aargang, raastoff, metode, alkohol, sukker,
        |   syre, lagringsgrad, produsent, grossist, distributor, emballasjetype, korktype, vareurl)
        | VALUES (:datotid, :varenummer, :varenavn, :volum, :pris, :literpris, :varetype, :produktutvalg,
        |   :butikkategori, :fylde, :friskhet, :garvestoffer, :bitterhet, :sodme, :farge, :lukt, :smak, :passertil01,
        |   :passertil02, :passertil03, :land, :distrikt, :underdistrikt, :aargang, :raastoff, :metode, :alkohol, :sukker,
        |   :syre, :lagringsgrad, :produsent, :grossist, :distributor, :emballasjetype, :korktype, :vareurl)
      """.stripMargin
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val id: Long = con.createQuery(sql, true).bind(product).executeUpdate().getKey.asInstanceOf[Long]
      con.commit(true)
      id
    } catch {
      case e: Exception =>
        con.rollback(true)
        throw new RuntimeException(e.getMessage, e)
    }
  }

  override def insertPrice(product: ProductLine, productId: Long, con: Connection): Long = {
    val sql =
      s"""
         | INSERT INTO t_price (id, product_id, datotid, varenummer, volum, pris, literpris, varetype, produktutvalg,
         |   butikkategori)
         | VALUES ( price_id_seq.nextval, $productId, :datotid, :varenummer, :volum, :pris, :literpris, :varetype, :produktutvalg,
         |   :butikkategori)
      """.stripMargin
    try {
      val id: Long = con.createQuery(sql, true)
        .bind(product).executeUpdate().getKey.asInstanceOf[Long]
      id
    } catch {
      case e: Exception =>
        throw new RuntimeException(e.getMessage, e)
    }
  }

  override def insertProduct(product: ProductLine, con: Connection): Long = {
    val sql =
      """
        | INSERT INTO t_product (id, datotid, varenummer, varenavn, varetype,
        |   fylde, friskhet, garvestoffer, bitterhet, sodme, farge, lukt, smak, passertil01,
        |   passertil02, passertil03, land, distrikt, underdistrikt, aargang, raastoff, metode, alkohol, sukker,
        |   syre, lagringsgrad, produsent, grossist, distributor, emballasjetype, korktype, vareurl)
        | VALUES (product_id_seq.nextval, :datotid, :varenummer, :varenavn, :varetype,
        |   :fylde, :friskhet, :garvestoffer, :bitterhet, :sodme, :farge, :lukt, :smak, :passertil01,
        |   :passertil02, :passertil03, :land, :distrikt, :underdistrikt, :aargang, :raastoff, :metode, :alkohol, :sukker,
        |   :syre, :lagringsgrad, :produsent, :grossist, :distributor, :emballasjetype, :korktype, :vareurl)
      """.stripMargin
    try {
      val id: Long = con.createQuery(sql, true).bind(product).executeUpdate().getKey.asInstanceOf[Long]
      id
    } catch {
      case e: Exception =>
        throw new RuntimeException(e.getMessage, e)
    }
  }

  override def updatePrice(product: ProductLine, con: Connection): Boolean = {
    val sql =
      """
        | INSERT INTO t_product ( datotid, varenummer, varenavn, volum, pris, literpris, varetype, produktutvalg,
        |   butikkategori, fylde, friskhet, garvestoffer, bitterhet, sodme, farge, lukt, smak, passertil01,
        |   passertil02, passertil03, land, distrikt, underdistrikt, aargang, raastoff, metode, alkohol, sukker,
        |   syre, lagringsgrad, produsent, grossist, distributor, emballasjetype, korktype, vareurl)
        | VALUES (:datotid, :varenummer, :varenavn, :volum, :pris, :literpris, :varetype, :produktutvalg,
        |   :butikkategori, :fylde, :friskhet, :garvestoffer, :bitterhet, :sodme, :farge, :lukt, :smak, :passertil01,
        |   :passertil02, :passertil03, :land, :distrikt, :underdistrikt, :aargang, :raastoff, :metode, :alkohol, :sukker,
        |   :syre, :lagringsgrad, :produsent, :grossist, :distributor, :emballasjetype, :korktype, :vareurl)
      """.stripMargin
    var con: Connection = null
    try {
      con = sql2o.beginTransaction()
      val id: Long = con.createQuery(sql, true).bind(product).executeUpdate().getKey.asInstanceOf[Long]
      con.commit(true)
      id != null
    } catch {
      case e: Exception =>
        con.rollback(true)
        throw new RuntimeException(e.getMessage, e)
    }
  }
}
