package com.gthr.gthrcollect.ui.productdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemUpForSellBinding
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.getCellWidth

class ProductAdapter(val productType: ProductType, val state: CustomProductCell.State,val callback : (data: ProductDisplayModel) -> Unit) :
    ListAdapter<ProductDisplayModel, ProductAdapter.UpForSellViewHolder>(DriftUtils) {

    companion object DriftUtils : DiffUtil.ItemCallback<ProductDisplayModel>() {
        override fun areItemsTheSame(
            oldItem: ProductDisplayModel,
            newItem: ProductDisplayModel
        ): Boolean {
            return oldItem.refKey == newItem.refKey
        }

        override fun areContentsTheSame(
            oldItem: ProductDisplayModel,
            newItem: ProductDisplayModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    inner class UpForSellViewHolder(val binding : ItemUpForSellBinding) : RecyclerView.ViewHolder(binding.root) {
          fun bind(){
              binding.root.setOnClickListener {
                  callback(getItem(layoutPosition))
              }
              binding.cpsMain.setState(state)
              binding.cpsMain.setValue(getItem(layoutPosition))
              when (productType) {
                  ProductType.FUNKO -> binding.cpsMain.setType(CustomProductCell.Type.FUNKO)
                  ProductType.POKEMON -> binding.cpsMain.setType(CustomProductCell.Type.CARDS)
                  ProductType.MAGIC_THE_GATHERING -> binding.cpsMain.setType(CustomProductCell.Type.FUNKO)
                  ProductType.YUGIOH -> binding.cpsMain.setType(CustomProductCell.Type.CARDS)
                  ProductType.SEALED_POKEMON, ProductType.SEALED_MTG, ProductType.SEALED_YUGIOH -> binding.cpsMain.setType(
                      CustomProductCell.Type.SEALED
                  )
              }
          }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : UpForSellViewHolder {
        val binding = ItemUpForSellBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.root.layoutParams.width = binding.root.getCellWidth()
        return UpForSellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpForSellViewHolder, position: Int) = holder.bind()

}