package no.hamre.polet.resources


import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import no.hamre.polet.rest.DomainToApiMapper
import no.hamre.polet.rest.QueryProduct
import no.hamre.polet.service.ProductDataService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping(path = ["/search"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class SearchResource(val service: ProductDataService, @Value("\${vinmonopolet.url}") val defaultUrl: String) {
  private val log = LoggerFactory.getLogger(this.javaClass)

  @RequestMapping(
      path = ["/"],
      method = [RequestMethod.PUT],
      produces = [MediaType.APPLICATION_JSON_VALUE])
  @ApiResponses(value = [
    ApiResponse(responseCode = "200", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))])
  ])
  fun findProduct(@RequestParam(name="q", required = true) q: String): ResponseEntity<List<QueryProduct>> {
    log.info("GET /search?q=$q")
    try {
      return service.findProducts(q)
          .map { DomainToApiMapper.mapToMiniProduct(it) }
          .let { p -> ResponseEntity.ok(p) }
          ?: ResponseEntity.notFound().build()
    } catch (e: Exception) {
      log.error("Exception getting products with q=$q. ${e.message}", e)
      return ResponseEntity.status(500).build()
    }
  }

}
