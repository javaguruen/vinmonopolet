package no.hamre.polet

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@SpringBootApplication
class Application

fun main(args: Array<String>) {
  //runApplication<AppApplication>(*args)
  SpringApplication.run(Application::class.java, *args)
}

@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "vinmonopolet")
data class Config(
  @Value("\${vinmonopolet.url}") val url: String,
  @Value("\${vinmonopolet.apiKey}") val apiKey: String
)

/*
    val dataSource = when (config.useH2Database) {
      true -> H2LiquibaseDataSourceFactory.createDataSource("polet")
      false -> config.database.build(environment.metrics(), "polet")
    }

    if (config.loadTestdata) {
      TestdataLoader.load(dataSource)
    }

    val productDao = PoletDao(dataSource)
    val productService = ProductDataServiceImpl(
        productDao,
        FileDownloaderImpl(config.dataEncoding),
        vinmonopoletClientImpl)
    val productResource = ProductResource(productService, config.dataUrl)
    val searchResource = SearchResource(productService, config.dataUrl)

    environment.jersey()
        .register(productResource)
    environment.jersey()
        .register(searchResource)
  }

  override fun initialize(bootstrap: Bootstrap<Config>) {
    ObjectMapperFactory.configure(bootstrap.objectMapper)
    bootstrap.addBundle(ConstrettoBundle<Config>())
    bootstrap.addBundle(object : MigrationsBundle<Config>() {
      override
      fun getDataSourceFactory(configuration: Config): DataSourceFactory {
        return configuration.database
      }
    })

    bootstrap.addBundle(object : SwaggerBundle<Config>() {
      override fun getSwaggerBundleConfiguration(t: Config): SwaggerBundleConfiguration {
        return t.swaggerBundleConfiguration
      }
    })
    // Map requests to /dashboard/${1} to be found in the class path at /assets/${1}.
    bootstrap.addBundle(ConfiguredAssetsBundle("/public/", "/", "index.html"))
  }
}
*/