package no.hamre.polet.resources


import com.codahale.metrics.annotation.Timed
import io.swagger.annotations.Api
import no.hamre.polet.rest.DomainToApiMapper
import no.hamre.polet.service.ProductDataService
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404
import org.slf4j.LoggerFactory
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api("Search for products")
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
class SearchResource(val service: ProductDataService, val defaultUrl: String) {
  private val log = LoggerFactory.getLogger(this.javaClass)

  @GET
  @Timed
  @Path("/")
  fun findProduct(@QueryParam("q") q: String): Response {
    log.info("GET /search?q=$q")
    try {
      return service.findProducts(q)
          .map { DomainToApiMapper.mapToMiniProduct(it) }
          .let { p -> Response.ok(p).build() }
          ?: Response.status(NOT_FOUND_404).build()
    } catch (e: Exception) {
      log.error("Exception getting products with q=$q. ${e.message}", e)
      return Response.serverError().build()
    }
  }

}
