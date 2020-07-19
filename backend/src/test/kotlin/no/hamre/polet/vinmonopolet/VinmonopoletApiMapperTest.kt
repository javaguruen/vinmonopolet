package no.hamre.polet.vinmonopolet

import no.hamre.polet.ObjectMapperFactory
import no.hamre.polet.vinmonopolet.model.Product
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class VinmonopoletApiMapperTest {

  @Test
  internal fun `Commons StringUtils trimToNull gir null for tom streng`() {
    assertNull(StringUtils.trimToNull(""))
  }

  @Test
  internal fun `parse batch to productline`() {

    val mapper = ObjectMapperFactory.create()
    val products = mapper.readValue(data, Array<Product>::class.java)
    val productlines = products.toList()
        .also { println(it) }
        .map { VinmonopoletApiMapper.map2ProductLine(it) }
        .also { println(it) }
    assert( productlines.size == 1)
  }

  private val data = """
[
 {
        "basic": {
            "productId": "12001",
            "productShortName": "Bell's Original",
            "productLongName": "Bell's Original",
            "volume": 0.700,
            "alcoholContent": 40.00,
            "vintage": 0,
            "ageLimit": "20",
            "packagingMaterialId": "03",
            "packagingMaterial": "Glass",
            "volumTypeId": "01",
            "volumType": "Helflaske",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 1,
            "introductionDate": "2014-01-09",
            "productStatusSaleId": "",
            "productStatusSaleName": "",
            "productStatusSaleValidFrom": "0000-00-00"
        },
        "logistics": {
            "wholesalerId": "30086",
            "wholesalerName": "Diageo Norway AS",
            "vendorId": "30190",
            "vendorName": "Skanlog AS",
            "vendorValidFrom": "2010-09-01",
            "manufacturerId": "61243",
            "manufacturerName": "Bell's Dist.",
            "barcodes": [
                {
                    "gtin": "5000387905634",
                    "isMainGtin": true
                },
                {
                    "gtin": "5000387005211",
                    "isMainGtin": false
                }
            ],
            "orderPack": "D12",
            "minimumOrderQuantity": 12.000,
            "packagingWeight": 568.000
        },
        "origins": {
            "origin": {
                "countryId": "GBS",
                "country": "Skottland",
                "regionId": "00",
                "region": "Øvrige",
                "subRegionId": "00",
                "subRegion": "Øvrige"
            },
            "production": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": ""
            },
            "localQualityClassifId": "",
            "localQualityClassif": ""
        },
        "properties": {
            "ecoLabellingId": "",
            "ecoLabelling": "",
            "storagePotentialId": "",
            "storagePotential": "",
            "organic": false,
            "biodynamic": false,
            "ethicallyCertified": false,
            "vintageControlled": false,
            "sweetWine": false,
            "freeOrLowOnGluten": false,
            "kosher": false,
            "locallyProduced": false,
            "noAddedSulphur": false,
            "environmentallySmart": false,
            "productionMethodStorage": ""
        },
        "classification": {
            "mainProductTypeId": "3",
            "mainProductTypeName": "Brennevin",
            "subProductTypeId": "312",
            "subProductTypeName": "Whisky",
            "productTypeId": "31201",
            "productTypeName": "Whisky, annen",
            "productGroupId": "48",
            "productGroupName": "Whisky, Skottland, annen"
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "< 3",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "Lys gyllen.",
                "odour": "Aroma med preg av lyst malt, vanilje og litt røyk.",
                "taste": "Lett og mild whisky med innslag av malt og krydder, streif av vanilje og røyk."
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "15",
            "assortment": "Bestillingsutvalget",
            "validFrom": "2016-01-01",
            "listedFrom": "2010-05-03",
            "assortmentGrade": ""
        },
        "prices": [
            {
                "priceValidFrom": "2020-01-01",
                "salesPrice": 339.90,
                "salesPricePrLiter": 485.57,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2020-04-01",
            "time": "05:33:51"
        }
    }
]
  """.trimIndent()
}