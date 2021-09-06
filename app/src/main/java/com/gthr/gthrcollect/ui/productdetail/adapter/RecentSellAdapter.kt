package com.gthr.gthrcollect.ui.productdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemRecentSeleBinding
import com.gthr.gthrcollect.databinding.ItemRecentSeleHerderBinding
import com.gthr.gthrcollect.model.domain.RecentSaleDomainModel
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.getResolvedColor
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.helper.*

class RecentSellAdapter(val productType : ProductType) : ListAdapter<RecentSaleDomainModel, RecyclerView.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<RecentSaleDomainModel>() {
        override fun areItemsTheSame(
            oldItem: RecentSaleDomainModel,
            newItem: RecentSaleDomainModel
        ): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(
            oldItem: RecentSaleDomainModel,
            newItem: RecentSaleDomainModel
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



    override fun getItemViewType(position: Int) = position

    inner class RecentSellHerderViewHolder(private val binding: ItemRecentSeleHerderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

        }
    }

    inner class RecentSellViewHolder(private val binding: ItemRecentSeleBinding) : RecyclerView.ViewHolder(binding.root) {

        val mTvLanguage = binding.tvLanguage
        val mTvConditionTitle = binding.tvConditionTitle
        val mTvConditionValue = binding.tvConditionValue
        val mTvEdition = binding.tvEdition

        fun bind(position: Int) {
            val item = getItem(layoutPosition)
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

            when(productType){
                ProductType.MAGIC_THE_GATHERING,ProductType.POKEMON,ProductType.YUGIOH -> {
                    binding.llCustomization.setBackgroundColor(
                        if(position%2==0)
                            binding.root.context.getResolvedColor(R.color.extra_light_blue)
                        else
                            binding.root.context.getResolvedColor(android.R.color.transparent)
                    )
                    if(item.date=="--.--.----"){
                        binding.llCustomization.gone()
                        binding.tvCustomization.visible()
                        binding.tvCustomization.text = "-"
                    }
                    else {
                        binding.llCustomization.visible()
                        binding.tvCustomization.gone()
                    }
                }
                ProductType.FUNKO,ProductType.SEALED_POKEMON,ProductType.SEALED_YUGIOH,ProductType.SEALED_MTG -> {
                    binding.tvCustomization.setBackgroundColor(
                        if(position%2==0)
                            binding.root.context.getResolvedColor(R.color.extra_light_blue)
                        else
                            binding.root.context.getResolvedColor(android.R.color.transparent)
                    )
                    binding.llCustomization.gone()
                    binding.tvCustomization.visible()
                    binding.tvCustomization.text = if(item.date=="--.--.----") "-" else  "New"
                }
            }



            when(productType){
                ProductType.MAGIC_THE_GATHERING,ProductType.POKEMON,ProductType.YUGIOH -> {
                    if(productType==ProductType.MAGIC_THE_GATHERING){
                        mTvLanguage.text =  getMTGLanguage(item.language).abbreviatedName
                    }
                    else if(productType==ProductType.POKEMON){
                        mTvLanguage.text =  getPokemonLanguageDomainModel(item.language).abbreviatedName
                    }
                    else if(productType==ProductType.YUGIOH){
                        mTvLanguage.text =  getYugiohLanguageDomainModel(item.language).abbreviatedName
                    }

                    binding.tvDate.text = item.date
                    binding.tvSalePrice.text = item.price

                    val condition = getConditionFromDisplayName(item.condition)
                    mTvConditionValue.text = condition.displayName
                    mTvConditionTitle.text = binding.root.context?.getConditionTitle(condition.type)
                    mTvEdition.text =  getEditionTypeFromRowType(item.edition).title

                }
                ProductType.FUNKO,ProductType.SEALED_POKEMON,ProductType.SEALED_YUGIOH,ProductType.SEALED_MTG -> {
                    binding.tvDate.text = item.date
                    binding.tvSalePrice.text = item.price
                }
            }


        }
    }


}