package com.gthr.gthrcollect.model.domain

import android.os.Parcelable
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDisplayModel(
    var objectID: String?,
    var productType: ProductType?,
    var productCategory: ProductCategory?,
    var refKey: String?,
    var firImageURL: String?,
    var name: String?,
    var description: String?,
    var lowestAskCost: Int?,
    var highestBidCost: Int?,
    var lowestAskID: String?,
    var highestBidID: String?,
    var productNumber: String?,
    var numberOfFavorites: Int?,
    var tier: String?,
    var rarity: String?
) : Parcelable {
    //TODO 01/08/21 : Modify firImageUrl added

    constructor(model: FunkoDomainModel) : this(
        objectID = model.objectID,
        productType = model.productType,
        productCategory = ProductCategory.TOYS,
        refKey = model.refKey,
        firImageURL = "",
        name = model.name,
        description = model.license,
        lowestAskCost = model.lowestAskCost,
        highestBidCost = model.highestBidCost,
        lowestAskID = model.lowestAskID,
        highestBidID = model.highestBidID,
        productNumber = model.itemNumber.toString(),
        numberOfFavorites = model.numberOfFavorites,
        tier = model.tier.toString(),
        rarity = ProductCategory.TOYS.title.toUpperCase()
    )

    constructor(model: MTGDomainModel) : this(
        objectID = model.objectID,
        productType = model.productType,
        productCategory = ProductCategory.CARDS,
        refKey = model.refKey,
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
        rarity = model.rarity
    )

    constructor(model: PokemonDomainModel) : this(
        objectID = model.objectID,
        productType = model.productType,
        productCategory = ProductCategory.CARDS,
        refKey = model.refKey,
        firImageURL = model.firImageURL,
        name = model.name,
        description = model.set,
        lowestAskCost = model.lowestAskCost,
        highestBidCost = -1,
        lowestAskID = model.lowestAskID,
        highestBidID = model.highestBidID,
        productNumber = model.number.toString(),
        numberOfFavorites = model.numberOfFavorites,
        tier = "0",
        rarity = model.rarity
    )

    constructor(model: YugiohDomainModel) : this(
        objectID = model.objectID,
        productType = model.productType,
        productCategory = ProductCategory.CARDS,
        refKey = model.refKey,
        firImageURL = model.firImageURL,
        name = model.name,
        description = model.set,
        lowestAskCost = model.lowestAskCost,
        highestBidCost = model.highestBidCost,
        lowestAskID = model.lowestAskID,
        highestBidID = model.highestBidID,
        productNumber = model.number,
        numberOfFavorites = model.numberOfFavorites,
        tier = "0",
        rarity = model.rarity
    )

    constructor(model: SealedDomainModel) : this(
        objectID = model.objectID,
        productType = model.productType,
        productCategory = ProductCategory.SEALED,
        refKey = model.refKey,
        firImageURL = model.firImageURL,
        name = model.name,
        description = model.set,
        lowestAskCost = model.lowestAskCost,
        highestBidCost = model.highestBidCost,
        lowestAskID = model.lowestAskID,
        highestBidID = model.highestBidID,
        productNumber = null,
        numberOfFavorites = model.numberOfFavorites,
        tier = model.tier.toString(),
        rarity = ProductCategory.SEALED.title.toUpperCase()
    )
}
