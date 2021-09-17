package com.gthr.gthrcollect.data.repository

import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.utils.constants.DynamicLinkConstants
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class DynamicLinkRepository {

    private val dynamicLink = Firebase.dynamicLinks

    //https://gthrcollect.page.link/product?objectID=test&productType=test2
    fun getProductDynamicLink(objectID : String,productType : ProductType) = flow<State<String>>{
        emit(State.loading())
        val await = dynamicLink.createDynamicLink()
            .setLink(Uri.parse(DynamicLinkConstants.PRODUCT_URL+DynamicLinkConstants.OBJECT_ID+"="+objectID+"&"+DynamicLinkConstants.PRODUCT_TYPE+"="+productType.title))
            .setDomainUriPrefix(DynamicLinkConstants.DOMAIN_URI_PREFIX)
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build()) // Open links with this app on Android/
            //.setIosParameters(IosParameters.Builder("com.appinventiv.ios").build()) / Open links with com.example.ios on iOS
            .buildShortDynamicLink().await()
        val shortLink: Uri = await.shortLink!!
        emit (State.Success(shortLink.toString()))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    //https://gthrcollect.page.link/collections?collectionID=test
    fun getCollectionsDynamicLink(collectionID : String) = flow<State<String>>{
        emit(State.loading())
        val await = dynamicLink.createDynamicLink()
            .setLink(Uri.parse(DynamicLinkConstants.COLLECTIONS_URL+DynamicLinkConstants.COLLECTION_ID+"="+collectionID))
            .setDomainUriPrefix(DynamicLinkConstants.DOMAIN_URI_PREFIX)
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build()) // Open links with this app on Android/
            //.setIosParameters(IosParameters.Builder("com.appinventiv.ios").build()) / Open links with com.example.ios on iOS
            .buildShortDynamicLink().await()
        val shortLink: Uri = await.shortLink!!
        emit (State.Success(shortLink.toString()))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}