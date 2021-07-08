package com.gthr.gthrcollect.ui.editaccountinfo.eauserinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gthr.gthrcollect.ui.base.BaseViewModel

class EaUserInfoViewModel : BaseViewModel() {

    private val _userInfoLiveData = MutableLiveData<UserInfoModel>()
    val userInfoLiveData: LiveData<UserInfoModel>
        get() = _userInfoLiveData

    fun setUserInfo(userInfoModel: UserInfoModel) {
        _userInfoLiveData.value = userInfoModel
    }


}