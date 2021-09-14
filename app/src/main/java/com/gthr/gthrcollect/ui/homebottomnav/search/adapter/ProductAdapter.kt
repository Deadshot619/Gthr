package com.gthr.gthrcollect.ui.homebottomnav.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemLoadMoreBinding
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.AdapterViewType

class ProductAdapter(val state: CustomProductCell.State, val callback : (data: ProductDisplayModel) -> Unit) :
    ListAdapter<ProductDisplayModel, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ProductDisplayModel>() {
        override fun areItemsTheSame(oldItem: ProductDisplayModel, newItem: ProductDisplayModel): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(oldItem: ProductDisplayModel, newItem: ProductDisplayModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == AdapterViewType.VIEW_TYPE_ITEM.value)
        FavSoldViewHolder(
            ItemTestProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        else
            ItemLoadMoreViewHolder(
                ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItem(position).viewType== AdapterViewType.VIEW_TYPE_ITEM)
            (holder as FavSoldViewHolder).bind(getItem(position))
    }

    inner class FavSoldViewHolder(var binding: ItemTestProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data:ProductDisplayModel) {
            binding.root.setOnClickListener {
                callback(data)
            }
            binding.item.setState(state)
            binding.item.setValue(data)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.value
    }

    inner class ItemLoadMoreViewHolder(val binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)

}