package com.gthr.gthrcollect.ui.profile.my_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.ProductType

class CollectionsAdapter(val state: CustomProductCell.State, val callback: (ProductDisplayModel) -> Unit) : ListAdapter<ProductDisplayModel, CollectionsAdapter.ProductViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ProductDisplayModel>() {
        override fun areItemsTheSame(oldItem: ProductDisplayModel, newItem: ProductDisplayModel): Boolean {
            return oldItem.refKey == newItem.refKey
        }

        override fun areContentsTheSame(oldItem: ProductDisplayModel, newItem: ProductDisplayModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemTestProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind()
    }

    inner class ProductViewHolder(var binding: ItemTestProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(layoutPosition)
            when (item.productType) {
                ProductType.FUNKO -> binding.item.setType(CustomProductCell.Type.FUNKO)
                ProductType.POKEMON -> binding.item.setType(CustomProductCell.Type.CARDS)
                ProductType.MAGIC_THE_GATHERING -> binding.item.setType(CustomProductCell.Type.FUNKO)
                ProductType.YUGIOH -> binding.item.setType(CustomProductCell.Type.CARDS)
                ProductType.SEALED_POKEMON, ProductType.SEALED_MTG, ProductType.SEALED_YUGIOH -> binding.item.setType(
                    CustomProductCell.Type.SEALED
                )
            }

            binding.item.setState(state)
            if(state==CustomProductCell.State.FOR_SALE){
                if(item.isForSale!=true)
                    binding.item.setLabelVisibility(false)
                else
                    binding.item.setLabelVisibility(true)
            }



            binding.item.setValue(item)
            binding.root.setOnClickListener {
                callback(item)
            }
        }
    }

}