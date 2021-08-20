package com.gthr.gthrcollect.ui.homebottomnav.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemUpForSellBinding
import com.gthr.gthrcollect.model.domain.UpForSellDomainModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.ProductTypeOld
import com.gthr.gthrcollect.utils.extensions.getScreenWidth

class ProductAdapter(val productType: ProductTypeOld, val state: CustomProductCell.State) : ListAdapter<UpForSellDomainModel,ProductAdapter.UpForSellViewHolder>(DriftUtils)  {

    companion object DriftUtils : DiffUtil.ItemCallback<UpForSellDomainModel>() {
        override fun areItemsTheSame(oldItem: UpForSellDomainModel, newItem: UpForSellDomainModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UpForSellDomainModel, newItem: UpForSellDomainModel): Boolean {
            return oldItem == newItem
        }

    }

    inner class UpForSellViewHolder(val binding : ItemUpForSellBinding) : RecyclerView.ViewHolder(binding.root) {
          fun bind(position: Int){
              binding.cpsMain.setState(state)
              when(productType) {
                  ProductTypeOld.FUNKO -> binding.cpsMain.setType(CustomProductCell.Type.FUNKO)
                  ProductTypeOld.POKEMON -> binding.cpsMain.setType(CustomProductCell.Type.HOLO_RARE)
                  ProductTypeOld.MTG -> binding.cpsMain.setType(CustomProductCell.Type.FUNKO)
                  ProductTypeOld.YUGIOH -> binding.cpsMain.setType(CustomProductCell.Type.SECRET_RARE)
                  ProductTypeOld.SEALED -> binding.cpsMain.setType(CustomProductCell.Type.SEALED)
              }
          }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : UpForSellViewHolder {
        val binding = ItemUpForSellBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.root.layoutParams.width = (((binding.root.context.getScreenWidth()) - (2*binding.root.context.resources.getDimensionPixelOffset(R.dimen.margin_normal))) / 2) /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        return UpForSellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpForSellViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount() = 10
}