package com.gthr.gthrcollect.ui.profile.reciepts.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemReceiptBinding
import com.gthr.gthrcollect.model.domain.ReceiptDisplayModel
import com.gthr.gthrcollect.utils.enums.ConditionType
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.setProductImage
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.helper.*
import com.gthr.gthrcollect.utils.logger.GthrLogger

class ReceiptAdapter(val callback : ReceiptListener) : ListAdapter<ReceiptDisplayModel, ReceiptAdapter.ReceiptViewHolder>(
    DiffCallback
) {

    companion object DiffCallback : DiffUtil.ItemCallback<ReceiptDisplayModel>() {
        override fun areItemsTheSame(oldItem: ReceiptDisplayModel, newItem: ReceiptDisplayModel): Boolean {
            return oldItem.receiptDomainModel.refKey == newItem.receiptDomainModel.refKey
        }
        override fun areContentsTheSame(oldItem: ReceiptDisplayModel, newItem: ReceiptDisplayModel): Boolean {
            return oldItem == newItem
        }
    }

    inner class  ReceiptViewHolder(var binding: ItemReceiptBinding) : RecyclerView.ViewHolder(binding.root) {

        private val mTvLanguage = binding.tvLanguage
        private val mTvConditionTitle = binding.tvCondition
        private val mTvConditionValue = binding.tvConditionValue
        private val mTvEdition = binding.tvEditionValue

        fun bind(callback: ReceiptListener, pos: Int) {
            val item = getItem(layoutPosition)

            binding.root.setOnClickListener {
                callback.onClick(item, pos)
            }

            binding.tvTitle.text = item.productDisplayModel.name
            binding.tvDescription.text = item.productDisplayModel.description
            binding.tvEditionNo.text = item.productDisplayModel.productNumber
            binding.tvRow1Colum2.text = item.productDisplayModel.rarity
            binding.cdbPrice.text =  String.format(binding.root.context.getString(R.string.rate_common),item.saleHistoryDomainModel.price)

            item.productDisplayModel.firImageURL?.let {
                binding.ivProduct.setProductImage(it)
            }




            when(item.productDisplayModel.productType){
                ProductType.MAGIC_THE_GATHERING, ProductType.POKEMON, ProductType.YUGIOH -> {
                    mTvLanguage.text = item.receiptDomainModel.lang?.abbreviatedName
                    mTvEdition.text = item.receiptDomainModel.edition?.title
                    GthrLogger.i("dbjdbf","${item.receiptDomainModel.condition}")
                    val condition = item.receiptDomainModel.condition
                    mTvConditionValue.text = condition?.abbreviatedName
                    mTvConditionTitle.text = binding.root.context?.getConditionTitle(condition?.type?:ConditionType.NEW)

                }
                ProductType.FUNKO, ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                    binding.clCustomization.gone()
                    binding.tvCustomization.visible()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder =
        ReceiptViewHolder(
            ItemReceiptBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder:  ReceiptViewHolder, position: Int) =
        holder.bind(callback, position)

    interface ReceiptListener {
        fun onClick(receiptDomainModel: ReceiptDisplayModel?, pos: Int)
    }
}