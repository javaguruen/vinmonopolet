package no.hamre.polet.vinmonopolet

import no.hamre.polet.ObjectMapperFactory
import no.hamre.polet.vinmonopolet.model.Product
import org.glassfish.jersey.client.JerseyClientBuilder
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class VinmonopoletClientImplTest{
  @Test
  @Disabled("Only for manual testing")
  internal fun `call vinmonopolet api`() {
    val client = VinmonopoletClientImpl(
        url = "https://apis.vinmonopolet.no",
        apiKey = "removed",
        jerseyClient = JerseyClientBuilder().build())
    val products = client.doRequest()
    println(products)
  }

  @Test
  internal fun `parse 100 first products`() {
    val mapper = ObjectMapperFactory.create()
    val products = mapper.readValue(TestData.products, Array<Product>::class.java)
  }
}

object TestData{
  const val products = """[
    {
        "basic": {
            "productId": "210",
            "productShortName": "Plastpose",
            "productLongName": "Plastpose",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "2019-10-23",
            "productStatusSaleId": "",
            "productStatusSaleName": "",
            "productStatusSaleValidFrom": "0000-00-00"
        },
        "logistics": {
            "wholesalerId": "",
            "wholesalerName": "",
            "vendorId": "30771",
            "vendorName": "Stenqvist AS",
            "vendorValidFrom": "2016-12-09",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7045342001233",
                    "isMainGtin": true
                }
            ],
            "orderPack": "KAR",
            "minimumOrderQuantity": 1.000,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "805",
            "subProductTypeName": "Handleposer",
            "productTypeId": "80501",
            "productTypeName": "Plastposer",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": ""
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2017-08-09",
            "listedFrom": "2011-04-07",
            "assortmentGrade": "1"
        },
        "prices": [
            {
                "priceValidFrom": "2019-11-25",
                "salesPrice": 2.00,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    },
    {
        "basic": {
            "productId": "215",
            "productShortName": "Gaveeske m/ sølvmotiv 1fl sort",
            "productLongName": "Gaveeske 1 fl.",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "0000-00-00",
            "productStatusSaleId": "",
            "productStatusSaleName": "",
            "productStatusSaleValidFrom": "0000-00-00"
        },
        "logistics": {
            "wholesalerId": "10379",
            "wholesalerName": "Allsidige Nord AS",
            "vendorId": "900",
            "vendorName": "ScanLog AS Distribusjonssenter",
            "vendorValidFrom": "2016-03-09",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7090017200749",
                    "isMainGtin": true
                }
            ],
            "orderPack": "KAR",
            "minimumOrderQuantity": 25.000,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "803",
            "subProductTypeName": "Gaveesker",
            "productTypeId": "80301",
            "productTypeName": "Gaveeske 1 flaske",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": ""
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2017-11-01",
            "listedFrom": "2012-10-03",
            "assortmentGrade": "2"
        },
        "prices": [
            {
                "priceValidFrom": "2019-11-01",
                "salesPrice": 33.00,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    },
    {
        "basic": {
            "productId": "220",
            "productShortName": "Gaveeske m/ sølvmotiv 1fl hvit",
            "productLongName": "Gaveeske 1 fl.",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "0000-00-00",
            "productStatusSaleId": "",
            "productStatusSaleName": "",
            "productStatusSaleValidFrom": "0000-00-00"
        },
        "logistics": {
            "wholesalerId": "10379",
            "wholesalerName": "Allsidige Nord AS",
            "vendorId": "900",
            "vendorName": "ScanLog AS Distribusjonssenter",
            "vendorValidFrom": "2016-03-09",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7090017200794",
                    "isMainGtin": true
                }
            ],
            "orderPack": "KAR",
            "minimumOrderQuantity": 25.000,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "803",
            "subProductTypeName": "Gaveesker",
            "productTypeId": "80301",
            "productTypeName": "Gaveeske 1 flaske",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": ""
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2017-11-01",
            "listedFrom": "2012-10-03",
            "assortmentGrade": "5"
        },
        "prices": [
            {
                "priceValidFrom": "2019-11-01",
                "salesPrice": 33.00,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    },
    {
        "basic": {
            "productId": "255",
            "productShortName": "Gavepose med sløyfe",
            "productLongName": "Cellofan med sløyfe",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "0000-00-00",
            "productStatusSaleId": "",
            "productStatusSaleName": "",
            "productStatusSaleValidFrom": "0000-00-00"
        },
        "logistics": {
            "wholesalerId": "10379",
            "wholesalerName": "Allsidige Nord AS",
            "vendorId": "900",
            "vendorName": "ScanLog AS Distribusjonssenter",
            "vendorValidFrom": "2016-03-09",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7090017201050",
                    "isMainGtin": true
                }
            ],
            "orderPack": "KAR",
            "minimumOrderQuantity": 25.000,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "802",
            "subProductTypeName": "Gaveposer",
            "productTypeId": "80202",
            "productTypeName": "Gavepose 1 flaske",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": ""
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2017-11-01",
            "listedFrom": "2017-02-15",
            "assortmentGrade": "3"
        },
        "prices": [
            {
                "priceValidFrom": "2019-11-01",
                "salesPrice": 33.00,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    },
    {
        "basic": {
            "productId": "260",
            "productShortName": "Vinopptrekker",
            "productLongName": "Vinopptrekker",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "0000-00-00",
            "productStatusSaleId": "04",
            "productStatusSaleName": "Utgått",
            "productStatusSaleValidFrom": "2019-11-06"
        },
        "logistics": {
            "wholesalerId": "10379",
            "wholesalerName": "Allsidige Nord AS",
            "vendorId": "900",
            "vendorName": "ScanLog AS Distribusjonssenter",
            "vendorValidFrom": "2016-03-09",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7045341212821",
                    "isMainGtin": true
                }
            ],
            "orderPack": "D20",
            "minimumOrderQuantity": 20.000,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "productionMethodStorage": "Med Vinmonopolet-logo."
        },
        "classification": {
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "804",
            "subProductTypeName": "Tilbehør",
            "productTypeId": "80401",
            "productTypeName": "Tilbehør",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": "Pulltex vinopptrekker, anerkjent modell."
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2017-11-01",
            "listedFrom": "2016-04-29",
            "assortmentGrade": "1"
        },
        "prices": [
            {
                "priceValidFrom": "2016-05-26",
                "salesPrice": 69.90,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    },
    {
        "basic": {
            "productId": "282",
            "productShortName": "Gaveeske m/sølvmotiv hvit 6 fl.",
            "productLongName": "Gaveeske 6 fl.",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "0000-00-00",
            "productStatusSaleId": "04",
            "productStatusSaleName": "Utgått",
            "productStatusSaleValidFrom": "2019-11-06"
        },
        "logistics": {
            "wholesalerId": "10379",
            "wholesalerName": "Allsidige Nord AS",
            "vendorId": "900",
            "vendorName": "ScanLog AS Distribusjonssenter",
            "vendorValidFrom": "2016-03-09",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7090017201272",
                    "isMainGtin": true
                }
            ],
            "orderPack": "KAR",
            "minimumOrderQuantity": 0,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "803",
            "subProductTypeName": "Gaveesker",
            "productTypeId": "80305",
            "productTypeName": "Gaveeske 6 flasker",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": ""
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2017-11-01",
            "listedFrom": "2017-01-09",
            "assortmentGrade": "6"
        },
        "prices": [
            {
                "priceValidFrom": "2019-11-01",
                "salesPrice": 40.00,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    },
    {
        "basic": {
            "productId": "287",
            "productShortName": "Gjenbruksnett m/flaskeskille 2 flasker",
            "productLongName": "Gjenbruksnett m/flaskeskille 2 flasker",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "0000-00-00",
            "productStatusSaleId": "",
            "productStatusSaleName": "",
            "productStatusSaleValidFrom": "0000-00-00"
        },
        "logistics": {
            "wholesalerId": "10379",
            "wholesalerName": "Allsidige Nord AS",
            "vendorId": "900",
            "vendorName": "ScanLog AS Distribusjonssenter",
            "vendorValidFrom": "2016-03-09",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7090017201326",
                    "isMainGtin": true
                }
            ],
            "orderPack": "KAR",
            "minimumOrderQuantity": 25.000,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "805",
            "subProductTypeName": "Handleposer",
            "productTypeId": "80502",
            "productTypeName": "Gjenbruksnett",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": ""
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2017-11-01",
            "listedFrom": "2016-04-29",
            "assortmentGrade": "3"
        },
        "prices": [
            {
                "priceValidFrom": "2019-07-01",
                "salesPrice": 24.90,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    },
    {
        "basic": {
            "productId": "288",
            "productShortName": "Gjenbruksnett m/flaskeskille 6 flasker",
            "productLongName": "Gjenbruksnett m/flaskeskille 6 flasker",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "0000-00-00",
            "productStatusSaleId": "",
            "productStatusSaleName": "",
            "productStatusSaleValidFrom": "0000-00-00"
        },
        "logistics": {
            "wholesalerId": "10379",
            "wholesalerName": "Allsidige Nord AS",
            "vendorId": "900",
            "vendorName": "ScanLog AS Distribusjonssenter",
            "vendorValidFrom": "2016-03-09",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7090017201333",
                    "isMainGtin": true
                }
            ],
            "orderPack": "KAR",
            "minimumOrderQuantity": 25.000,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "805",
            "subProductTypeName": "Handleposer",
            "productTypeId": "80502",
            "productTypeName": "Gjenbruksnett",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": ""
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2017-11-01",
            "listedFrom": "2016-04-29",
            "assortmentGrade": "3"
        },
        "prices": [
            {
                "priceValidFrom": "2019-07-01",
                "salesPrice": 29.90,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    },
    {
        "basic": {
            "productId": "289",
            "productShortName": "Resirkulert gjenbruksnett",
            "productLongName": "Resirkulert gjenbruksnett",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "0000-00-00",
            "productStatusSaleId": "",
            "productStatusSaleName": "",
            "productStatusSaleValidFrom": "0000-00-00"
        },
        "logistics": {
            "wholesalerId": "10379",
            "wholesalerName": "Allsidige Nord AS",
            "vendorId": "900",
            "vendorName": "ScanLog AS Distribusjonssenter",
            "vendorValidFrom": "2016-03-09",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7090017201340",
                    "isMainGtin": true
                }
            ],
            "orderPack": "KAR",
            "minimumOrderQuantity": 25.000,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "805",
            "subProductTypeName": "Handleposer",
            "productTypeId": "80502",
            "productTypeName": "Gjenbruksnett",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": ""
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2017-11-01",
            "listedFrom": "2016-04-29",
            "assortmentGrade": "1"
        },
        "prices": [
            {
                "priceValidFrom": "2019-07-01",
                "salesPrice": 25.00,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    },
    {
        "basic": {
            "productId": "296",
            "productShortName": "Gaveeske 1 flaske m/rosa blomster",
            "productLongName": "Gaveeske 1 flaske",
            "volume": 0,
            "alcoholContent": 0,
            "vintage": 0,
            "ageLimit": "0",
            "packagingMaterialId": "",
            "packagingMaterial": "",
            "volumTypeId": "",
            "volumType": "",
            "corkTypeId": "",
            "corkType": "",
            "bottlePerSalesUnit": 0,
            "introductionDate": "0000-00-00",
            "productStatusSaleId": "",
            "productStatusSaleName": "",
            "productStatusSaleValidFrom": "0000-00-00"
        },
        "logistics": {
            "wholesalerId": "10379",
            "wholesalerName": "Allsidige Nord AS",
            "vendorId": "900",
            "vendorName": "ScanLog AS Distribusjonssenter",
            "vendorValidFrom": "2016-04-06",
            "manufacturerId": "",
            "manufacturerName": "",
            "barcodes": [
                {
                    "gtin": "7090017201425",
                    "isMainGtin": true
                }
            ],
            "orderPack": "KAR",
            "minimumOrderQuantity": 25.000,
            "packagingWeight": 0
        },
        "origins": {
            "origin": {
                "countryId": "",
                "country": "",
                "regionId": "",
                "region": "",
                "subRegionId": "",
                "subRegion": ""
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
            "mainProductTypeId": "8",
            "mainProductTypeName": "Gaveartikler og tilbehør",
            "subProductTypeId": "803",
            "subProductTypeName": "Gaveesker",
            "productTypeId": "80301",
            "productTypeName": "Gaveeske 1 flaske",
            "productGroupId": "",
            "productGroupName": ""
        },
        "ingredients": {
            "grapes": [],
            "ingredients": "",
            "sugar": "",
            "acid": ""
        },
        "description": {
            "characteristics": {
                "colour": "",
                "odour": "",
                "taste": ""
            },
            "freshness": "",
            "fullness": "",
            "bitterness": "",
            "sweetness": "",
            "tannins": "",
            "recommendedFood": []
        },
        "assortment": {
            "assortmentId": "11",
            "assortment": "Basisutvalget",
            "validFrom": "2018-05-22",
            "listedFrom": "2016-04-12",
            "assortmentGrade": "5"
        },
        "prices": [
            {
                "priceValidFrom": "2019-11-01",
                "salesPrice": 33.00,
                "salesPricePrLiter": 0,
                "bottleReturnValue": 0
            }
        ],
        "lastChanged": {
            "date": "2019-12-06",
            "time": "09:51:09"
        }
    }
]"""
}