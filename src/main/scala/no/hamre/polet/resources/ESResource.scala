package no.hamre.polet.resources

import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import com.codahale.metrics.annotation.Timed
import io.swagger.annotations.Api
import no.hamre.polet.service.ProductDataService
import no.hamre.polet.util.Slf4jLogger
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404

@Api("Product related")
@Path("/")
@Produces(Array(MediaType.APPLICATION_JSON))
class ESResource(service: ProductDataService, defaultUrl: String) extends Slf4jLogger {

  @GET
  @Timed
  def findProduct(@QueryParam("q") q: String): Response = {
    //log.info(s"q=$q")
      println(q)
      Response.ok().build()
  }


}
