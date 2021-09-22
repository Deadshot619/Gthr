package com.gthr.gthrcollect.model.domain

import android.os.Parcelable
import com.gthr.gthrcollect.utils.enums.AdapterViewType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.getProductCategory
import com.gthr.gthrcollect.utils.getProductType
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
        var lowestAskCost: Double?,
        var highestBidCost: Int?,
        var lowestAskID: String?,
        var highestBidID: String?,
        var productNumber: String?,
        var numberOfFavorites: Int?,
        var tier: String?,
        var rarity: String?,
        val viewType: AdapterViewType = AdapterViewType.VIEW_TYPE_ITEM,
        val forsaleItemNodel: ForSaleItemDomainModel? = null,
    val isForSale : Boolean? = false
) : Parcelable {
    //TODO 01/08/21 : Modify firImageUrl added

    constructor(viewType: AdapterViewType) : this(
            objectID = "",
            productType = ProductType.YUGIOH,
            productCategory = ProductCategory.TOYS,
            refKey = "",
            firImageURL = "",
            name = "",
            description = "",
            lowestAskCost = -1.0,
            highestBidCost = -1,
            lowestAskID = "",
            highestBidID = "",
            productNumber = "",
            numberOfFavorites = -1,
            tier = "",
            rarity = ProductType.FUNKO.title.capitalize(),
            viewType = viewType
    )

    constructor(model: FunkoDomainModel,isForSale : Boolean = false) : this(
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
        rarity = ProductType.FUNKO.title.capitalize(),
        isForSale = isForSale
    )

    constructor(model: MTGDomainModel,isForSale : Boolean = false) : this(
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
        productNumber = model.collectorNumber,
        numberOfFavorites = model.numberOfFavorites,
        tier = "0",
        rarity = model.rarity,
        isForSale = isForSale
    )

    constructor(model: PokemonDomainModel,isForSale : Boolean = false) : this(
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
        rarity = model.rarity,
        isForSale = isForSale
    )

    constructor(model: YugiohDomainModel,isForSale : Boolean = false) : this(
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
        rarity = model.rarity,
        isForSale = isForSale
    )

    constructor(model: SealedDomainModel,isForSale : Boolean = false) : this(
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
        rarity = ProductCategory.SEALED.title.capitalize(),
        isForSale = isForSale
    )

    constructor(model: ForSaleItemDomainModel) : this(
        objectID = model.itemObjectID,
        productType = model.productType,
        productCategory = if (model.productType == null) null else getProductCategory(
            getProductType(
                model.productType.title
            )!!
        ),
        refKey = model.askRefKey,
        firImageURL = model.productFirImageURL,
        name = model.productProductName,
        description = model.productGroup,
        lowestAskCost = model.price,
        highestBidCost = 0,
        lowestAskID = "",
        highestBidID = "",
        productNumber = model.productProductNumber.toString(),
        numberOfFavorites = model.productNumberOfFavorites,
        tier = "",
        rarity = model.productRarity?.capitalize(),
        forsaleItemNodel = model
    )

}
