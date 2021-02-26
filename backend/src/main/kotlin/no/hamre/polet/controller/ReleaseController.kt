package no.hamre.polet.controller


import no.hamre.polet.modell.LatestProductchange
import no.hamre.polet.service.Oppdateringsstatus
import no.hamre.polet.service.ProductDataService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReleaseController(private val service: ProductDataService) : ReleaseApi {

  override fun sisteEndringer(): ResponseEntity<List<LatestProductchange>> {
    log.info("GET /api/v1/releases/latest")
    try {
      val releases: List<LatestProductchange> = service.findLatestReleases()
          .map {
            LatestProductchange(
                it.id,
                it.datotid,
                it.varenummer,
                it.varenavn,
                it.varetype,
                it.volum,
                it.land,
                it.distrikt,
                it.underdistrikt,
                it.alkohol,
                it.produsent,
                it.vareurl,
                it.active,
                it.updated,
                it.prices[0],
                it.prices[0].literpris,
                if (it.prices.size > 1) (it.prices[0].pris - it.prices[1].pris) else null,
                if (it.prices.size > 1) ((it.prices[0].pris - it.prices[1].pris)) / (it.prices[1].pris) * 100 else null
            )
          }
      return ResponseEntity.ok(releases)
    } catch (e: Exception) {
      log.error("Exception getting latest release", e)
      return ResponseEntity.status(500).build()
    }
  }

/*
  override fun findProductByReleaseDate(): ResponseEntity<List<ProductRelease>> {
    log.info("GET /products/release")
    return try {
      val releases = service.findProductByReleaseDate()
      ResponseEntity.ok(releases)
    } catch (e: Exception) {
      log.error("Exception getting products by release date")
      ResponseEntity.status(500).build()
    }
  }
*/

  override fun updateFromVinmonopolet(): ResponseEntity<Oppdateringsstatus> {
    log.info("PUT /products/scrape")
    val result = service.updateFromApi()
    return ResponseEntity.ok(result)
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
