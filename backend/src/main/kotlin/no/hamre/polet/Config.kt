package no.hamre.polet

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.Valid
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.bundles.assets.AssetsBundleConfiguration
import io.dropwizard.bundles.assets.AssetsConfiguration
import io.dropwizard.client.HttpClientConfiguration
import io.dropwizard.client.JerseyClientConfiguration

import io.dropwizard.db.DataSourceFactory
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import javax.validation.constraints.NotNull
import org.constretto.annotation.Configuration

data class Config  (
    @field:Configuration
    @field:Valid
    @field:JsonProperty(value = "database", required = true)
    val database: DataSourceFactory = DataSourceFactory(),

    @field:Configuration
    @field:JsonProperty(value = "useH2Database", required = true)
    val useH2Database: Boolean,

    @field:Valid
    @field:NotNull
    @field:JsonProperty("loadTestdata")
    val loadTestdata: Boolean,

    @field:Configuration
    val dataUrl: String,

    @field:Configuration
    val dataEncoding: String,
    @field:JsonProperty("swagger")
    val swaggerBundleConfiguration: SwaggerBundleConfiguration,

    @field:Valid
    @field:Configuration
    @field:JsonProperty("assets")
    val assets: AssetsConfiguration = AssetsConfiguration.builder().build(),

    @field:Valid
    @field:NotNull
    @field:JsonProperty("jerseyClient")
    val jerseyClient: JerseyClientConfiguration = JerseyClientConfiguration()

) : io.dropwizard.Configuration(), AssetsBundleConfiguration {
  override fun getAssetsConfiguration(): AssetsConfiguration = assets
}