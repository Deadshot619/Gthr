package com.gthr.gthrcollect.ui.settings.editshippingaddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.LayoutShippingAddressBinding
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel

class ShippingAddressAdapter(private val clickListener: SAClickListener) :
    ListAdapter<ShippingAddressDomainModel, ShippingAddressAdapter.ShippingAddressViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ShippingAddressDomainModel>() {
        override fun areItemsTheSame(oldItem: ShippingAddressDomainModel, newItem: ShippingAddressDomainModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShippingAddressDomainModel,
            newItem: ShippingAddressDomainModel
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
        fun onBind(shippingAddressDomainModel: ShippingAddressDomainModel, clickListener: SAClickListener) {
            bindingView.cavAddress.run {
                updateValues(shippingAddressDomainModel)
                setOnClickListener {
                    val index= layoutPosition
                    shippingAddressDomainModel.addresKey=index
                    clickListener.onClickAddress(shippingAddressDomainModel)
                }
                onEditClick {
                    clickListener.onClickEdit(shippingAddressDomainModel)
                }
                onDeleteClick {
                    clickListener.onClickDelete(shippingAddressDomainModel)
                }
            }
            bindingView.executePendingBindings()
        }
    }

    interface SAClickListener {
        fun onClickAddress(shippingAddressDomainModel: ShippingAddressDomainModel)
        fun onClickEdit(shippingAddressDomainModel: ShippingAddressDomainModel)
        fun onClickDelete(shippingAddressDomainModel: ShippingAddressDomainModel)
    }



}



