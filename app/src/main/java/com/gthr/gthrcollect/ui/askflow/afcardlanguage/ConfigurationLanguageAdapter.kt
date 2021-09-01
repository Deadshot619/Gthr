package com.gthr.gthrcollect.ui.askflow.afcardlanguage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemConfrgurationBinding
import com.gthr.gthrcollect.model.domain.LanguageDomainModel

class ConfigurationLanguageAdapter(private val clickListener: IConfigurationLanguageListener) :
    ListAdapter<LanguageDomainModel, ConfigurationLanguageAdapter.ConfigurationLanguageViewModel>(
        DriftUtils
    ) {

    companion object DriftUtils : DiffUtil.ItemCallback<LanguageDomainModel>() {
        override fun areItemsTheSame(oldItem: LanguageDomainModel, newItem: LanguageDomainModel) =
            oldItem.key == newItem.key

        override fun areContentsTheSame(
            oldItem: LanguageDomainModel,
            newItem: LanguageDomainModel
        ) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ConfigurationLanguageViewModel(
            ItemConfrgurationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ConfigurationLanguageViewModel, position: Int) =
        holder.bind(getItem(position), clickListener)

    inner class ConfigurationLanguageViewModel(var binding: ItemConfrgurationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageDomainModel, clickListener: IConfigurationLanguageListener) {
            binding.tvEnglish.text = item.displayName
            binding.root.setOnClickListener {
                clickListener.onClick(item)
            }
        }
    }

    interface IConfigurationLanguageListener {
        fun onClick(languageDomainModel: LanguageDomainModel)
    }
}