package com.gthr.gthrcollect.ui.profile.my_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.CollectionProductDomainModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell

class CollectionsAdapter(val state: CustomProductCell.State, val callback: (CollectionProductDomainModel) -> Unit) :
    ListAdapter<CollectionProductDomainModel, CollectionsAdapter.FavSoldViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<CollectionProductDomainModel>() {
        override fun areItemsTheSame(oldItem: CollectionProductDomainModel, newItem: CollectionProductDomainModel): Boolean {
            return oldItem.image_URL == newItem.image_URL
        }

        override fun areContentsTheSame(oldItem: CollectionProductDomainModel, newItem: CollectionProductDomainModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavSoldViewHolder =
        FavSoldViewHolder(
            ItemTestProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: FavSoldViewHolder, position: Int) {
        holder.bind()
        holder.binding.item.setState(state)
    }

    override fun getItemCount(): Int = 17

    inner class FavSoldViewHolder(var binding: ItemTestProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {

            }
        }
    }

}