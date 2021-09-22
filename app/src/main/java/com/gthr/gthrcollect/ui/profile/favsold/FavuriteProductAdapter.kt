package com.gthr.gthrcollect.ui.profile.favsold

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemLoadMoreBinding
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.AdapterViewType
import com.gthr.gthrcollect.utils.extensions.getScreenWidth

class FavuriteProductAdapter(val state: CustomProductCell.State, val callback : (data: ProductDisplayModel) -> Unit) :
    ListAdapter<ProductDisplayModel, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ProductDisplayModel>() {
        override fun areItemsTheSame(oldItem: ProductDisplayModel, newItem: ProductDisplayModel): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(oldItem: ProductDisplayModel, newItem: ProductDisplayModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding= ItemTestProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        binding.root.layoutParams.width = (((binding.root.context.getScreenWidth()) - (2*binding.root.context.resources.getDimensionPixelOffset(
            R.dimen.dp_14))) / 2) /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        return FavSoldViewHolder(binding)
    }

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