package no.hamre.polet

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


//@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
class Application

fun main(args: Array<String>) {
  SpringApplication.run(Application::class.java, *args)
}

@Configuration
class OasConfiguration() {

  @Bean
  fun openAPIInfo(): OpenAPI {
    return OpenAPI()
      .info(
        Info().title("Whiskyer på polet")
          .description("Oversikt over whiskyer på vinmonopolet.no og deres prisutvikling")
          .version("v0.5.0")
          .license(
            License().name("Apache 2.0")
              .url("https://polet.herokuapp.com")
          )
      )
      .externalDocs(
        ExternalDocumentation()
          .description("Github")
          .url("https://github.com/javaguruen/vinmonopolet")
      )
      .servers(
        listOf(
          Server()
            .description("Local Spring Boot")
            .url("http://localhost:8000/"),
          Server()
            .description("Local Node")
            .url("http://localhost:4000/"),
          Server()
            .description("Production at Heroku")
            .url("https://polet.herokuapp.com/"),

          )
      )
  }
}
/*

@Validated
@ConfigurationProperties(prefix = "vinmonopolet")
data class SampleProperties(
  val url: String,
  val apiKey: String
)
*/


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