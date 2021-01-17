package no.hamre.polet.vinmonopolet

import no.hamre.polet.modell.Productline
import no.hamre.polet.vinmonopolet.model.Price
import no.hamre.polet.vinmonopolet.model.Product
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object VinmonopoletApiMapper {
  private val LOG = LoggerFactory.getLogger(VinmonopoletApiMapper::class.java)
  private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
  private const val productBaseUrl = "https://apis.vinmonopolet.no/products/v0/details-normal?productId="

  fun map2ProductLine(product: Product): Productline {
    LOG.info("Mapper product.id ${product.basic.productId}")
    return Productline(
        id = null,
        datotid = parseIntroductionDate(product.basic.introductionDate),  //basic.introductionDate todo: hva skal denne være?
        varenummer = product.basic.productId,
        varenavn = product.basic.productLongName,
        volum = product.basic.volume,
        pris = getLatestPrice(product.prices).salesPrice,
        literpris = getLatestPrice(product.prices).salesPricePrLiter,
        varetype = product.classification.subProductTypeName,
        produktutvalg = product.assortment.assortment,
        butikkategori = "NONE",
        farge = product.description.characteristics.colour,
        lukt = product.description.characteristics.odour,
        smak = product.description.characteristics.taste,
        land = product.origins.origin.country,
        distrikt = product.origins.origin.region,
        underdistrikt = product.origins.origin.subRegion,
        aargang = product.basic.vintage,
        raastoff = StringUtils.trimToNull(product.ingredients.ingredients),
        metode = StringUtils.trimToNull(product.properties.productionMethodStorage),
        alkohol = product.basic.alcoholContent,
        produsent = product.logistics.manufacturerName,
        grossist = product.logistics.wholesalerName,
        distributor = product.logistics.vendorName,
        vareurl = "$productBaseUrl${product.basic.productId}",
        updated = LocalDateTime.of(
            LocalDate.parse(product.lastChanged.date, dateTimeFormatter),
            LocalTime.parse(product.lastChanged.time, timeFormatter)
        )
    )
  }

  private fun parseIntroductionDate(introductionDate: String): LocalDateTime {
    return when (introductionDate) {
      "" -> LocalDateTime.now()
      "0000-00-00" -> LocalDateTime.now()
      else -> LocalDate.parse(introductionDate, dateTimeFormatter).atStartOfDay()
    }
  }

  private fun getLatestPrice(prices: List<Price>): Price {
    if (prices.size == 1) {
      return prices[0]
    }
    throw RuntimeException("Product with ${prices.size} prices")
  }
}
