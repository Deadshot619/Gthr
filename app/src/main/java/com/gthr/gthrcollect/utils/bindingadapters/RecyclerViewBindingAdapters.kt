package com.gthr.gthrcollect.utils.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.ui.settings.editshippingaddress.ShippingAddressAdapter

@BindingAdapter("listShippingAddress")
fun bindShippingAddressRecyclerView(recyclerView: RecyclerView, data: List<ShippingAddressDomainModel>?) {
    val adapter = recyclerView.adapter as ShippingAddressAdapter
    adapter.submitList(data)
}