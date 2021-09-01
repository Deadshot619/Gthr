package com.gthr.gthrcollect.ui.askflow.afcardedition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemConfrgurationBinding
import com.gthr.gthrcollect.utils.enums.EditionType

class ConfigurationEditionAdapter(private val clickListener: IConfigurationEditionListener) :
    ListAdapter<EditionType, ConfigurationEditionAdapter.ConfigurationEditionViewModel>(DriftUtils) {

    companion object DriftUtils : DiffUtil.ItemCallback<EditionType>() {
        override fun areItemsTheSame(oldItem: EditionType, newItem: EditionType) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: EditionType, newItem: EditionType) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ConfigurationEditionViewModel(
            ItemConfrgurationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ConfigurationEditionViewModel, position: Int) =
        holder.bind(getItem(position), clickListener)

    inner class ConfigurationEditionViewModel(var binding: ItemConfrgurationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EditionType, clickListener: IConfigurationEditionListener) {
            binding.tvEnglish.text = item.title
            binding.root.setOnClickListener {
                clickListener.onClick(item)
            }
        }
    }

    interface IConfigurationEditionListener {
        fun onClick(editionType: EditionType)
    }
}