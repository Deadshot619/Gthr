package com.gthr.gthrcollect.ui.askflow.afcardcondition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ItemConfrgurationBinding
import com.gthr.gthrcollect.model.domain.ConditionDomainModel

class ConfigurationConditionAdapter(private val clickListener: IConfigurationConditionListener) :
    ListAdapter<ConditionDomainModel, ConfigurationConditionAdapter.ConfigurationConditionViewModel>(
        DriftUtils
    ) {

    companion object DriftUtils : DiffUtil.ItemCallback<ConditionDomainModel>() {
        override fun areItemsTheSame(oldItem: ConditionDomainModel, newItem: ConditionDomainModel) =
            oldItem.key == newItem.key

        override fun areContentsTheSame(
            oldItem: ConditionDomainModel,
            newItem: ConditionDomainModel
        ) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ConfigurationConditionViewModel(
            ItemConfrgurationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ConfigurationConditionViewModel, position: Int) =
        holder.bind(getItem(position), clickListener)

    inner class ConfigurationConditionViewModel(var binding: ItemConfrgurationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ConditionDomainModel, clickListener: IConfigurationConditionListener) {
            binding.tvEnglish.text = item.displayName
            binding.root.setOnClickListener {
                clickListener.onClick(item)
            }
        }
    }

    interface IConfigurationConditionListener {
        fun onClick(conditionDomainModel: ConditionDomainModel)
    }
}