package com.gthr.gthrcollect.ui.homebottomnav.feed


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemCollectionBinding
import com.gthr.gthrcollect.databinding.ItemFeedBinding
import com.gthr.gthrcollect.model.domain.FeedDomainModel
import com.gthr.gthrcollect.utils.extensions.getResolvedColor
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class FeedAdapter : ListAdapter<FeedDomainModel, FeedAdapter.FeedViewHolder>(DiffCallback){

    object DiffCallback : DiffUtil.ItemCallback<FeedDomainModel>(){
        override fun areItemsTheSame(oldItem: FeedDomainModel, newItem: FeedDomainModel): Boolean {
            return oldItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: FeedDomainModel, newItem: FeedDomainModel): Boolean {
            return oldItem == oldItem
        }

    }

    inner class FeedViewHolder(val binding : ItemFeedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(){
            binding.ivProduct.clipToOutline = true
            when(layoutPosition%3){
                0 -> {
                    setBuyList(binding)
                }
                1 -> {
                    setAsk(binding)
                }
                2 -> {
                    setCollection(binding)
                }
            }
            when(layoutPosition%9){
                0,1,2 -> {
                    setCard(binding)
                }
                3,4,5 -> {
                    setSealed(binding)
                }
                6,7,8 -> {
                    setToy(binding)
                }
            }
        }
    }

    private fun setCollection(binding: ItemFeedBinding) {
        binding.tvNewTo.text = binding.tvNewTo.context.getString(R.string.text_feed_new_to_collection)
        binding.tvNewTo.setTextColor(binding.tvNewTo.getResolvedColor(R.color.text_color_dark_green))
        binding.btnBuyNow.gone()
    }

    private fun setAsk(binding: ItemFeedBinding) {
        binding.tvNewTo.text = binding.tvNewTo.context.getString(R.string.text_feed_new_ask)
        binding.tvNewTo.setTextColor(binding.tvNewTo.getResolvedColor(R.color.red))
        binding.btnBuyNow.visible()
    }

    private fun setBuyList(binding: ItemFeedBinding) {
        binding.tvNewTo.text = binding.tvNewTo.context.getString(R.string.text_feed_new_to_buy_list)
        binding.tvNewTo.setTextColor(binding.tvNewTo.getResolvedColor(R.color.text_color_dark_green))
        binding.btnBuyNow.gone()
    }


    private fun setToy(binding: ItemFeedBinding) {
        binding.groupToy.visible()
        binding.groupCard.gone()
        binding.groupSealed.gone()
    }

    private fun setSealed(binding: ItemFeedBinding) {
        binding.groupToy.gone()
        binding.groupCard.gone()
        binding.groupSealed.visible()
    }

    private fun setCard(binding: ItemFeedBinding) {
        binding.groupToy.gone()
        binding.groupCard.visible()
        binding.groupSealed.gone()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder = FeedViewHolder(ItemFeedBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = 10
}