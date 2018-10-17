package no.hamre.polet.modell

import java.time.LocalDateTime

import scala.beans.BeanProperty

case class Price
(
  @BeanProperty id: Long,
  @BeanProperty datotid: LocalDateTime,
  @BeanProperty varenummer: String,
  @BeanProperty volum: Double,
  @BeanProperty pris: Double,
  @BeanProperty literpris: Double,
  @BeanProperty produktutvalg: String,
  @BeanProperty butikkategori: String,
  @BeanProperty updated: LocalDateTime
)

case class Product
(
  @BeanProperty id: Long,
  @BeanProperty datotid: LocalDateTime,
  @BeanProperty varenummer: String,
  @BeanProperty varenavn: String,
  @BeanProperty varetype: String,
  @BeanProperty volum: Double,
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
  @BeanProperty active: Boolean = true,
  @BeanProperty updated: LocalDateTime = LocalDateTime.now(),
  @BeanProperty prices: List[Price] = List()
)

//2014-10-22T00:56:50;1101;L�iten Linie;0,70;399,90;571,30;
//Akevitt;Basisutvalget;Butikkategori 3;
//0;0;0;0;0;;;;;;;Norge;�vrige;�vrige;;Poteter, krydder;16 mnd p� fat;
// 41,50;5,00;Ukjent;;;Arcus AS;Vectura AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-1101
