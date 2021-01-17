package no.hamre.polet.modell

import java.time.LocalDateTime

data class Pris(
    val id: Long,
    val datotid: LocalDateTime,
    val varenummer: String,
    val volum: Double,
    val pris: Double,
    val literpris: Double,
    val produktutvalg: String,
    val updated: LocalDateTime
)

data class Whisky(
    val id: Long,
    val datotid: LocalDateTime,
    val varenummer: String,
    val varenavn: String,
    val varetype: String,
    val volum: Double,
    val farge: String? = null,
    val lukt: String? = null,
    val smak: String? = null,
    val land: String,
    val distrikt: String? = null,
    val underdistrikt: String? = null,
    val aargang: Int? = null,
    val raastoff: String? = null,
    val metode: String? = null,
    val alkohol: Double,
    val produsent: String,
    val grossist: String,
    val distributor: String,
    val vareurl: String,
    val active: Boolean = true,
    val updated: LocalDateTime? = LocalDateTime.now(),
    val prices: List<Pris> = listOf()
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
    val price: Pris,
    val priceLiter: Double,
    val priceChangeKr: Double?,
    val priceChangePercent: Double?
)