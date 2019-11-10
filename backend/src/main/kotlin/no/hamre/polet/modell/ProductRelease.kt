package no.hamre.polet.modell

import java.time.LocalDate


data class ProductRelease
(
    val releaseDate: LocalDate,
    val products: List<MiniProduct>,
    val links: List<Link>
)

data class MiniProduct
(
    val id: Long,
    val varenummer: String,
    val varenavn: String,
    val volume: Double,
    val price: Double,
    val links: List<Link>
)

data class Link
(
    val rel: String,
    val href: String
)
