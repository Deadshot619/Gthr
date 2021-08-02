package com.gthr.gthrcollect.ui.profile.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.CardFollowBinding
import com.gthr.gthrcollect.model.domain.FollowDomainModel

class FollowUserAdapter(val callback: FollowUserListener) :
    ListAdapter<FollowDomainModel, FollowUserAdapter.FollowUserViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<FollowDomainModel>() {
        override fun areItemsTheSame(oldItem: FollowDomainModel, newItem: FollowDomainModel): Boolean {
            return oldItem.image_URL == newItem.image_URL
        }

        override fun areContentsTheSame(oldItem: FollowDomainModel, newItem: FollowDomainModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowUserViewHolder =
        FollowUserViewHolder(
            CardFollowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: FollowUserViewHolder, position: Int) =
        holder.bind(/*getItem(position),*/ callback)

    override fun getItemCount(): Int = 17

    inner class FollowUserViewHolder(var binding: CardFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(/*data: FollowDomainModel,*/ callback: FollowUserListener) {
            binding.root.setOnClickListener {
                callback.onClick(null)
            }
        }
    }

    interface FollowUserListener {
        fun onClick(followDomainModel: FollowDomainModel?)
    }
}