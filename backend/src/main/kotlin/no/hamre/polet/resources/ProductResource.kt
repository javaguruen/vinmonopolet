package no.hamre.polet.resources


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import no.hamre.polet.service.DownloadResult
import no.hamre.polet.service.ProductDataService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping(path = ["/api/v1/products"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class ProductResource(val service: ProductDataService, @Value("defaultUrl") val defaultUrl: String) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @RequestMapping(
    path = ["/scrape"],
    method = [RequestMethod.PUT],
    produces = [MediaType.APPLICATION_JSON_VALUE]
  )
/*
  @Operation(
    summary = "Triggers an update from vinmonopolet.no",
    responses = [
      ApiResponse(
        responseCode = "200", description = "Successfully persisted.",
        content = [Content(schema = Schema(implementation = Long::class))]
      )
    ]
  )
*/
  @Operation(summary = "Triggers an update from vinmonopolet.no")
  @ApiResponses(
      ApiResponse(
        responseCode = "200", description = "Successfully persisted standalone @ApiResponse.",
        content = [Content(schema = Schema(implementation = Long::class))]
      )
  )
  fun downloadFromApi(): ResponseEntity<DownloadResult> {
    log.info("PUT /products/scrape")
    val result = service.updateFromApi()
    return ResponseEntity.ok(result)
  }
}
