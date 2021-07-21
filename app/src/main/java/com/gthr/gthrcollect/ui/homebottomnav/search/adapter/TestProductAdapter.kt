package com.gthr.gthrcollect.ui.homebottomnav.search.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemCollectionBinding
import com.gthr.gthrcollect.databinding.ItemTestProductBinding
import com.gthr.gthrcollect.model.domain.SearchCollection
import com.gthr.gthrcollect.utils.customviews.CustomProductCell

class TestProductAdapter(val state : CustomProductCell.State) : ListAdapter<SearchCollection,TestProductAdapter.TestProductViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SearchCollection>() {
        override fun areItemsTheSame(oldItem: SearchCollection, newItem: SearchCollection): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: SearchCollection, newItem: SearchCollection): Boolean {
            return oldItem == newItem
        }
    }

    inner class TestProductViewHolder(var binding: ItemTestProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            when(layoutPosition){
                0 -> binding.item.setType(CustomProductCell.Type.FUNKO)
                1 -> binding.item.setType(CustomProductCell.Type.SEALED)
                2 -> binding.item.setType(CustomProductCell.Type.MYTHIC)
                3 -> binding.item.setType(CustomProductCell.Type.HOLO_RARE)
                4 -> binding.item.setType(CustomProductCell.Type.SECRET_RARE)
            }
            when(state){
                CustomProductCell.State.FOR_SALE -> binding.item.setState(CustomProductCell.State.FOR_SALE)
                CustomProductCell.State.WANT -> binding.item.setState(CustomProductCell.State.WANT)
                CustomProductCell.State.NORMAL -> binding.item.setState(CustomProductCell.State.NORMAL)
                CustomProductCell.State.FAVORITE -> binding.item.setState(CustomProductCell.State.FAVORITE)
                CustomProductCell.State.OFFER -> binding.item.setState(CustomProductCell.State.OFFER)
                CustomProductCell.State.SOLD -> binding.item.setState(CustomProductCell.State.SOLD)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestProductViewHolder =
        TestProductViewHolder(
            ItemTestProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: TestProductViewHolder, position: Int) =
        holder.bind()

    override fun getItemCount(): Int = 5

}