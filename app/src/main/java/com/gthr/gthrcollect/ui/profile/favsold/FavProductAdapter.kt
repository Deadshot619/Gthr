package com.gthr.gthrcollect.ui.profile.favsold

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.extensions.getScreenWidth

class FavProductAdapter(val callback : (data: ProductDisplayModel) -> Unit) :
    ListAdapter<ProductDisplayModel, FavProductAdapter.FavSoldViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ProductDisplayModel>() {
        override fun areItemsTheSame(oldItem: ProductDisplayModel, newItem: ProductDisplayModel): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(oldItem: ProductDisplayModel, newItem: ProductDisplayModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavSoldViewHolder {

       /* val binding= ItemTestProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        binding.root.layoutParams.width = (((binding.root.context.getScreenWidth()) - (2*binding.root.context.resources.getDimensionPixelOffset(
            R.dimen.dp_14))) / 2)*/

        val binding = ItemTestProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        return FavSoldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavSoldViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.item.setState(CustomProductCell.State.FAVORITE)
    }

    inner class FavSoldViewHolder(var binding: ItemTestProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data:ProductDisplayModel) {
            binding.root.setOnClickListener {
                callback(data)
            }
            binding.item.setValue(data)
        }
    }
}