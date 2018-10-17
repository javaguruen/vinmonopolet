package no.hamre.polet.resources

import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import com.codahale.metrics.annotation.Timed
import io.swagger.annotations.Api
import no.hamre.polet.service.ProductDataService
import no.hamre.polet.util.Slf4jLogger
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404

@Api("Product related")
@Path("/products")
@Produces(Array(MediaType.APPLICATION_JSON))
class ProductResource(service: ProductDataService, defaultUrl: String) extends Slf4jLogger {

  @PUT
  @Path("/download")
  @Timed
  def download(@QueryParam("url") url: String): Response = {
    log.info(s"PUT /products/download?url=$url")
    val downloadUrl = url match {
      case null | "" => defaultUrl
      case s: String => s
    }
    val result = service.updateFromWeb(downloadUrl)
    Response.ok(result).build()
  }

  @GET
  @Timed
  @Path("{id}")
  def findProduct(@PathParam("id") id: Long): Response = {
    log.info(s"GET /products/$id")
    try {
      service.findProduct(id)
        .map(p => Response.ok(p).build())
        .getOrElse(Response.status(NOT_FOUND_404).build())
    } catch{
      case e: Exception =>
        log.error(s"Exception getting product=$id")
        Response.serverError().build()
    }
  }


  @GET
  @Timed
  def findAllProduct(): Response = {
    log.info(s"GET /products")
    try {
      service.findAllProduct() match {
        case Nil =>
          Response.status(NOT_FOUND_404).build()
        case xs =>
          Response.ok(xs).build()
      }
    } catch{
      case e: Exception =>
        log.error(s"Exception getting product")
        Response.serverError().build()
    }
  }

  @GET
  @Timed
  @Path("/latest")
  def findLatestReleases(): Response = {
    log.info(s"GET /products/latest")
    try {
      val releases = service.findLatestReleases()
      Response.ok(releases).build()
    } catch{
      case e: Exception =>
        log.error(s"Exception getting latest releases", e)
        Response.serverError().build()
    }
  }

  @GET
  @Timed
  @Path("/releases")
  def findProductByReleaseDate(): Response = {
    log.info(s"GET /products/releases")
    try {
      val releases = service.findProductByReleaseDate()
      Response.ok(releases).build()
    } catch{
      case e: Exception =>
        log.error(s"Exception getting products by release date")
        Response.serverError().build()
    }
  }
}
