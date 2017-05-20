package no.hamre.polet

import javax.validation.Valid

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

case class Config
(
  @Valid
  @JsonProperty(value="database", required = true)
  database: DataSourceFactory = new DataSourceFactory(),
  @JsonProperty(value="useH2Database", required = true)
  useH2Database: Boolean,
  dataUrl: String,
  dataEncoding: String,
  @JsonProperty("swagger")
  swaggerBundleConfiguration: SwaggerBundleConfiguration
) extends Configuration