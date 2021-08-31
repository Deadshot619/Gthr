package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType

data class ProductDisplayModel(
    var objectID: String?,
    var productType: ProductType?,
    var productCategory: ProductCategory?,
    var refKey: String?,
    var firImageURL: String?,
    var name: String?,
    var description: String?,
    var lowestAskCost: Double?,
    var highestBidCost: Double?,
    var lowestAskID: String?,
    var highestBidID: String?,
    var productNumber: String?,
    var numberOfFavorites: Int?,
    var tier: String?,
) {
    //TODO 24/07/21 : Modify productCategory & productType

    constructor(model: FunkoDomainModel) : this(
        objectID = model.objectID,
        productType = ProductType.FUNKO,
        productCategory = ProductCategory.TOYS,
        refKey = "",
        firImageURL = model.imageURL,
        name = model.name,
        description = model.license,
        lowestAskCost = model.lowestAskCost,
        highestBidCost = model.highestBidCost,
        lowestAskID = model.lowestAskID,
        highestBidID = model.highestBidID,
        productNumber = model.itemNumber.toString(),
        numberOfFavorites = model.numberOfFavorites,
        tier = model.tier.toString(),
    )

    constructor(model: MTGDomainModel) : this(
        objectID = model.objectID,
        productType = ProductType.MAGIC_THE_GATHERING,
        productCategory = ProductCategory.CARDS,
        refKey = "",
        firImageURL = model.imageUris,
        name = model.name,
        description = model.setName,
        lowestAskCost = model.lowestAskCost,
        highestBidCost = model.highestBidCost,
        lowestAskID = model.lowestAskID,
        highestBidID = model.highestBidID,
        productNumber = model.id,
        numberOfFavorites = model.numberOfFavorites,
        tier = "0",
    )

    constructor(model: PokemonDomainModel) : this(
        objectID = model.objectID,
        productType = ProductType.MAGIC_THE_GATHERING,
        productCategory = ProductCategory.CARDS,
        refKey = "",
        firImageURL = model.imageURL,
        name = model.name,
        description = model.set,
        lowestAskCost = model.lowestAskCost,
        highestBidCost = model.highestBidCost,
        lowestAskID = model.lowestAskID,
        highestBidID = model.highestBidID,
        productNumber = model.number.toString(),
        numberOfFavorites = model.numberOfFavorites,
        tier = "0",
    )

    constructor(model: YugiohDomainModel) : this(
        objectID = model.objectID,
        productType = ProductType.MAGIC_THE_GATHERING,
        productCategory = ProductCategory.CARDS,
        refKey = "",
        firImageURL = model.imageURL,
        name = model.name,
        description = model.set,
        lowestAskCost = model.lowestAskCost,
        highestBidCost = model.highestBidCost,
        lowestAskID = model.lowestAskID,
        highestBidID = model.highestBidID,
        productNumber = model.number,
        numberOfFavorites = model.numberOfFavorites,
        tier = "0",
    )

    constructor(model: SealedDomainModel) : this(
        objectID = model.objectID,
        productType = ProductType.MAGIC_THE_GATHERING,
        productCategory = ProductCategory.SEALED,
        refKey = "",
        firImageURL = model.imageURL,
        name = model.name,
        description = model.set,
        lowestAskCost = model.lowestAskCost,
        highestBidCost = model.highestBidCost,
        lowestAskID = model.lowestAskID,
        highestBidID = model.highestBidID,
        productNumber = null,
        numberOfFavorites = model.numberOfFavorites,
        tier = "0",
    )
}
