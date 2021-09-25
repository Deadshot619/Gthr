package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.EditionType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.helper.*
import java.util.*

data class ItemDisplayDomainModel(
    var objectID : String? = null,
    var productType: ProductType? = null,
    var productCategory: ProductCategory? = null,
    var price : Double? = 0.0,
    var language : LanguageDomainModel? = null,
    var condition : ConditionDomainModel? = null,
    var editionType: EditionType? =null,
    var name : String? = null,
    var description : String? = null,
    var productNumber : String? = null,
    var rarity : String? = null,
    var firImageURL: String? = null,
    var date : Date? = null
){
    constructor(saleHistoryDomainModel: SaleHistoryDomainModel,productDisplayModel: ProductDisplayModel):this(
        objectID = saleHistoryDomainModel.objectID,
                productType = productDisplayModel.productType,
                productCategory= productDisplayModel.productCategory,
                price= saleHistoryDomainModel.price,
                language = when(productDisplayModel.productType){
                    ProductType.POKEMON -> getPokemonLanguageDomainModel(saleHistoryDomainModel.language!!)
                    ProductType.YUGIOH -> getYugiohLanguageDomainModel(saleHistoryDomainModel.language!!)
                    ProductType.MAGIC_THE_GATHERING -> getMTGLanguage(saleHistoryDomainModel.language!!)
                    else -> null
                },
                condition= getCondition(saleHistoryDomainModel.condition!!),
                editionType=   when(productDisplayModel.productType){
                    ProductType.POKEMON -> getPokemonSelectedEdition(saleHistoryDomainModel.edition!!)
                    ProductType.YUGIOH -> getYugiohSelectedEdition(saleHistoryDomainModel.edition!!)
                    ProductType.MAGIC_THE_GATHERING -> getSelectedMTGEdition(saleHistoryDomainModel.edition!!)
                    else -> null
                },
                name= productDisplayModel.name,
                description= productDisplayModel.description,
                productNumber= productDisplayModel.productNumber,
                rarity= productDisplayModel.rarity,
                firImageURL= productDisplayModel.firImageURL,
                date = saleHistoryDomainModel.saleDate
    )
}