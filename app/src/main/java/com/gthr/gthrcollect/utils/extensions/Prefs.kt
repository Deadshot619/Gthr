package com.gthr.gthrcollect.utils.extensions

import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.utils.Prefs

fun Prefs.updateUserInfoModelData(userInfoDomainModel: UserInfoDomainModel) {
    this.userInfoModel = userInfoDomainModel
}

fun Prefs.updateShippingAddress(shippingAddressList: List<ShippingAddressDomainModel>) {
    this.userInfoModel = this.userInfoModel!!.apply {
        this.addressList = shippingAddressList
    }
}

fun Prefs.updateCollectionInfoModelData(collectionInfoDomainModel: CollectionInfoDomainModel){
    this.collectionInfoModel = collectionInfoDomainModel
}