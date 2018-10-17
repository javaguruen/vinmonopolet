package no.hamre.polet.resources

import no.hamre.polet.dao.{H2LiquibaseDataSourceFactory, PoletDao}
import no.hamre.polet.modell.Product
import no.hamre.polet.parser.MockFileDownloader
import no.hamre.polet.service.{DownloadResult, ProductDataServiceImpl}
import org.eclipse.jetty.http.HttpStatus
import org.scalatest.FunSuite

trait ProductResourceTestData{
  val encoding = "windows-1252"
  val url = "produkter-2017-05-20-short.csv"
  val dao = new PoletDao(H2LiquibaseDataSourceFactory.createDataSource("polet"))
  val service = new ProductDataServiceImpl(dao, new MockFileDownloader)
  val resource = new ProductResource(service, url)
  val ardbeg10Id = 100
  val ardbeg10Name = "Ardbeg 10 Years Old"
}

class ProductResourceTest extends FunSuite {

  test("Insert downloaded file"){
    new ProductResourceTestData{
      val response = resource.download(url)
      assert( response.getStatus == HttpStatus.OK_200)
      val result =response.getEntity.asInstanceOf[DownloadResult]
      //val result = ObjectMapperFactory.create.readValue( response.getEntity, classOf[DownloadResult])
      assert( result.failure == 0)
      assert( result.total == 10)
      assert( result.success == 1)
    }
  }

  test("Found product should return OK (200)"){
    new ProductResourceTestData{
      resource.download(url)
      val response = resource.findProduct(ardbeg10Id)
      assert( response.getStatus == HttpStatus.OK_200)
      val ardbeg10 =response.getEntity.asInstanceOf[Product]
      assert(ardbeg10.varenavn == ardbeg10Name)
    }
  }

  test("Non-existing product should return NOT_FOUND (404)"){
    new ProductResourceTestData{
      val response = resource.findProduct(-10000)
      assert( response.getStatus == HttpStatus.NOT_FOUND_404)
    }
  }
}
