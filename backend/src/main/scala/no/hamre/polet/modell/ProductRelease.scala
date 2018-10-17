package no.hamre.polet.modell

import java.time.LocalDate

case class ProductRelease
(
  releaseDate: LocalDate,
  products: List[MiniProduct],
  links: List[Link]
)

case class MiniProduct
(
  id: Long,
  varenummer: String,
  varenavn: String,
  volume: Double,
  price: Double,
  links: List[Link]
)

case class Link
(
  rel: String,
  href: String
)
