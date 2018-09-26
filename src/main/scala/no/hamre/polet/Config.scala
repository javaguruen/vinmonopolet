package no.hamre.polet

import javax.validation.Valid
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard
import io.dropwizard.bundles.assets.{AssetsBundleConfiguration, AssetsConfiguration}
import io.dropwizard.db.DataSourceFactory
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import javax.validation.constraints.NotNull
import org.constretto.annotation
import org.constretto.annotation.Configuration

case class Config
(
  @annotation.Configuration
  @Valid
  @JsonProperty(value="database", required = true)
  database: DataSourceFactory = new DataSourceFactory(),
  @Configuration
  @JsonProperty(value="useH2Database", required = true)
  useH2Database: Boolean,
  @Valid
  @NotNull
  @JsonProperty("loadTestdata")
  loadTestdata: Boolean,
  @Configuration
  dataUrl: String,
  @Configuration
  dataEncoding: String,
  @JsonProperty("swagger")
  swaggerBundleConfiguration: SwaggerBundleConfiguration,
  @Valid
  @Configuration
  @JsonProperty("assets")
  assets: AssetsConfiguration = AssetsConfiguration.builder().build()
)extends dropwizard.Configuration with AssetsBundleConfiguration {
  override def getAssetsConfiguration: AssetsConfiguration = assets
}