package com.gthr.gthrcollect.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: AskFlowRepository) : BaseViewModel() {

    var mIsPayoutAuth: Boolean = false
        private set

    fun setPayoutAuth(auth: Boolean) {
        mIsPayoutAuth = auth
    }


    //Variable to indicate whether Collection data has been updated in Firebase
    private val _stripeAccId = MutableLiveData<Event<State<Boolean>>>()
    val stripeAccId: LiveData<Event<State<Boolean>>>
        get() = _stripeAccId

    fun checkStripeAccId(userId: String? = null) {
        viewModelScope.launch {
            repository.authStripeAccount(userId).collect {
                _stripeAccId.value = Event(it)
            }
        }
    }

    //Variable to get Updated Payout Link of Stripe
    private val _payoutLink = MutableLiveData<Event<State<String>>>()
    val payoutLink: LiveData<Event<State<String>>>
        get() = _payoutLink

    fun getPayoutLink() {
        viewModelScope.launch {
            repository.getStripePayoutLink().collect {
                _payoutLink.value = Event(it)
            }
        }
    }
}