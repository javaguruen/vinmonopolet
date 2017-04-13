package no.hamre.polet.dao

import java.time.ZoneId
import javax.sql.DataSource

import no.hamre.polet.modell.Product
import org.sql2o.quirks.PostgresQuirks
import org.sql2o.{Connection, Sql2o}
import scala.collection.JavaConverters._

trait ProductDao {
  def findAll: List[Product]

  def create(product: Product): Long
}

class ProductDaoImpl(dataSource: DataSource) extends ProductDao {
  val sql2o = new Sql2o(dataSource, new PostgresQuirks)

  override def findAll: List[Product] = {
    val sql =
      """
        | SELECT * from product
      """.stripMargin
    var con: Connection = null
    try {
      val defaultZoneId = ZoneId.systemDefault()
      con = sql2o.beginTransaction()
      val products = con.createQuery(sql)
        .executeAndFetchTable().rows().asScala.map(r =>
        Product(
          r.getLong("id"),
          r.getDate("datotid").toInstant.atZone(defaultZoneId).toLocalDateTime(),
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
          r.getDate("updated").toInstant.atZone(defaultZoneId).toLocalDateTime()
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


override def create (product: Product): Long = {
  val sql =
  """
    | INSERT INTO product ( datotid, varenummer, varenavn, volum, pris, literpris, varetype, produktutvalg,
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
  con = sql2o.beginTransaction ()
  val id: Long = con.createQuery (sql, true).bind (product).executeUpdate ().getKey.asInstanceOf[Long]
  con.commit (true)
  id
} catch {
  case e: Exception =>
  con.rollback (true)
  throw new RuntimeException (e.getMessage, e)
}
}
}
