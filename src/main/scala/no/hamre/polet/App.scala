package no.hamre.polet

import io.dropwizard.Application
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.{Bootstrap, Environment}
import io.federecio.dropwizard.swagger.{SwaggerBundle, SwaggerBundleConfiguration}
import no.hamre.polet.dao.{H2LiquibaseDataSourceFactory, PoletDao}
import no.hamre.polet.parser.FileDownloaderImpl
import no.hamre.polet.resources.ProductResource
import no.hamre.polet.service.ProductDataServiceImpl

object App {
  def main(args: Array[String]): Unit = {
    new App().run(args: _*)
  }

}

class App() extends Application[Config] {

  override def run(config: Config, environment: Environment): Unit = {
    ObjectMapperFactory.configure(environment.getObjectMapper)

    val dataSource = config.useH2Database match {
      case true => H2LiquibaseDataSourceFactory.createDataSource("polet")
      case false => config.database.build(environment.metrics(), "polet")
    }
    val productDao = new PoletDao(dataSource)
    val productService = new ProductDataServiceImpl(productDao, new FileDownloaderImpl(config.dataEncoding))
    val productResource = new ProductResource(productService, config.dataUrl)

    environment.jersey().register(productResource)
  }

  override def initialize(bootstrap: Bootstrap[Config]): Unit = {
    bootstrap.addBundle(new MigrationsBundle[Config]() {
      override
      def getDataSourceFactory(configuration: Config): DataSourceFactory = {
        configuration.database
      }
    })
    bootstrap.addBundle(new SwaggerBundle[Config] {
      override def getSwaggerBundleConfiguration(t: Config): SwaggerBundleConfiguration = {
        t.swaggerBundleConfiguration
      }
    })
  }
}
