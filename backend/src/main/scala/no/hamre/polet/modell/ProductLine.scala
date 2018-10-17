package no.hamre.polet.modell

import java.time.LocalDateTime

import scala.beans.BeanProperty

object ProductLine {

  def toInt(s: String): Int = {
    Option(s).filter(s => s.length > 0).map(s => s.toInt).getOrElse(null).asInstanceOf[Int]
  }

  def toDouble(s: String): Double = {
    Option(s).filter(s => s.length > 0).map(s => s.replace(",", ".")).map(s => s.toDouble).getOrElse(null).asInstanceOf[Double]
  }

  def toLocalDateTime(s: String): LocalDateTime = {
    Option(s).filter(s => s.length > 0).map(s => LocalDateTime.parse(s)).getOrElse(null).asInstanceOf[LocalDateTime]
  }

  def apply(line: Array[String]): ProductLine = {
    ProductLine(
      null.asInstanceOf[Long],
      toLocalDateTime(line(0)),
      line(1),
      line(2),
      toDouble(line(3)),
      toDouble(line(4)),
      toDouble(line(5)),
      line(6),
      line(7),
      line(8),
      toInt(line(9)),
      toInt(line(10)),
      toInt(line(11)),
      toInt(line(12)),
      line(13),
      line(14),
      line(15),
      line(16),
      line(17),
      line(18),
      line(19),
      line(20),
      line(21),
      line(22),
      toInt(line(23)),
      line(24),
      line(25),
      toDouble(line(26)),
      line(27),
      line(28),
      line(29),
      line(30),
      line(31),
      line(32),
      line(33),
      line(34),
      line(35).trim(),
      null
    )
  }
}

case class ProductLine
(
  @BeanProperty id: Long,
  @BeanProperty datotid: LocalDateTime,
  @BeanProperty varenummer: String,
  @BeanProperty varenavn: String,
  @BeanProperty volum: Double,
  @BeanProperty pris: Double,
  @BeanProperty literpris: Double,
  @BeanProperty varetype: String,
  @BeanProperty produktutvalg: String,
  @BeanProperty butikkategori: String,
  @BeanProperty fylde: Int,
  @BeanProperty friskhet: Int,
  @BeanProperty garvestoffer: Int,
  @BeanProperty bitterhet: Int,
  @BeanProperty sodme: String,
  @BeanProperty farge: String,
  @BeanProperty lukt: String,
  @BeanProperty smak: String,
  @BeanProperty passertil01: String,
  @BeanProperty passertil02: String,
  @BeanProperty passertil03: String,
  @BeanProperty land: String,
  @BeanProperty distrikt: String,
  @BeanProperty underdistrikt: String,
  @BeanProperty aargang: Int,
  @BeanProperty raastoff: String,
  @BeanProperty metode: String,
  @BeanProperty alkohol: Double,
  @BeanProperty sukker: String,
  @BeanProperty syre: String,
  @BeanProperty lagringsgrad: String,
  @BeanProperty produsent: String,
  @BeanProperty grossist: String,
  @BeanProperty distributor: String,
  @BeanProperty emballasjetype: String,
  @BeanProperty korktype: String,
  @BeanProperty vareurl: String,
  @BeanProperty updated: LocalDateTime
)

//2014-10-22T00:56:50;1101;L�iten Linie;0,70;399,90;571,30;
//Akevitt;Basisutvalget;Butikkategori 3;
//0;0;0;0;0;;;;;;;Norge;�vrige;�vrige;;Poteter, krydder;16 mnd p� fat;
// 41,50;5,00;Ukjent;;;Arcus AS;Vectura AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-1101
