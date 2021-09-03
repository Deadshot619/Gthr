package com.gthr.gthrcollect.ui.homebottomnav.search.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemCollectionBinding
import com.gthr.gthrcollect.model.domain.SearchCollection

class SearchCollectionAdapter(val callback : (position : Int) -> Unit) : ListAdapter<SearchCollection,SearchCollectionAdapter.SearchCollectionViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SearchCollection>() {
        override fun areItemsTheSame(oldItem: SearchCollection, newItem: SearchCollection): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: SearchCollection, newItem: SearchCollection): Boolean {
            return oldItem == newItem
        }
    }

    inner class SearchCollectionViewHolder(var binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.ivMain.clipToOutline = true
            binding.root.setOnClickListener {
                  callback(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCollectionViewHolder =
        SearchCollectionViewHolder(
            ItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context),parent,false)
        )

    override fun onBindViewHolder(holder: SearchCollectionViewHolder, position: Int) =
        holder.bind()

    override fun getItemCount(): Int = 1

}