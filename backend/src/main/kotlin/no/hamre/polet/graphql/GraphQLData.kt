package no.hamre.polet.graphql

import no.hamre.polet.modell.Product
import java.time.LocalDateTime

object GraphQLData {
    val produkt = listOf(Product(
        id = 1,
        datotid = LocalDateTime.now(),
        varenummer = "121212",
        varenavn = "Best whisky",
        varetype = "Whisky",
        volum = 0.7,
        fylde = 1,
        friskhet = 1,
        garvestoffer = 1,
        bitterhet = 1,
        sodme = 1,
        land = "Scotland",
        alkohol = 40.0,
        produsent = "Distillers INC",
        distrikt = "Highlands",
        underdistrikt = "down under",
        aargang = 2020,
        distributor = "Distributør",
        vareurl = "http://vinmonopolet.no",
        emballasjetype = "Glass",
        grossist = "Grosist",
        sukker = "",
        prices = emptyList()
    )
    )
}