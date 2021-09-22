package com.gthr.gthrcollect.ui.productdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell

class UpForSellAdapter(
    val state: CustomProductCell.State,
    val callback: (data: ProductDisplayModel) -> Unit
) :
    ListAdapter<ProductDisplayModel, UpForSellAdapter.UpForSellViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ProductDisplayModel>() {
        override fun areItemsTheSame(
            oldItem: ProductDisplayModel,
            newItem: ProductDisplayModel
        ): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(
            oldItem: ProductDisplayModel,
            newItem: ProductDisplayModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpForSellViewHolder =
        UpForSellViewHolder(
            ItemTestProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: UpForSellViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UpForSellViewHolder(var binding: ItemTestProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductDisplayModel) {
            binding.root.setOnClickListener {
                callback(data)
            }
            binding.item.setState(state)
            binding.item.setValue(data)
        }
    }

}