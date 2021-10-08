package com.gthr.gthrcollect.ui.homebottomnav.feed


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemFeedBinding
import com.gthr.gthrcollect.databinding.ItemLoadMoreBinding
import com.gthr.gthrcollect.model.domain.FeedDomainModel
import com.gthr.gthrcollect.utils.enums.AdapterViewType
import com.gthr.gthrcollect.utils.enums.FeedType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.extensions.*
import com.gthr.gthrcollect.utils.getProductCategory
import com.gthr.gthrcollect.utils.helper.getConditionTitle
import com.gthr.gthrcollect.utils.helper.getPokemonLanguageDomainModel

class FeedAdapter(val listener: FeedListener) : ListAdapter<FeedDomainModel, RecyclerView.ViewHolder>(DiffCallback){

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
            val item = getItem(layoutPosition)
            binding.ivProduct.clipToOutline = true

            item.feedType?.let {
                when(it){
                    FeedType.BID -> setBuyList(binding)
                    FeedType.ASK -> setAsk(binding)
                    FeedType.COLLECTION -> setCollection(binding)
                }
            }

            item.productCategory?.let {
                when(it){
                    ProductCategory.CARDS -> {
                        setCard(binding)
                        if(item.condition!=null){
                            binding.tvCondition.text = binding.root.context?.getConditionTitle(item.condition.type)
                            binding.tvConditionValue.text = item.condition.displayName
                        }
                        else{
                            binding.tvCondition.text = "-"
                            binding.tvConditionValue.text = "-"
                        }
                        binding.tvLanguage.text = if(item.language!=null) getPokemonLanguageDomainModel(item.language.key).displayName  else "-"
                        binding.tvRow1Colum2.text = if(item.product_group.isNullOrEmpty()) "-" else item.product_group
                        binding.tvHashValue.text = if(item.product_productNumber.isNullOrEmpty()) "-" else item.product_productNumber
                        binding.tvEditionValue.text = if(item.edition.isNullOrEmpty()) "-" else item.edition
                        binding.tvRarity.text = if(item.product_rarity.isNullOrEmpty()) "-" else {
                            val rarity = item.product_rarity?:""
                            rarity.replaceFirstChar{ it.uppercaseChar() } //Capitalize the first character
                        }
                    }
                    ProductCategory.SEALED -> {
                        setSealed(binding)
                        binding.tvSealedSet.text = if(item.product_group.isNullOrEmpty()) "-" else item.product_group
                        binding.tvRarity.text = ProductCategory.SEALED?.title.replaceFirstChar{ it.uppercaseChar() }
                    }
                    ProductCategory.TOYS -> {
                        setToy(binding)
                        binding.tvToyLicense.text = "-"
                        binding.tvToyHash.text = if(item.product_productNumber.isNullOrEmpty()) "-" else item.product_productNumber
                        binding.tvRarity.text = ProductCategory.SEALED?.title.replaceFirstChar{ it.uppercaseChar() }
                        binding.tvRarity.text = if(item.productType?.title.isNullOrEmpty()) "-" else{
                            val category = item.productType?.title?:""
                            category.replaceFirstChar{ it.uppercaseChar() } //Capitalize the first character
                        }
                    }
                }
            }

            binding.tvUserName.text = if(item.collectionDisplayName.isNullOrEmpty()) "-" else item.collectionDisplayName
            binding.tvTitle.text = if(item.product_productName.isNullOrEmpty()) "-" else item.product_productName
            binding.tvPrice.text = if(item.feedType==FeedType.BID) item?.bidPrice?.let { String.format(binding.tvPrice.context.getString(R.string.rate_common),it.toDouble()) } else item?.price?.let { String.format(binding.tvPrice.context.getString(R.string.rate_common),it.toDouble()) }

            item.collection_profileImageURL?.let { binding.ivUser.setProfileImage(it) }

            if(item.frontImageURL.isNullOrEmpty())
                item.product_firImageURL?.let { binding.ivProduct.setProductImage(it) }
            else
                item.frontImageURL.let { binding.ivProduct.setProductImage(it) }

            binding.ivProduct.setOnClickListener {
                listener.goToProductDetail(item)
            }

            binding.ivShare.setOnClickListener {
                listener.share(item)
            }

            binding.btnBuyNow.setOnClickListener{
                listener.buyNow(item)
            }

            binding.ivUser.setOnClickListener {
                listener.goToProfile(item)
            }

            binding.tvUserName.setOnClickListener {
                listener.goToProfile(item)
            }

        }
    }

    inner class ItemLoadMoreViewHolder(val binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == AdapterViewType.VIEW_TYPE_ITEM.value)
            FeedViewHolder(ItemFeedBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        else
            ItemLoadMoreViewHolder(ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItem(position).viewType== AdapterViewType.VIEW_TYPE_ITEM)
            (holder as FeedViewHolder).bind()
    }


    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.value
    }

    private fun setCollection(binding: ItemFeedBinding) {
        binding.tvNewTo.text = binding.tvNewTo.context.getString(R.string.text_feed_new_to_collection)
        binding.tvNewTo.setTextColor(binding.tvNewTo.getResolvedColor(R.color.text_color_dark_green))
        binding.btnBuyNow.gone()
        binding.tvPrice.gone()
    }

    private fun setAsk(binding: ItemFeedBinding) {
        binding.tvNewTo.text = binding.tvNewTo.context.getString(R.string.text_feed_new_ask)
        binding.tvNewTo.setTextColor(binding.tvNewTo.getResolvedColor(R.color.red))
        binding.btnBuyNow.visible()
        binding.tvPrice.visible()
    }

    private fun setBuyList(binding: ItemFeedBinding) {
        binding.tvNewTo.text = binding.tvNewTo.context.getString(R.string.text_feed_new_to_buy_list)
        binding.tvNewTo.setTextColor(binding.tvNewTo.getResolvedColor(R.color.text_color_dark_green))
        binding.btnBuyNow.gone()
        binding.tvPrice.visible()
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

    interface FeedListener {
        fun buyNow(feedDomainModel: FeedDomainModel)
        fun goToProductDetail(feedDomainModel: FeedDomainModel)
        fun share(feedDomainModel: FeedDomainModel)
        fun goToProfile(feedDomainModel: FeedDomainModel)
    }

}