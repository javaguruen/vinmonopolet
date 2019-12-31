package no.hamre.polet

import io.dropwizard.Application
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import no.hamre.polet.dao.H2LiquibaseDataSourceFactory
import no.hamre.polet.dao.PoletDao
import no.hamre.polet.parser.FileDownloaderImpl
import no.hamre.polet.resources.ProductResource
import no.hamre.polet.service.ProductDataServiceImpl
import no.hamre.polet.vinmonopolet.VinmonopoletClientImpl
import org.constretto.dropwizard.ConstrettoBundle


class App : Application<Config>() {
  
  companion object {
    @JvmStatic
    fun main(args: Array<String>){
      App().run(*args)
    }
  }

  override fun run(config: Config, environment: Environment){
    ObjectMapperFactory.configure(environment.objectMapper)

    val jerseyClient = JerseyClientBuilder(environment)
        .using(config.jerseyClient)
        .build(getName());

    environment.jersey().register(VinmonopoletClientImpl(
        "https://apis.vinmonopolet.no/products/v0/details-normal?maxResults=100",
        jerseyClient, ""));

    val dataSource = when (config.useH2Database) {
      true -> H2LiquibaseDataSourceFactory.createDataSource("polet")
      false -> config.database.build(environment.metrics(), "polet")
    }

    if( config.loadTestdata ){
      TestdataLoader.load(dataSource)
    }

    val productDao = PoletDao(dataSource)
    val productService = ProductDataServiceImpl(productDao, FileDownloaderImpl(config.dataEncoding))
    val productResource = ProductResource(productService, config.dataUrl)

    environment.jersey().register(productResource)
  }

  override fun initialize(bootstrap: Bootstrap<Config>) {
    ObjectMapperFactory.configure( bootstrap.objectMapper )
    bootstrap.addBundle( ConstrettoBundle<Config>())
    bootstrap.addBundle( object : MigrationsBundle<Config>() {
      override
      fun getDataSourceFactory(configuration: Config): DataSourceFactory {
        return configuration.database
      }
    })

    bootstrap.addBundle( object : SwaggerBundle<Config>() {
      override fun getSwaggerBundleConfiguration(t: Config): SwaggerBundleConfiguration {
        return t.swaggerBundleConfiguration
      }
    })
    // Map requests to /dashboard/${1} to be found in the class path at /assets/${1}.
    bootstrap.addBundle( ConfiguredAssetsBundle("/public/", "/", "index.html"))
  }
}
