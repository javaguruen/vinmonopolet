package no.hamre.polet.rest

import no.hamre.polet.modell.Product

object DomainToApiMapper {
  fun mapToMiniProduct(model: Product): QueryProduct {
    return QueryProduct(
        id = model.id,
        varenummer = model.varenummer,
        varenavn = model.varenavn,
        land = model.land,
        distrikt = model.distrikt,
        underdistrikt = model.underdistrikt,
        produsent = model.produsent
    )
  }
}