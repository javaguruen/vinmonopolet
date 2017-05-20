package no.hamre.polet.resources

import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import com.codahale.metrics.annotation.Timed
import no.hamre.polet.service.ProductDataService
import no.hamre.polet.util.Slf4jLogger
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404

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
  @Path("{productid}")
  def findProduct(@PathParam("productid") productId: String): Response = {
    log.info(s"GET /products/$productId")
    try {
      service.findProduct(productId)
        .map(p => Response.ok(p).build())
        .getOrElse(Response.status(NOT_FOUND_404).build())
    } catch{
      case e: Exception =>
        log.error(s"Exception getting product=$productId")
        Response.serverError().build()
    }
  }


}
