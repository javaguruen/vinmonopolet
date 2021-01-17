package no.hamre.polet.vinmonopolet.model

data class Product(
    val basic: Basic,
    val logistics: Logistics,
    val origins: Origins,
    val properties: Properties,
    val classification: Classification,
    val ingredients: Ingredients,
    val description: Description,
    val assortment: Assortment,
    val prices: List<Price>,
    val lastChanged: LastChanged
){
  fun isWhisky() = "whisky" == classification.subProductTypeName.toLowerCase()
}

data class Basic(
    val productId: String,
    val productShortName: String,
    val productLongName: String,
    val volume: Double,
    val alcoholContent: Double,
    val vintage: Int, //should be string?
    val ageLimit: String,
    val packagingMaterialId: String,
    val packagingMaterial: String,
    val volumTypeId: String,
    val volumType: String,
    val corkTypeId: String,
    val corkType: String,
    val bottlePerSalesUnit: Int, //Should be string?
    val introductionDate: String,
    val productStatusSaleId: String,
    val productStatusSaleName: String,
    val productStatusSaleValidFrom: String
)

data class Logistics(
    val barcodes: List<Barcode>,
    val orderPack: String,
    val minimumOrderQuantity: Double,
    val packagingWeight: Double,

    //disse finnes ikke i eksempelet men i openapi3.
    val wholesalerId: String,
    val wholesalerName: String,
    val vendorId: String,
    val vendorName: String,
    val vendorValidFrom: String,
    val manufacturerId: String,
    val manufacturerName: String
)

data class Barcode(
    val gtin: String,
    val isMainGtin: Boolean
)

data class Origins(
    val origin: Origin,
    val production: Production,
    val localQualityClassifId: String,
    val localQualityClassif: String
)

data class Origin(
    val countryId: String,
    val country: String,
    val regionId: String,
    val region: String,
    val subRegionId: String,
    val subRegion: String
)

data class Production(
    val countryId: String,
    val country: String,
    val regionId: String,
    val region: String
)

data class Properties(
    val ecoLabellingId: String,
    val ecoLabelling: String,
    val storagePotentialId: String,
    val storagePotential: String,
    val organic: Boolean,
    val biodynamic: Boolean,
    val ethicallyCertified: Boolean,
    val vintageControlled: Boolean,
    val sweetWine: Boolean,
    val freeOrLowOnGluten: Boolean,
    val kosher: Boolean,
    val locallyProduced: Boolean,
    val noAddedSulphur: Boolean,
    val environmentallySmart: Boolean,
    val productionMethodStorage: String
)

data class Classification(
    val mainProductTypeId: String,
    val mainProductTypeName: String,
    val subProductTypeId: String,
    val subProductTypeName: String,
    val productTypeId: String,
    val productTypeName: String,
    val productGroupId: String,
    val productGroupName: String
)

data class Ingredients(
    val grapes: List<Grape>,
    val ingredients: String,
    val sugar: String,
    val acid: String
)

data class Grape(
    val grapeId: String,
    val grapeDesc: String,
    val grapePct: String
)

data class Characteristics(
    val colour: String,
    val odour: String,
    val taste: String
)

data class Description(
    val characteristics: Characteristics,
    val freshness: String,
    val fullness: String,
    val bitterness: String,
    val sweetness: String,
    val tannins: String,
    val recommendedFood: List<Food>
)

data class Food(
    val foodId: String,
    val foodDesc: String
)

data class Assortment(
    val assortmentId: String,
    val assortment: String,
    val validFrom: String,  //Burde vært dato
    val listedFrom: String, //Burde vært dato
    val assortmentGrade: String
)

data class LastChanged(
    val date: String,  //Burde vært string
    val time: String   //Burde vært Time
)

data class Price(
    val priceValidFrom: String,  //should be date
    val salesPrice: Double,
    val salesPricePrLiter: Double,
    val bottleReturnValue: Double
)