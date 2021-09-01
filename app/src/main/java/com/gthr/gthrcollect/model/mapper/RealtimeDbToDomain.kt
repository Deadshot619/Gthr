package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.getProductType

fun CollectionInfoModel.toCollectionInfoDomainModel(collectionId: String = "") =
    CollectionInfoDomainModel(
        about = about,
        buyList = buyList,
        collectionDisplayName = collectionDisplayName, //collectionList,
        collectionRawName = collectionRawName,
        favoriteCollectionList = favoriteCollectionList,
        favoriteProductList = favoriteProductList,
        followersList = followersList,
        sellList = sellList,
        userRefKey = userRefKey,
        profileImage = profileImageURL,
        collectionId = collectionId
    )

fun FunkoModel.toFunkoDomainModel(refKey: String) = FunkoDomainModel(
    exclusive = this.exclusive?:"",
    funkoID = this.funkoID?:"",
    category = this.category?:"",
    funkoType = this.funkoType?:"",
    highestBidCost = this.highestBidCost?:-1,
    highestBidID = this.highestBidID?:"",
    imageURL = this.imageURL?:"",
    itemNumber = this.itemNumber?:"",
    license = this.license?:"",
    lowestAskCost = this.lowestAskCost?:-1,
    lowestAskID = this.lowestAskID?:"",
    marketValue = this.marketValue?:"",
    name = this.name?:"",
    numberOfFavorites = this.numberOfFavorites?:-1,
    objectID = this.objectID?:"",
    productType = getProductType(this.productType?:"")?:ProductType.FUNKO,
    releaseDate = this.releaseDate?:"",
    status = this.status?:"",
    tier = this.tier?:"",
    refKey = refKey
)

fun MTGModel.toMTGDomainModel(refKey: String) = MTGDomainModel(
    firImageURL = this.firImageURL?:"",
    cardBackId = this.cardBackId?:"",
    colors = this.colors?:"",
    flavorText = this.flavorText?:"",
    foil = this.foil?:false,
    highestBidCost = this.highestBidCost?:-1,
    highestBidID = this.highestBidID?:"",
    id = this.id?:"",
    imageID = this.imageID?:"",
    imageUris = this.imageUris?:"",
    lang = this.lang?:"",
    lowestAskCost = this.lowestAskCost?:-1,
    lowestAskID = this.lowestAskID?:"",
    mtgoFoilId = this.mtgoFoilId?:"",
    mtgoId = this.mtgoId?:"",
    name = this.name?:"",
    numberOfFavorites = this.numberOfFavorites?:-1,
    objectID = this.objectID?:"",
    oracleId = this.oracleId?:"",
    productType = getProductType(this.productType?:"")?:ProductType.MAGIC_THE_GATHERING,
    rarity = this.rarity?:"",
    releasedAt = this.releasedAt?:"",
    setName = this.setName?:"",
    setType = this.setType?:"",
    typeLine = this.typeLine?:"",
    refKey = refKey
)

fun PokemonModel.toPokemonDomainModel(refKey: String) = PokemonDomainModel(
    firImageURL = this.firImageURL?:"",
    highestBid = this.highestBid?:-1,
    japanese = this.japanese?:-1,
    english = this.english?:-1,
    cardType = this.cardType?:"",
    highestBidID = this.highestBidID?:"",
    hp = this.hp?:"",
    imageID = this.imageID?:"",
    imageURL = this.imageURL?:"",
    japaneseNumber = this.japaneseNumber?:-1,
    japaneseSet = this.japaneseSet?:"",
    lowestAskCost = this.lowestAskCost?:-1,
    lowestAskID = this.lowestAskID?:"",
    name = this.name?:"",
    noLanguagenoEdition = this.noLanguagenoEdition?:-1,
    number = this.number?:"",
    numberOfFavorites = this.numberOfFavorites?:-1,
    objectID = this.objectID?:"",
    priceFoil = this.priceFoil?:"",
    priceNonFoil = this.priceNonFoil?:"",
    productType = getProductType(this.productType?:"")?:ProductType.POKEMON,
    rarity = this.rarity?:"",
    resistance = this.resistance?:"",
    retreatCost = this.retreatCost?:"",
    `set` = this.`set`?:"",
    stage = this.stage?:"",
    weakness = this.weakness?:"",
    refKey = refKey
)

fun SealedModel.toSealedDomainModel(refKey: String) = SealedDomainModel(
    firImageURL = firImageURL?:"",
    cardText = this.cardText?:"",
    highestBidCost = this.highestBidCost?:-1,
    highestBidID = this.highestBidID?:"",
    imageID = this.imageID?:"",
    imageURL = this.imageURL?:"",
    lowestAskCost = this.lowestAskCost?:-1,
    lowestAskID = this.lowestAskID?:"",
    marketPrice = this.marketPrice?:-1.0,
    name = this.name?:"",
    numberOfFavorites = this.numberOfFavorites?:-1,
    objectID = this.objectID?:"",
    productType = getProductType(this.productType?:"")?:ProductType.SEALED_POKEMON,
    rarity = this.rarity?:"",
    `set` = this.`set`?:"",
    tier = this.tier?:-1,
    refKey = refKey
)

fun YugiohModel.toYugiohDomainModel(key: String) = YugiohDomainModel(
    firImageURL = this.firImageURL?:"",
    asianEnglish = this.asianEnglish?:-1,
    asianEnglishOG = this.asianEnglishOG?:-1,
    cardType = this.cardType?:"",
    detailPrice = this.detailPrice?:"",
    english = this.english?:-1,
    englishOG = this.englishOG?:-1,
    euroEnglish = this.euroEnglish?:-1,
    firstDescription = this.firstDescription?:"",
    french = this.french?:-1,
    frenchCanadian = this.frenchCanadian?:-1,
    frenchOG = this.frenchOG?:-1,
    german = this.german?:-1,
    germanOG = this.germanOG?:-1,
    highestBidCost = this.highestBidCost?:-1,
    highestBidID = this.highestBidID?:"",
    imageID = this.imageID?:"",
    imageURL = this.imageURL?:"",
    italian = this.italian?:-1,
    italianOG = this.italianOG?:-1,
    japanese = this.japanese?:-1,
    korean = this.korean?:-1,
    koreanOG = this.koreanOG?:-1,
    lowestAskCost = this.lowestAskCost?:-1,
    lowestAskID = this.lowestAskID?:"",
    name = this.name?:"",
    number = this.number?:"",
    numberOfFavorites = this.numberOfFavorites?:-1,
    objectID = this.objectID?:"",
    oceanicEnglish = this.oceanicEnglish?:-1,
    portuguese = this.portuguese?:-1,
    portugueseOG = this.portugueseOG?:-1,
    productType = getProductType(this.productType?:"")?:ProductType.YUGIOH,
    rarity = this.rarity?:"",
    `set` = this.`set`?:"",
    spanish = this.spanish?:-1,
    spanishOG = this.spanishOG?:-1,
    stats = this.stats?:"",
    refKey = key
)