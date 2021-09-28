package com.gthr.gthrcollect.utils.extensions

import androidx.lifecycle.MutableLiveData
import com.gthr.gthrcollect.model.domain.FeedDomainModel
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.SearchCollection
import com.gthr.gthrcollect.utils.enums.AdapterViewType

fun <T> MutableLiveData<MutableList<T>>.clear() {
    val oldValue = this.value ?: mutableListOf()
    oldValue.clear()
    this.value = oldValue
}

fun MutableLiveData<MutableList<SearchCollection>>.addSearchCollectionLoadMore() {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(SearchCollection("","","","",viewType = AdapterViewType.VIEW_TYPE_LOADING))
    this.value = oldValue
}

fun MutableLiveData<MutableList<SearchCollection>>.removeSearchCollectionLoadMore() {
    val oldValue = this.value ?: mutableListOf()
    removeLoadingModelFromSearchCollection(oldValue)
    this.value = oldValue
}

fun MutableLiveData<MutableList<SearchCollection>>.addAllSearchCollection(item: List<SearchCollection>) {
    val oldValue = this.value ?: mutableListOf()
    removeLoadingModelFromSearchCollection(oldValue)
    oldValue.addAll(item)
    this.value = oldValue
}

fun removeLoadingModelFromSearchCollection(oldValue: MutableList<SearchCollection>) {
    if(oldValue.size>0){
        val last = oldValue.last()
        if(last.viewType== AdapterViewType.VIEW_TYPE_LOADING)
            oldValue.remove(last)
    }
}

fun MutableLiveData<MutableList<ProductDisplayModel>>.addProductDisplayModelLoadMore() {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(ProductDisplayModel(viewType = AdapterViewType.VIEW_TYPE_LOADING))
    this.value = oldValue
}

fun MutableLiveData<MutableList<ProductDisplayModel>>.removeProductDisplayModelLoadMore() {
    val oldValue = this.value ?: mutableListOf()
    removeLoadingModelFromProductDisplayModel(oldValue)
    this.value = oldValue
}

fun MutableLiveData<MutableList<ProductDisplayModel>>.addAllProductDisplayModel(item: List<ProductDisplayModel>) {
    val oldValue = this.value ?: mutableListOf()
    removeLoadingModelFromProductDisplayModel(oldValue)
    oldValue.addAll(item)
    this.value = oldValue
}

fun removeLoadingModelFromProductDisplayModel(oldValue: MutableList<ProductDisplayModel>) {
    if(oldValue.size>0){
        val last = oldValue.last()
        if(last.viewType== AdapterViewType.VIEW_TYPE_LOADING)
            oldValue.remove(last)
    }
}

fun MutableLiveData<MutableList<FeedDomainModel>>.addFeedLoadMore() {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(FeedDomainModel(viewType = AdapterViewType.VIEW_TYPE_LOADING))
    this.value = oldValue
}

fun MutableLiveData<MutableList<FeedDomainModel>>.removeFeedLoadMore() {
    val oldValue = this.value ?: mutableListOf()
    removeLoadingModelFromFeed(oldValue)
    this.value = oldValue
}

fun MutableLiveData<MutableList<FeedDomainModel>>.addAllFeed(item: List<FeedDomainModel>) {
    val oldValue = this.value ?: mutableListOf()
    removeLoadingModelFromFeed(oldValue)
    oldValue.addAll(item)
    this.value = oldValue
}

fun removeLoadingModelFromFeed(oldValue: MutableList<FeedDomainModel>) {
    if(oldValue.size>0){
        val last = oldValue.last()
        if(last.viewType== AdapterViewType.VIEW_TYPE_LOADING)
            oldValue.remove(last)
    }
}
