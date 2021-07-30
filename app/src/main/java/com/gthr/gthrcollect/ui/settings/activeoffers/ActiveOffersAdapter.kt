package com.gthr.gthrcollect.ui.settings.activeoffers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.OffersDomainModel

class ActiveOffersAdapter(val callback: (OffersDomainModel) -> Unit) :
    ListAdapter<OffersDomainModel, ActiveOffersAdapter.ActiveOffersViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<OffersDomainModel>() {
        override fun areItemsTheSame(oldItem: OffersDomainModel, newItem: OffersDomainModel): Boolean {
            return oldItem.image_URL == newItem.image_URL
        }

        override fun areContentsTheSame(oldItem: OffersDomainModel, newItem: OffersDomainModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveOffersViewHolder =
        ActiveOffersViewHolder(
            ItemTestProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ActiveOffersViewHolder, position: Int) =
        holder.bind()

    override fun getItemCount(): Int = 17

    inner class ActiveOffersViewHolder(var binding: ItemTestProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
            }
        }
    }

}