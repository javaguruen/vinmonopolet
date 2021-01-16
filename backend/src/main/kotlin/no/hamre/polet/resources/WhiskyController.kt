package no.hamre.polet.resources


import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import no.hamre.polet.modell.LatestProductchange
import no.hamre.polet.modell.ProductRelease
import no.hamre.polet.modell.Whisky
import no.hamre.polet.service.ProductDataService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping(path = ["/api/v1/whiskies"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class WhiskyController(val service: ProductDataService, @Value("defaultUrl") val defaultUrl: String) {

  @RequestMapping(
      path = ["/{id}"],
      method = [RequestMethod.GET],
      produces = [MediaType.APPLICATION_JSON_VALUE])
  @ApiResponses(value = [
    ApiResponse(responseCode = "200", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))])
  ])
  fun findProduct(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Whisky> {
    log.info("GET /api/v1/whiskies/$id")
    return try {
      service.findProductById(id)
        ?.let { p -> ResponseEntity.ok(p) }
        ?: ResponseEntity.notFound().build()
    } catch (e: Exception) {
      log.error("Exception getting product=$id. ${e.message}")
      ResponseEntity.status(500).build()
    }
  }

  @RequestMapping(
      method = [RequestMethod.GET],
      produces = [MediaType.APPLICATION_JSON_VALUE])
  @ApiResponses(value = [
    ApiResponse(responseCode = "200", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))])
  ])
  fun findAllProduct(): ResponseEntity<List<Whisky>> {
    log.info("GET /api/v1/whiskies")
    return try {
      val products = service.findAllProduct()
      when (products.size) {
        0 -> ResponseEntity.notFound().build()
        else -> ResponseEntity.ok(products)
      }
    } catch (e: Exception) {
      log.error("Exception getting product")
      ResponseEntity.status(500).build()
    }
  }

  @RequestMapping(
      path = ["/latest"],
      method = [RequestMethod.GET],
      produces = [MediaType.APPLICATION_JSON_VALUE])
  @ApiResponses(value = [
    ApiResponse(responseCode = "200", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))])
  ])
  fun findLatestReleases(): ResponseEntity<List<LatestProductchange>> {
    log.info("GET /api/v1/whiskies/latest")
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

  @RequestMapping(
      path = ["/release"],
      method = [RequestMethod.GET],
      produces = [MediaType.APPLICATION_JSON_VALUE])
  @ApiResponses(value = [
    ApiResponse(responseCode = "200", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))])
  ])
  fun findProductByReleaseDate(): ResponseEntity<List<ProductRelease>> {
    log.info("GET /products/release")
    return try {
      val releases = service.findProductByReleaseDate()
      ResponseEntity.ok(releases)
    } catch (e: Exception) {
      log.error("Exception getting products by release date")
      ResponseEntity.status(500).build()
    }
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
