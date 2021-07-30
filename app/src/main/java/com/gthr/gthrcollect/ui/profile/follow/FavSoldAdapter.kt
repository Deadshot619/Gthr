package com.gthr.gthrcollect.ui.profile.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.FavSoldDomainModel
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.logger.GthrLogger

class FavSoldAdapter(val cardType:ProfileNavigationType, val callback: (FavSoldDomainModel) -> Unit) :
    ListAdapter<FavSoldDomainModel, FavSoldAdapter.FavSoldViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<FavSoldDomainModel>() {
        override fun areItemsTheSame(oldItem: FavSoldDomainModel, newItem: FavSoldDomainModel): Boolean {
            return oldItem.image_URL == newItem.image_URL
        }

        override fun areContentsTheSame(oldItem: FavSoldDomainModel, newItem: FavSoldDomainModel): Boolean {
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

        GthrLogger.e("card_type",cardType.toString())

        when(cardType) {
            ProfileNavigationType.SOLD -> {
                holder.binding.item.setState(CustomProductCell.State.SOLD)
                holder. binding.item.setType(CustomProductCell.Type.SEALED)
            }
            ProfileNavigationType.FAVOURITES -> {
                holder.binding.item.setState(CustomProductCell.State.FAVORITE)
                holder. binding.item.setType(CustomProductCell.Type.HOLO_RARE)
            }
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