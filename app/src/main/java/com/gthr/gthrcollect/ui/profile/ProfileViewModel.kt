package com.gthr.gthrcollect.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toRealtimeDatabaseModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionItemModel
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(private val mProfileRepository: ProfileRepository, private val mDynamicLinkRepository : DynamicLinkRepository, private val otherUserId: String?) : BaseViewModel() {

    private var uploadImageJob: Job? = null

    //Variable to hold User data retrieved from FireStore
    private val _userCollectionInfo = MutableLiveData<Event<State<CollectionInfoDomainModel>>>()
    val userCollectionInfo: LiveData<Event<State<CollectionInfoDomainModel>>>
        get() = _userCollectionInfo

    //Variable to hold User data retrieved from FireStore
    private val _followersList = MutableLiveData<Event<State<List<CollectionInfoDomainModel>>>>()
    val followersList: LiveData<Event<State<List<CollectionInfoDomainModel>>>>
        get() = _followersList

    //Variable to hold User data retrieved from FireStore
    private val _followingList = MutableLiveData<Event<State<List<CollectionInfoDomainModel>>>>()
    val followingList: LiveData<Event<State<List<CollectionInfoDomainModel>>>>
        get() = _followingList

    private val _followUser = MutableLiveData<Event<State<String>>>()
    val followUser: LiveData<Event<State<String>>>
        get() = _followUser

    private val _unFollowUser = MutableLiveData<Event<State<String>>>()
    val unFollowUser: LiveData<Event<State<String>>>
        get() = _unFollowUser

    private val _mProductDynamicLink = MutableLiveData<Event<State<String>>>()
    val mProductDynamicLink: LiveData<Event<State<String>>>
        get() = _mProductDynamicLink

    private val _mAllCollectionProduct = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val mAllCollectionProduct: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _mAllCollectionProduct

    private val _mAllBidProduct = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val mAllBidProduct: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _mAllBidProduct

    private val _mDisplayCollectionProduct = MutableLiveData<Event<List<ProductDisplayModel>>>()
    val mDisplayCollectionProduct: MutableLiveData<Event<List<ProductDisplayModel>>>
        get() = _mDisplayCollectionProduct

    var mAllCollectionProductList = listOf<ProductDisplayModel>()
        private set

    var mAllBidProductList = listOf<ProductDisplayModel>()
        private set

    fun setAllBidProduct(list : List<ProductDisplayModel>){
        mAllBidProductList = list
    }

    fun setAllCollectionProductList(list : List<ProductDisplayModel>){
        mAllCollectionProductList = list
    }

    fun setDisplayCollectionProducts(list : List<ProductDisplayModel>){
        _mDisplayCollectionProduct.value = Event(list)
    }


    init {
        fetchUserProfileData(otherUserId ?: GthrCollect.prefs?.getUserCollectionId().toString())
    }

    fun fetchUserProfileData(collectionId: String) {
        viewModelScope.launch {
            mProfileRepository.fetchUserProfileData(collectionId).collect {
                _userCollectionInfo.value = Event(it)
            }
        }
    }

    fun fetchFollowingData(collectionId: String) {
        viewModelScope.launch {
            mProfileRepository.fetchMyFollowing(collectionId).collect {
                _followersList.value = Event(it)
            }
        }
    }

    fun fetchFollowersData(collectionId: String) {
        viewModelScope.launch {
            mProfileRepository.fetchMyFollowersList(collectionId).collect {
                _followingList.value = Event(it)
            }
        }
    }

    fun followToUser(collectionId: String) {
        viewModelScope.launch {
            mProfileRepository.followToUser(collectionId).collect {
                _followUser.value = Event(it)
            }
        }
    }

    fun unFollowToUser(collectionId: String) {
        viewModelScope.launch {
            mProfileRepository.unFollowToUser(collectionId).collect {
                _unFollowUser.value = Event(it)
            }
        }
    }

    fun getProductDynamicLink(value : String){
        viewModelScope.launch {
            mDynamicLinkRepository.getCollectionsDynamicLink(value).collect {
                _mProductDynamicLink.value = Event(it)
            }
        }
    }

    fun getCollectionProduct(map : Map<String, CollectionItemModel>){
        viewModelScope.launch {
            mProfileRepository.getCollectionProduct(map).collect {
                _mAllCollectionProduct.value = Event(it)
            }
        }
    }
    fun fetchBidProducts(collectionId : String){
        viewModelScope.launch {
            mProfileRepository.fetchBidProducts(collectionId).collect {
                _mAllBidProduct.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        uploadImageJob?.cancel()
    }

}