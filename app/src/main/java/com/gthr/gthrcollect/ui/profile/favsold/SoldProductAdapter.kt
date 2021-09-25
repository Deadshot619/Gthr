package com.gthr.gthrcollect.ui.profile.favsold

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemLoadMoreBinding
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.ItemDisplayDomainModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.AdapterViewType
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.getScreenWidth
import com.gthr.gthrcollect.utils.logger.GthrLogger

class SoldProductAdapter(val callback : (data: ItemDisplayDomainModel) -> Unit) :
    ListAdapter<ItemDisplayDomainModel, SoldProductAdapter.FavSoldViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ItemDisplayDomainModel>() {
        override fun areItemsTheSame(oldItem: ItemDisplayDomainModel, newItem: ItemDisplayDomainModel): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(oldItem: ItemDisplayDomainModel, newItem: ItemDisplayDomainModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavSoldViewHolder {

        val binding= ItemTestProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        binding.root.layoutParams.width = (((binding.root.context.getScreenWidth()) - (2*binding.root.context.resources.getDimensionPixelOffset(
            R.dimen.dp_14))) / 2) /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        return FavSoldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavSoldViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.item.setState(CustomProductCell.State.SOLD)
    }

    inner class FavSoldViewHolder(var binding: ItemTestProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data:ItemDisplayDomainModel) {
            binding.root.setOnClickListener {
                callback(data)
            }
            binding.item.setValue(data)
        }
    }
}