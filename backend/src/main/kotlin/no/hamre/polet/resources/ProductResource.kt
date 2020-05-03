package no.hamre.polet.resources


import com.codahale.metrics.annotation.Timed
import io.swagger.annotations.Api
import no.hamre.polet.service.ProductDataService
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404
import org.slf4j.LoggerFactory
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api("Product related")
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
class ProductResource(val service: ProductDataService, val defaultUrl: String) {
  private val log = LoggerFactory.getLogger(this.javaClass)

  @PUT
  @Path("/download")
  @Timed
  fun download(@QueryParam("url") url: String?): Response {
    log.info("PUT /products/download?url=$url")
    val downloadUrl = when (url) {
      null, "" -> defaultUrl
      else -> url
    }
    val result = service.updateFromWeb(downloadUrl)
    return Response.ok(result).build()
  }

  @PUT
  @Path("/scrape")
  @Timed
  fun downloadFromApi(): Response {
    log.info("PUT /products/scrape")
    val result = service.updateFromApi()
    return Response.ok(result).build()
  }

  @GET
  @Timed
  @Path("{id}")
  fun findProduct(@PathParam("id") id: Long): Response {
    log.info("GET /products/$id")
    try {
      return service.findProduct(id)
          ?.let { p -> Response.ok(p).build() }
          ?: Response.status(NOT_FOUND_404).build()
    } catch (e: Exception) {
      log.error("Exception getting product=$id. ${e.message}")
      return Response.serverError().build()
    }
  }

  @GET
  @Timed
  fun findAllProduct(): Response {
    log.info("GET /product")
    try {
      val products = service.findAllProduct()
      return when (products.size) {
        0 -> Response.status(NOT_FOUND_404).build()
        else -> Response.ok(products).build()
      }
    } catch (e: Exception) {
      log.error("Exception getting product")
      return Response.serverError().build()
    }
  }

  @GET
  @Timed
  @Path("/latest")
  fun findLatestReleases(): Response {
    log.info("GET /products/latest")
    try {
      val releases = service.findLatestReleases()
      return Response.ok(releases).build()
    } catch (e: Exception) {
      log.error("Exception getting latest release", e)
      return Response.serverError().build()
    }
  }

  @GET
  @Timed
  @Path("/release")
  fun findProductByReleaseDate(): Response {
    log.info("GET /products/release")
    return try {
      val releases = service.findProductByReleaseDate()
      Response.ok(releases).build()
    } catch (e: Exception) {
      log.error("Exception getting products by release date")
      Response.serverError().build()
    }
  }
}
