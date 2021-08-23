package com.gthr.gthrcollect.ui.askflow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemConfrgurationBinding

class ConfigurationAdapter(val callback : () -> Unit ) : ListAdapter<String, ConfigurationAdapter.ConfigurationViewModel>(DriftUtils) {

    companion object DriftUtils : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }

    inner class ConfigurationViewModel(var binding: ItemConfrgurationBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(){
            binding.tvEnglish.text = getItem(layoutPosition)
            binding.root.setOnClickListener {
                callback()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ConfigurationViewModel(ItemConfrgurationBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ConfigurationViewModel, position: Int) = holder.bind()
}