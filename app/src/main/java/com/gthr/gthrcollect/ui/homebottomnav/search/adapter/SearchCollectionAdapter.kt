package com.gthr.gthrcollect.ui.homebottomnav.search.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemCollectionBinding
import com.gthr.gthrcollect.model.domain.SearchCollection
import com.gthr.gthrcollect.utils.extensions.setProfileImage

class SearchCollectionAdapter(val callback : (data: SearchCollection) -> Unit) : ListAdapter<SearchCollection,SearchCollectionAdapter.SearchCollectionViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SearchCollection>() {
        override fun areItemsTheSame(oldItem: SearchCollection, newItem: SearchCollection): Boolean {
            return oldItem.objectId == newItem.objectId
        }
        override fun areContentsTheSame(oldItem: SearchCollection, newItem: SearchCollection): Boolean {
            return oldItem == newItem
        }
    }

    inner class SearchCollectionViewHolder(var binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data:SearchCollection) {
            binding.ivMain.setProfileImage(data.productImage.toString())
            binding.ivUser.setProfileImage(data.profileUrl.toString())
            binding.tvUserMage.text=data.name
            binding.ivMain.clipToOutline = true
            binding.root.setOnClickListener {
                  callback(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCollectionViewHolder =
        SearchCollectionViewHolder(
            ItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context),parent,false)
        )

    override fun onBindViewHolder(holder: SearchCollectionViewHolder, position: Int) =
        holder.bind(getItem(position))


}