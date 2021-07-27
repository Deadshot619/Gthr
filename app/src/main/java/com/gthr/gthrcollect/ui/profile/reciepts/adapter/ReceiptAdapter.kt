package com.gthr.gthrcollect.ui.profile.reciepts.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemReceiptBinding
import com.gthr.gthrcollect.model.domain.ReceiptDomainModel
import com.gthr.gthrcollect.model.domain.SearchCollection

class ReceiptAdapter(val callback : (SearchCollection) -> Unit) : ListAdapter<ReceiptDomainModel, ReceiptAdapter.ReceiptViewHolder>(
    DiffCallback
) {

    companion object DiffCallback : DiffUtil.ItemCallback<ReceiptDomainModel>() {
        override fun areItemsTheSame(oldItem: ReceiptDomainModel, newItem: ReceiptDomainModel): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ReceiptDomainModel, newItem: ReceiptDomainModel): Boolean {
            return oldItem == newItem
        }
    }

    inner class  ReceiptViewHolder(var binding: ItemReceiptBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {

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
        holder.bind()

    override fun getItemCount(): Int = 10

}