package com.gthr.gthrcollect.ui.settings.editshippingaddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.LayoutShippingAddressBinding
import com.gthr.gthrcollect.model.domain.ShippingAddress

class ShippingAddressAdapter(val clickListener: SAClickListener) :
    ListAdapter<ShippingAddress, ShippingAddressAdapter.ShippingAddressViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ShippingAddress>() {
        override fun areItemsTheSame(oldItem: ShippingAddress, newItem: ShippingAddress): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShippingAddress,
            newItem: ShippingAddress
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingAddressViewHolder {
        return ShippingAddressViewHolder(
            LayoutShippingAddressBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ShippingAddressViewHolder, position: Int) {
        holder.onBind(getItem(position), clickListener)
    }

    class ShippingAddressViewHolder(private var bindingView: LayoutShippingAddressBinding) :
        RecyclerView.ViewHolder(bindingView.root) {
        fun onBind(shippingAddress: ShippingAddress, clickListener: SAClickListener) {
            bindingView.cavAddress.run {
                updateValues(shippingAddress)
                setOnClickListener {
                    clickListener.onClickAddress(shippingAddress)
                    if (isViewSelected)
                        setUnselected()
                    else
                        setSelected()
                }
                onEditClick {
                    clickListener.onClickEdit(shippingAddress)
                }
                onDeleteClick {
                    clickListener.onClickDelete(shippingAddress)
                }
            }


            bindingView.executePendingBindings()
        }
    }

    interface SAClickListener {
        fun onClickAddress(shippingAddress: ShippingAddress)
        fun onClickEdit(shippingAddress: ShippingAddress)
        fun onClickDelete(shippingAddress: ShippingAddress)
    }
}



