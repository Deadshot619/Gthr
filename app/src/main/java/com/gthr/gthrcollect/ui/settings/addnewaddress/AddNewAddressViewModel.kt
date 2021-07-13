package com.gthr.gthrcollect.ui.settings.addnewaddress

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.model.domain.CountryStatesModelItem
import com.gthr.gthrcollect.model.domain.ShippingAddress
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.extensions.fromJsonString
import com.gthr.gthrcollect.utils.extensions.gson
import com.gthr.gthrcollect.utils.extensions.toJsonString

class AddNewAddressViewModel(private val context: Context) : BaseViewModel() {

    var countryList: ArrayList<String>? = null
    private var mListOfCountries: List<CountryStatesModelItem>? = null

    private val _stateList = MutableLiveData<ArrayList<String>>()
    val stateList: LiveData<ArrayList<String>>
        get() = _stateList


    init {
        getAssetJsonData()
    }

    fun setShippingAddress(shippingAddress: ShippingAddress) {
        val addresses = GthrCollect.prefs?.shippingAddresses ?: "[]"
        val listOfAddress: MutableList<ShippingAddress> =
            Gson().fromJsonString<List<ShippingAddress>>(addresses)?.toMutableList()
                ?: mutableListOf()
        listOfAddress.add(shippingAddress)
        listOfAddress.toList()
        GthrCollect.prefs?.shippingAddresses = gson.toJsonString(listOfAddress)
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