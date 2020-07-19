package no.hamre.polet.rest

data class QueryProduct(
    val id: Long,
    val varenummer: String,
    val varenavn: String,
    val land: String,
    val distrikt: String?,
    val underdistrikt: String?,
    val produsent: String
)