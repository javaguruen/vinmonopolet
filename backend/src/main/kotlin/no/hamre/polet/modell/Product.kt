package no.hamre.polet.modell

import java.time.LocalDateTime

data class Price(
    val id: Long,
    val datotid: LocalDateTime,
    val varenummer: String,
    val volum: Double,
    val pris: Double,
    val literpris: Double,
    val produktutvalg: String,
    val butikkategori: String,
    val updated: LocalDateTime
)

data class Product(
    val id: Long,
    val datotid: LocalDateTime,
    val varenummer: String,
    val varenavn: String,
    val varetype: String,
    val volum: Double,
    val fylde: Int,
    val friskhet: Int,
    val garvestoffer: Int,
    val bitterhet: Int,
    val sodme: Int,
    val farge: String? = null,
    val lukt: String? = null,
    val smak: String? = null,
    val passertil01: String? = null,
    val passertil02: String? = null,
    val passertil03: String? = null,
    val land: String,
    val distrikt: String? = null,
    val underdistrikt: String? = null,
    val aargang: Int? = null,
    val raastoff: String? = null,
    val metode: String? = null,
    val alkohol: Double,
    val sukker: String,
    val syre: String? = null,
    val lagringsgrad: String? = null,
    val produsent: String,
    val grossist: String,
    val distributor: String,
    val emballasjetype: String,
    val korktype: String? = null,
    val vareurl: String,
    val active: Boolean = true,
    val updated: LocalDateTime? = LocalDateTime.now(),
    val prices: List<Price> = listOf()
)

data class LatestProductchange(
    val id: Long,
    val datotid: LocalDateTime,
    val varenummer: String,
    val varenavn: String,
    val varetype: String,
    val volum: Double,
    val land: String,
    val distrikt: String?,
    val underdistrikt: String?,
    val alkohol: Double,
    val produsent: String,
    val vareurl: String,
    val active: Boolean = true,
    val updated: LocalDateTime? = LocalDateTime.now(),
    val price: Price,
    val priceLiter: Double,
    val priceChangeKr: Double?,
    val priceChangePercent: Double?
)

//2014-10-22T00:56:50;1101;L�iten Linie;0,70;399,90;571,30;
//Akevitt;Basisutvalget;Butikkategori 3;
//0;0;0;0;0;;;;;;;Norge;�vrige;�vrige;;Poteter, krydder;16 mnd p� fat;
// 41,50;5,00;Ukjent;;;Arcus AS;Vectura AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-1101
