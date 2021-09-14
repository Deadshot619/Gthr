package com.gthr.gthrcollect.ui.homebottomnav.search.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemCollectionBinding
import com.gthr.gthrcollect.databinding.ItemLoadMoreBinding
import com.gthr.gthrcollect.model.domain.SearchCollection
import com.gthr.gthrcollect.utils.enums.AdapterViewType
import com.gthr.gthrcollect.utils.extensions.setCollectionProductImage
import com.gthr.gthrcollect.utils.extensions.setProfileImage

class SearchCollectionAdapter(val callback: (data: SearchCollection) -> Unit) :
    ListAdapter<SearchCollection, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SearchCollection>() {
        override fun areItemsTheSame(
            oldItem: SearchCollection,
            newItem: SearchCollection
        ): Boolean {
            return oldItem.objectId == newItem.objectId
        }

        override fun areContentsTheSame(
            oldItem: SearchCollection,
            newItem: SearchCollection
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class SearchCollectionViewHolder(var binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchCollection) {
            binding.ivMain.setCollectionProductImage(data.productImage.toString())
            binding.ivUser.setProfileImage(data.profileUrl.toString())
            binding.tvUserMage.text = data.name
            binding.ivMain.clipToOutline = true
            binding.root.setOnClickListener {
                callback(data)
            }
        }
    }

    inner class ItemLoadMoreViewHolder(val binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == AdapterViewType.VIEW_TYPE_ITEM.value)
            SearchCollectionViewHolder(
                ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        else
            ItemLoadMoreViewHolder(
                ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
        if(getItem(position).viewType== AdapterViewType.VIEW_TYPE_ITEM)
            (holder as SearchCollectionViewHolder).bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.value
    }
}