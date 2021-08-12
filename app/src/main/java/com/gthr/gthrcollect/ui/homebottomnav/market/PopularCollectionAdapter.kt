package com.gthr.gthrcollect.ui.homebottomnav.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemCollectionBinding
import com.gthr.gthrcollect.model.domain.SearchCollection
import com.gthr.gthrcollect.utils.extensions.getScreenWidth

class PopularCollectionAdapter: ListAdapter<SearchCollection, PopularCollectionAdapter.PopularCollectionViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SearchCollection>() {
        override fun areItemsTheSame(oldItem: SearchCollection, newItem: SearchCollection): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: SearchCollection, newItem: SearchCollection): Boolean {
            return oldItem == newItem
        }
    }

    inner class PopularCollectionViewHolder(var binding:  ItemCollectionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind() {
            binding.ivMain.clipToOutline = true
            binding.root.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PopularCollectionViewHolder{
        val binding = ItemCollectionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.root.layoutParams.width = (((binding.root.context.getScreenWidth()) - (2*binding.root.context.resources.getDimensionPixelOffset(R.dimen.dp_14))) / 2) /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        return PopularCollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularCollectionViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = 10
}