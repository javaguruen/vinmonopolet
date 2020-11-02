package no.hamre.polet.resources


import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import no.hamre.polet.modell.LatestProductchange
import no.hamre.polet.modell.Product
import no.hamre.polet.modell.ProductRelease
import no.hamre.polet.service.DownloadResult
import no.hamre.polet.service.ProductDataService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/api/v1/products"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class ProductResource(val service: ProductDataService, @Value("defaultUrl") val defaultUrl: String) {
  private val log = LoggerFactory.getLogger(this.javaClass)

  @RequestMapping(
      path = ["/download"],
      method = [RequestMethod.PUT],
      produces = [MediaType.APPLICATION_JSON_VALUE])
  @ApiResponses(value = [
    ApiResponse(responseCode = "201", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))])
  ])
  fun download(@RequestParam(name = "url", required = true) url: String): ResponseEntity<DownloadResult> {
    log.info("PUT /products/download?url=$url")
    val result = service.updateFromWeb(url)
    return ResponseEntity.ok(result)
  }

  @RequestMapping(
      path = ["/scrape"],
      method = [RequestMethod.PUT],
      produces = [MediaType.APPLICATION_JSON_VALUE])
  @ApiResponses(value = [
    ApiResponse(responseCode = "200", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))])
  ])
  fun downloadFromApi(): ResponseEntity<DownloadResult> {
    log.info("PUT /products/scrape")
    val result = service.updateFromApi()
    return ResponseEntity.ok(result)
  }

  @RequestMapping(
      path = ["/{id}"],
      method = [RequestMethod.GET],
      produces = [MediaType.APPLICATION_JSON_VALUE])
  @ApiResponses(value = [
    ApiResponse(responseCode = "200", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))])
  ])
  fun findProduct(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Product> {
    log.info("GET /products/$id")
    try {
      return service.findProductById(id)
          ?.let { p -> ResponseEntity.ok(p) }
          ?: ResponseEntity.notFound().build()
    } catch (e: Exception) {
      log.error("Exception getting product=$id. ${e.message}")
      return ResponseEntity.status(500).build()
    }
  }

  @RequestMapping(
      method = [RequestMethod.GET],
      produces = [MediaType.APPLICATION_JSON_VALUE])
  @ApiResponses(value = [
    ApiResponse(responseCode = "200", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))])
  ])
  fun findAllProduct(): ResponseEntity<List<Product>> {
    log.info("GET /product")
    try {
      val products = service.findAllProduct()
      return when (products.size) {
        0 -> ResponseEntity.notFound().build()
        else -> ResponseEntity.ok(products)
      }
    } catch (e: Exception) {
      log.error("Exception getting product")
      return ResponseEntity.status(500).build()
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
    log.info("GET /products/latest")
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
}
