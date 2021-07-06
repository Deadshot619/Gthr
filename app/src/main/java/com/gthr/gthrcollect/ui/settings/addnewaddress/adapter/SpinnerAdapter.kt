package com.gthr.gthrcollect.ui.settings.addnewaddress.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ItemSpinnerBinding


class SpinnerAdapter(val list : ArrayList<String>) : BaseAdapter() {
    override fun getCount(): Int {
       return list.size
    }

    override fun getItem(position: Int): Any {
       return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: SpinnerViewHolder

        if (convertView == null) {
            val itemBinding: ItemSpinnerBinding = ItemSpinnerBinding.inflate(
                LayoutInflater.from(parent!!.context),
                parent,
                false
            )
            holder = SpinnerViewHolder(itemBinding)
            holder.view = itemBinding.root
            holder.view.tag = holder
        } else {
            holder = convertView.tag as SpinnerViewHolder
        }
        holder.binding.tvMain.text = list[position]


        return holder.view
    }

    private class SpinnerViewHolder internal constructor(val binding: ItemSpinnerBinding) {
        var view: View = binding.root
    }


}