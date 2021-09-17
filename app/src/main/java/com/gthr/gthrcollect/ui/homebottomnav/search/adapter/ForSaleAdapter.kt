package com.gthr.gthrcollect.ui.homebottomnav.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemLoadMoreBinding
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.network.cloudfunction.ForSaleItemModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.AdapterViewType

class ForSaleAdapter(val state: CustomProductCell.State, val callback : (data: ForSaleItemModel) -> Unit) :
    ListAdapter<ForSaleItemModel, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ForSaleItemModel>() {
        override fun areItemsTheSame(oldItem: ForSaleItemModel, newItem: ForSaleItemModel): Boolean {
            return oldItem.askRefKey == newItem.askRefKey
        }

        override fun areContentsTheSame(oldItem: ForSaleItemModel, newItem: ForSaleItemModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == AdapterViewType.VIEW_TYPE_ITEM.value)
        FavSaleViewHolder(
            ItemTestProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        else
            ItemLoadMoreViewHolder(
                ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as FavSaleViewHolder).bind(getItem(position))}



    inner class FavSaleViewHolder(var binding: ItemTestProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data:ForSaleItemModel) {
            binding.root.setOnClickListener {
                callback(data)
            }
            binding.item.setState(state)
        }
    }

    inner class ItemLoadMoreViewHolder(val binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)

}