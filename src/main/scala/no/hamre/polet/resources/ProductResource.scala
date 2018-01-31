package no.hamre.polet.resources

import java.util.Optional
import javax.validation.constraints.NotNull
import javax.ws.rs.core.{MediaType, Response}
import javax.ws.rs._

import com.codahale.metrics.annotation.Timed
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.{Api, ApiModelProperty, ApiResponse, ApiResponses}
import no.hamre.polet.modell.Product
import no.hamre.polet.service.ProductDataService
import no.hamre.polet.util.Slf4jLogger
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404

import scala.annotation.meta.field
import scala.beans.BeanProperty

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
  @ApiResponses(
    Array(
      new ApiResponse(code = 200, message = "user deteled",
        response = classOf[Product]
      ),
      new ApiResponse(code = 400, message = "Invalid username supplied"),
      new ApiResponse(code = 404, message = "User not found")
    )
  )
  def findProduct(@PathParam("id") id: Long): Response = {
    log.info(s"GET /products/$id")
    try {
      service.findProduct(id)
        .map(p => Response.ok(p).build())
        .getOrElse(Response.status(NOT_FOUND_404).build())
    } catch {
      case e: Exception =>
        log.error(s"Exception getting product=$id")
        Response.serverError().build()
    }
  }

  case class SwTest
  (
    noAnnotation: String,
    noAnnotationOption: Option[String],
    @JsonProperty jsonPropDefault: String,
    @JsonProperty(required = true) jsonPropReq: String,
    @JsonProperty(required = false) jsonPropNotReq: Option[String],
    @(JsonProperty@field)(required = true) jsonPropFieldReq: String,
    @(JsonProperty@field)(required = false) jsonPropFieldNotReq: Option[String],
    @BeanProperty beanProp: String,
    @BeanProperty beanPropOption: Option[String],
    @NotNull notNull: String,
    optionNone: Option[String] = None,
    javaOptional: Optional[String],
    javaOptionalInt: Optional[Int],
    @ApiModelProperty(dataType = "text", required = false) apiModProp: Option[String]
  )

  @GET
  @Timed
  @Path("{id}/2")
  @ApiResponses(
    Array(
      new ApiResponse(code = 200, message = "user deteled",
        response = classOf[SwTest]
      ),
      new ApiResponse(code = 400, message = "Invalid username supplied"),
      new ApiResponse(code = 404, message = "User not found")
    )
  )
  def findProduct2(@PathParam("id") id: Long): Response = {
    Response.ok.build()
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
    } catch {
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
    } catch {
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
    } catch {
      case e: Exception =>
        log.error(s"Exception getting products by release date")
        Response.serverError().build()
    }
  }
}
