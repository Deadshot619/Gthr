package com.gthr.gthrcollect.ui.productdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.FavSoldDomainModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.ProductType

class UpForSellAdapter(val productType: ProductType, val callback: (FavSoldDomainModel) -> Unit) :
    ListAdapter<FavSoldDomainModel, UpForSellAdapter.FavSoldViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<FavSoldDomainModel>() {
        override fun areItemsTheSame(
            oldItem: FavSoldDomainModel,
            newItem: FavSoldDomainModel
        ): Boolean {
            return oldItem.image_URL == newItem.image_URL
        }

        override fun areContentsTheSame(
            oldItem: FavSoldDomainModel,
            newItem: FavSoldDomainModel
        ): Boolean {
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
        holder.binding.item.setState(CustomProductCell.State.FOR_SALE)
        when (productType) {
            ProductType.FUNKO -> holder.binding.item.setType(CustomProductCell.Type.FUNKO)
            ProductType.POKEMON -> holder.binding.item.setType(CustomProductCell.Type.CARDS)
            ProductType.MAGIC_THE_GATHERING -> holder.binding.item.setType(CustomProductCell.Type.FUNKO)
            ProductType.YUGIOH -> holder.binding.item.setType(CustomProductCell.Type.CARDS)
            ProductType.SEALED_YUGIOH, ProductType.SEALED_POKEMON, ProductType.SEALED_MTG -> holder.binding.item.setType(
                CustomProductCell.Type.CARDS
            )
        }
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