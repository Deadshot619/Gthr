package com.gthr.gthrcollect.ui.productdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemRecentSeleBinding
import com.gthr.gthrcollect.databinding.ItemRecentSeleHerderBinding
import com.gthr.gthrcollect.model.domain.RecentCellDomainModel
import com.gthr.gthrcollect.utils.extensions.getResolvedColor

class RecentSellAdapter(val count : Int) : ListAdapter<RecentCellDomainModel, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<RecentCellDomainModel>() {
        override fun areItemsTheSame(
            oldItem: RecentCellDomainModel,
            newItem: RecentCellDomainModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecentCellDomainModel,
            newItem: RecentCellDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == 0)
            RecentSellHerderViewHolder(
                ItemRecentSeleHerderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        else
            RecentSellViewHolder(
                ItemRecentSeleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (position == 0)
            (holder as RecentSellHerderViewHolder).bind(position)
        else
            (holder as RecentSellViewHolder).bind(position)

    override fun getItemCount() = count

    override fun getItemViewType(position: Int) = position

    inner class RecentSellHerderViewHolder(private val binding: ItemRecentSeleHerderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

        }
    }

    inner class RecentSellViewHolder(private val binding: ItemRecentSeleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvDate.setBackgroundColor(if(position%2==0)
                binding.root.context.getResolvedColor(R.color.extra_light_blue)
            else
                binding.root.context.getResolvedColor(android.R.color.transparent)
            )

            binding.tvSalePrice.setBackgroundColor(
                if(position%2==0)
                    binding.root.context.getResolvedColor(R.color.extra_light_blue)
                else
                    binding.root.context.getResolvedColor(android.R.color.transparent)
            )

            binding.llCustomization.setBackgroundColor(
                if(position%2==0)
                    binding.root.context.getResolvedColor(R.color.extra_light_blue)
                else
                    binding.root.context.getResolvedColor(android.R.color.transparent)
            )
        }
    }


}