package com.gthr.gthrcollect.utils.constants

import com.gthr.gthrcollect.BuildConfig

object CloudFunctions {
    const val FIREBASE_FUN_BASE_URL = BuildConfig.LINK_CLOUD_FUNCTION
    const val SEARCH_PRODUCT = "searchProducts"
    const val SEARCH_BIDS = "searchBids"
    const val FUN_DELETE_ASK = "onAskDelete"
    const val SEARCH_ASK = "searchAsk"
    const val SEARCH_COLLECTION = "searchCollections"
    const val FEED_ACTIVITY = "feedActivity"

    //  const val SEARCH_COLLECTION ="searchAlgolia"
    const val SEARCK_KEY = "searchTerm"
    const val PAGE = "pageNo"
    const val SWEEP_TAKES = "excludeSweepstakes"
    const val PRODUCT_CATEGORY = "productCategory"
    const val PRODUCT_TYPE = "productType"
    const val LIMIT = "limit"
    const val USERID = "userId"
    const val UID = "uid"
    const val CODE = "code"
    const val STATE = "state"
    const val COLLECTION_ID = "_collectionId"
    const val SORT_BY = "sortBy"
    const val IS_ASCENDING = "isAscending"
    const val OBJECT_ID = "objectId"
    const val ITEM_OBJECT_ID = "itemObjectID"
    const val UPDATED_PAYOUT_LINK = "getUpdatePayoutLink"
    const val CREATE_STRIPE_ACC = "createStripeAccount"
    const val CREATOR_UID = "creatorUID"
    const val CREATE_EPHEMER_KEY = "createEphemeralKey"
    const val NEW_PAYOUT_URL = "url"
    const val CREATE_BUY_INTENT = "createBuyNowPaymentIntent"
    const val SHIPPING_TIER = "shipping_tier"
    const val ASK_ID = "ask_id"
    const val ASKID = "askId"
    const val APP_FEE = "appFee"
    const val SELLER_PAYOUT = "sellerPayout"
    const val BUY_CHARGE = "buyerCharge"
    const val CLIENT_SECRET = "clientSecret"
    const val BUY_NOW = "buyNow"
    const val BUYER_ADDRESS_KEY = "buyerAddressKey"
    const val SELLER_ADDRESS_KEY = "sellerAddressKey"
    const val ADDRESS_KEY = "addressKey"
    const val SHIPPING_TIER_KEY = "shippingTierKey"
    const val PAYMENT_ID = "paymentId"
    const val RECIEPT = "reciept"
    const val ORDER_STATUS = "order_status"


}