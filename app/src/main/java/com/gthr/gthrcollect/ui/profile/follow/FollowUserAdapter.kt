package com.gthr.gthrcollect.ui.profile.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gthr.gthrcollect.databinding.CardFollowBinding
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel

class FollowUserAdapter(
    val callback: FollowUserListener
) :
    ListAdapter<CollectionInfoDomainModel, FollowUserAdapter.FollowUserViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<CollectionInfoDomainModel>() {
        override fun areItemsTheSame(oldItem: CollectionInfoDomainModel, newItem: CollectionInfoDomainModel): Boolean {
            return oldItem.userRefKey == newItem.userRefKey
        }

        override fun areContentsTheSame(oldItem: CollectionInfoDomainModel, newItem: CollectionInfoDomainModel): Boolean {
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
        holder.bind(getItem(position),callback)

  //  override fun getItemCount(): Int = followersList.size

    inner class FollowUserViewHolder(var binding: CardFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CollectionInfoDomainModel, callback: FollowUserListener) {
            binding.root.setOnClickListener {
                callback.onClick(null)
            }

            binding.btnEditAccountInfo.mTvTitle.setText(data.collectionDisplayName)
            Glide.with(binding.root.context).load(data.profileImage).into(binding.userImage)
        }
    }

    interface FollowUserListener {
        fun onClick(collectionInfoDomainModel: CollectionInfoDomainModel?)
    }
}