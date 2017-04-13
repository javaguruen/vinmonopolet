package no.hamre.polet.resources

import javax.ws.rs.core.{MediaType, Response}
import javax.ws.rs.{GET, Path, Produces}

import com.codahale.metrics.annotation.Timed
import no.hamre.polet.dao.ProductDao
import no.hamre.polet.modell.Product
import no.hamre.polet.parser.CharsetConverter

import scala.io.Source

@Path("/products")
@Produces(Array("application/json; charset=UTF-8"))
//@Produces(Array(MediaType.APPLICATION_JSON))
class ProductResource(dao: ProductDao) {

  @GET
  @Timed
  def list: Response = {
    val products = dao.findAll
    Response.ok( products.take(100) ).build()
  }

  @GET
  @Path("/file")
  @Timed
  def loadFile: Response = {
    val filename = "produkter_short.csv"
    //val filename = "produkter.csv"
    val path = "src/main/resources"
    val products: List[String] = Source.fromFile(s"$path/$filename", "Windows-1252")
      .getLines().toList
    products.tail
      .map(l => CharsetConverter(l))
      .map(l=>Product(l.split(";")))
        .foreach(dao.create)

    Response.ok( ).build()
  }


}
