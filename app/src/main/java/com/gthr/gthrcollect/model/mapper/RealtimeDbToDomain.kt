package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.toRecentSaleDate
import com.gthr.gthrcollect.utils.getProductCategory
import com.gthr.gthrcollect.utils.getProductType
import com.gthr.gthrcollect.utils.helper.getEditionTypeFromRowType

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
        collectionId = collectionId,
        collectionList=collectionList
    )

fun CollectionItemModel.toCollectionItemDomainModel()=CollectionItemDomainModel(
    id=id,
    itemRefKey=itemRefKey,
    marketCost=marketCost,
    productType= getProductType(productType.toString()),
    edition= getEditionTypeFromRowType(edition.toString() ),
    askRefKey=askRefKey,
    frontImageURL = frontImageURL,
    backImageURL= backImageURL

)

fun FunkoModel.toFunkoDomainModel(refKey: String) = FunkoDomainModel(
    exclusive = this.exclusive ?: "",
    funkoID = this.funkoID ?: "",
    category = if(this.category.isNullOrEmpty())"N/A" else this.category!!,
    funkoType = this.funkoType ?: "",
    highestBidCost = this.highestBidCost ?: -1,
    highestBidID = this.highestBidID ?: "",
    imageURL = this.imageURL ?: "",
    itemNumber = if(this.itemNumber.isNullOrEmpty()) "N/A" else this.itemNumber!!,
    license = if(this.license.isNullOrEmpty()) "N/A" else this.license!!,
    lowestAskCost = this.lowestAskCost ?: -1,
    lowestAskID = this.lowestAskID ?: "",
//    marketValue = this.marketValue ?: "",
    name = this.name ?: "",
    numberOfFavorites = this.numberOfFavorites ?: -1,
    objectID = this.objectID ?: "",
    productType = getProductType(this.productType ?: "") ?: ProductType.FUNKO,
    releaseDate = if(this.releaseDate.isNullOrEmpty()) "N/A" else this.releaseDate!!,
    status = this.status ?: "",
    tier = this.tier ?: "",
    exclusivity = if(this.exclusivity.isNullOrEmpty()) "N/A" else this.exclusivity!! ,
    imageID = this.imageID?: "",
    firImageURL = this.firImageURL?: "",
    refKey = refKey
)

fun MTGModel.toMTGDomainModel(refKey: String) = MTGDomainModel(
    firImageURL = this.firImageURL ?: "",
    cardBackId = this.cardBackId ?: "",
    colors = this.colors ?: "",
    flavorText = this.flavorText ?: "",
    foil = this.foil ?: false,
    highestBidCost = this.highestBidCost ?: -1,
    highestBidID = this.highestBidID ?: "",
    id = this.id ?: "",
    imageID = this.imageID ?: "",
    imageUris = this.imageUris ?: "",
    lang = this.lang ?: "",
    lowestAskCost = this.lowestAskCost ?: -1,
    lowestAskID = this.lowestAskID ?: "",
    mtgoFoilId = this.mtgoFoilId ?: "",
    mtgoId = this.mtgoId ?: "",
    name = this.name ?: "",
    numberOfFavorites = this.numberOfFavorites ?: -1,
    objectID = this.objectID ?: "",
    oracleId = this.oracleId ?: "",
    productType = getProductType(this.productType ?: "") ?: ProductType.MAGIC_THE_GATHERING,
    rarity = if(this.rarity.isNullOrEmpty()) "-" else this.rarity!!,
    releasedAt = this.releasedAt ?: "",
    setName = this.setName ?: "",
    setType = this.setType ?: "",
    typeLine = this.typeLine ?: "",
    refKey = refKey,

    brawl = this.brawl ?: false,
    commander = this.commander ?: false,
    duel = this.duel ?: false,
    future = this.future ?: false,
    legacy = this.legacy ?: false,
    modern = this.modern ?: false,
    oldschool = this.oldschool ?: false,
    pauper = this.pauper ?: false,
    penny = this.penny ?: false,
    pioneer = this.pioneer ?: false,
    vintage = this.vintage ?: false,
    standard = this.standard ?: false,
    historic = this.historic ?: false,
    power = this.power ?: "",
    toughness = this.toughness ?: "",
    collectorNumber = if(this.collectorNumber.isNullOrEmpty()) "-" else this.collectorNumber!!
)

fun PokemonModel.toPokemonDomainModel(refKey: String) = PokemonDomainModel(
    firImageURL = this.firImageURL ?: "",
    highestBid = this.highestBid ?: -1,
    japanese = this.japanese ?: -1,
    english = this.english ?: -1,
    cardType = if(this.cardType.isNullOrEmpty()) "N/A" else this.cardType!!,
    highestBidID = this.highestBidID ?: "",
    hp = if(this.hp.isNullOrEmpty()) "N/A" else this.hp!!,
    imageID = this.imageID ?: "",
    imageURL = this.imageURL ?: "",
    japaneseNumber = if(this.japaneseNumber!=null)"${this.japaneseNumber!!}" else "N/A",
    japaneseSet = if(this.japaneseSet.isNullOrEmpty()) "N/A" else this.japaneseSet!!,
    lowestAskCost = this.lowestAskCost ?: -1,
    lowestAskID = this.lowestAskID ?: "",
    name = this.name ?: "",
    noLanguagenoEdition = this.noLanguagenoEdition ?: -1,
    number = if(this.number.isNullOrEmpty()) "N/A" else this.number!!,
    numberOfFavorites = this.numberOfFavorites ?: -1,
    objectID = this.objectID ?: "",
    priceFoil = this.priceFoil ?: "",
    priceNonFoil = this.priceNonFoil ?: "",
    productType = getProductType(this.productType ?: "") ?: ProductType.POKEMON,
    rarity = if(this.rarity.isNullOrEmpty()) "N/A" else this.rarity!!,
    resistance = this.resistance ?: "",
    retreatCost = this.retreatCost ?: "",
    `set` = if(this.`set`.isNullOrEmpty()) "N/A" else this.`set`!!,
    stage = if(this.stage.isNullOrEmpty()) "N/A" else this.stage!!,
    weakness = this.weakness ?: "",
    refKey = refKey
)

fun SealedModel.toSealedDomainModel(refKey: String) = SealedDomainModel(
    firImageURL = firImageURL ?: "",
    cardText = this.cardText ?: "",
    highestBidCost = this.highestBidCost ?: -1,
    highestBidID = this.highestBidID ?: "",
    imageID = this.imageID ?: "",
    imageURL = this.imageURL ?: "",
    lowestAskCost = this.lowestAskCost ?: -1,
    lowestAskID = this.lowestAskID ?: "",
//    marketPrice = this.marketPrice ?: "",
    name = this.name ?: "",
    numberOfFavorites = this.numberOfFavorites ?: -1,
    objectID = this.objectID ?: "",
    productType = getProductType(this.productType ?: "") ?: ProductType.SEALED_POKEMON,
    rarity = this.rarity ?: "",
    `set` = this.`set` ?: "",
    tier = this.tier ?: -1,
    description =  if(this.description.isNullOrEmpty()) "-" else this.description!!,
    refKey = refKey
)

fun YugiohModel.toYugiohDomainModel(key: String) = YugiohDomainModel(
    firImageURL = this.firImageURL ?: "",
    asianEnglish = this.asianEnglish ?: -1,
    asianEnglishOG = this.asianEnglishOG ?: -1,
    cardType = this.cardType ?: "",
    detailPrice = this.detailPrice ?: "",
    english = this.english ?: -1,
    englishOG = this.englishOG ?: -1,
    euroEnglish = this.euroEnglish ?: -1,
    firstDescription = if(this.firstDescription.isNullOrEmpty()) "-" else this.firstDescription!!,
    french = this.french ?: -1,
    frenchCanadian = this.frenchCanadian ?: -1,
    frenchOG = this.frenchOG ?: -1,
    german = this.german ?: -1,
    germanOG = this.germanOG ?: -1,
    highestBidCost = this.highestBidCost ?: -1,
    highestBidID = this.highestBidID ?: "",
    imageID = this.imageID ?: "",
    imageURL = this.imageURL ?: "",
    italian = this.italian ?: -1,
    italianOG = this.italianOG ?: -1,
    japanese = this.japanese ?: -1,
    korean = this.korean ?: -1,
    koreanOG = this.koreanOG ?: -1,
    lowestAskCost = this.lowestAskCost ?: -1,
    lowestAskID = this.lowestAskID ?: "",
    name = this.name ?: "",
    number = if(this.number.isNullOrEmpty())  "N/A" else  this.number!! ,
    numberOfFavorites = this.numberOfFavorites ?: -1,
    objectID = this.objectID ?: "",
    oceanicEnglish = this.oceanicEnglish ?: -1,
    portuguese = this.portuguese ?: -1,
    portugueseOG = this.portugueseOG ?: -1,
    productType = getProductType(this.productType ?: "") ?: ProductType.YUGIOH,
    rarity = if(this.rarity.isNullOrEmpty()) "N/A" else this.rarity!!,
    `set` = if(this.`set`.isNullOrEmpty()) "N/A" else this.`set`!! ,
    spanish = this.spanish ?: -1,
    spanishOG = this.spanishOG ?: -1,
    stats = if(this.stats.isNullOrEmpty()) "N/A" else this.stats!! ,
    refKey = key
)

fun RecentSaleModel.toDomainModel(key : String) = RecentSaleDomainModel(
    condition = this.condition?:-1,
    date = this.date?.toRecentSaleDate()?:"",
    edition = this.edition?:-1,
    language = this.language?:-1,
    objectId = this.objectId?:"",
    price = if(this.price!=null) "$${this.price}" else "",
    customization = if(condition==null) "New" else "",
    key = key
)

fun AskItemModel.toAskItemDomainModel() = AskItemDomainModel(
    refKey = refKey,
    creatorUID = creatorUID,
    duration = duration,
    askPrice = askPrice,
    totalPayout = totalPayout,
    itemRefKey = itemRefKey,
    itemObjectID = itemObjectID,
    productType = getProductType(productType.toString()),
    productCategory = getProductCategory(getProductType(productType.toString())!!),
    edition = getEditionTypeFromRowType(edition.toString()),
    condition = condition,
    language = language,
    returnName = returnName,
    returnAddressLine1 = returnAddressLine1,
    returnAddressLine2 = returnAddressLine2,
    returnCity = returnCity,
    returnState = returnState,
    returnZipCode = returnZipCode,
    returnCountry = returnCountry,
    frontImageURL = frontImageURL,
    backImageURL = backImageURL
)

fun ShippingInfoModel.toDomainModel() = ShippingInfoDomainModel(
    billing = billing,
    frontEndShippingProcessing = frontEndShippingProcessing,
    service = service,
    refKey = refKey,
)
