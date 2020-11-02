package no.hamre.polet.resources

import no.hamre.polet.dao.H2LiquibaseDataSourceFactory
import no.hamre.polet.dao.PoletDao
import no.hamre.polet.resources.ProductResourceTestData.ardbeg10Id
import no.hamre.polet.resources.ProductResourceTestData.ardbeg10Name
import no.hamre.polet.resources.ProductResourceTestData.url
import org.junit.jupiter.api.Test

import no.hamre.polet.modell.Product
import no.hamre.polet.parser.MockFileDownloader
import no.hamre.polet.service.DownloadResult
import no.hamre.polet.service.ProductDataServiceImpl
import no.hamre.polet.vinmonopolet.VinmonopoletClientImpl

object ProductResourceTestData {
  val encoding = "windows-1252"
  val url = "produkter-2017-05-20-short.csv"
  val dao: PoletDao = TODO() //PoletDao(H2LiquibaseDataSourceFactory.createDataSource("polet"))
  //val service = ProductDataServiceImpl(dao, MockFileDownloader(), VinmonopoletClientImpl("", JerseyClientBuilder.createClient(), ""))
  //val resource = ProductResource(service, url)
  val ardbeg10Id = 100L
  val ardbeg10Name = "Ardbeg 10 Years Old"
}

class ProductResourceTest {

  @Test
  fun `Insert downloaded file`() {
/*
    val response = resource.download(url)
    assert(response.status == HttpStatus.OK_200)
    val result = response.entity as DownloadResult
    assert(result.failure == 0)
    assert(result.total == 10)
    assert(result.success == 1)
 */
  }

  @Test
  fun `Found product should return OK (200)`() {
/*
    resource.download(url)
    val response = resource.findProduct(ardbeg10Id)
    assert(response.status == HttpStatus.OK_200)
    val ardbeg10 = response.entity as Product
    assert(ardbeg10.varenavn == ardbeg10Name)
*/
  }

  @Test
  fun `Non-existing product should return NOT_FOUND (404)`() {
/*
      val response = resource.findProduct(-10000)
      assert(response.status == HttpStatus.NOT_FOUND_404)
*/
  }
}
