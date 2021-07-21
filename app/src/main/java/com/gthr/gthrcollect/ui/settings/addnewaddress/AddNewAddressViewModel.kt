package com.gthr.gthrcollect.ui.settings.addnewaddress

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AddressRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CountryStatesModelItem
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.model.mapper.toFirestoreModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.extensions.fromJsonString
import com.gthr.gthrcollect.utils.extensions.gson
import com.gthr.gthrcollect.utils.extensions.toJsonString
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddNewAddressViewModel(private val context: Context,private  val repository : AddressRepository) : BaseViewModel() {


    private val _addAddress = MutableLiveData<Event<State<List<ShippingAddressDomainModel>>>>()
    val addAddress: LiveData<Event<State<List<ShippingAddressDomainModel>>>>
        get() = _addAddress

    var countryList: ArrayList<String>? = null
    private var mListOfCountries: List<CountryStatesModelItem>? = null

    private val _stateList = MutableLiveData<ArrayList<String>>()
    val stateList: LiveData<ArrayList<String>>
        get() = _stateList


    init {
        getAssetJsonData()
    }


    fun updateAddressList(list : List<ShippingAddressDomainModel>){
        viewModelScope.launch {
            repository.updateAddressListFirestore(list.toFirestoreModel(),GthrCollect.prefs?.signedInUser!!.uid).collect {
                _addAddress.value = Event(it)
            }
        }
    }






    fun setShippingAddress(shippingAddressDomainModel: ShippingAddressDomainModel) {
        val addresses = GthrCollect.prefs?.shippingAddresses ?: "[]"
        val listOfAddressDomainModels: MutableList<ShippingAddressDomainModel> =
            Gson().fromJsonString<List<ShippingAddressDomainModel>>(addresses)?.toMutableList()
                ?: mutableListOf()
        listOfAddressDomainModels.add(shippingAddressDomainModel)
        listOfAddressDomainModels.toList()
        GthrCollect.prefs?.shippingAddresses = gson.toJsonString(listOfAddressDomainModels)
    }

    private fun getAssetJsonData() {
        try {
            val jsonfile: String =
                context.assets.open("usa_states.json").bufferedReader().use { it.readText() }
            mListOfCountries =
                gson.fromJsonString<Array<CountryStatesModelItem>>(jsonfile)?.toList()

            countryList = ArrayList()
            val tempStateList = arrayListOf<String>()

            countryList?.add(context.getString(R.string.select_country))
            tempStateList.add(context.getString(R.string.select_states))
            _stateList.value = tempStateList

            for (itemes in mListOfCountries!!) {
                countryList?.add(itemes.country)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getStates(countryName: String) {
        val tempStateList = arrayListOf<String>()
        tempStateList.add(context.getString(R.string.select_states))

        for(count in mListOfCountries!!){
            if ( count.country==countryName){
                for (states in count.states) {
                    tempStateList.add(states.name)
                }
                break
            }
        }
        _stateList.value = tempStateList
    }
}