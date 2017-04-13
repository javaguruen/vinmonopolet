package no.hamre.polet

import io.dropwizard.Application
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.{Bootstrap, Environment}
import no.hamre.polet.dao.{H2LiquibaseDataSourceFactory, ProductDaoImpl}
import no.hamre.polet.resources.ProductResource

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
    val productDao = new ProductDaoImpl(dataSource)
    val productResource = new ProductResource(productDao)

    environment.jersey().register(productResource)
  }

  override def initialize(bootstrap: Bootstrap[Config]): Unit = {
    bootstrap.addBundle(new MigrationsBundle[Config]() {
      override
      def getDataSourceFactory(configuration: Config): DataSourceFactory = {
        configuration.database
      }
    })
  }
}
