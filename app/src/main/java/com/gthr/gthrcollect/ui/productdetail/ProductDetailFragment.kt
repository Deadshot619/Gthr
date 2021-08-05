package com.gthr.gthrcollect.ui.productdetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ProductDetailFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.adapter.ProductAdapter
import com.gthr.gthrcollect.ui.productdetail.adapter.RecentSellAdapter
import com.gthr.gthrcollect.utils.customviews.CustomProductCell

class ProductDetailFragment : BaseFragment<ProductDetailsViewModel, ProductDetailFragmentBinding>() {

    override val mViewModel: ProductDetailsViewModel by viewModels()
    override fun getViewBinding() = ProductDetailFragmentBinding.inflate(layoutInflater)

    private lateinit var rvRecentSell : RecyclerView
    private lateinit var rvUpForSell : RecyclerView
    private lateinit var rvRelated : RecyclerView

    override fun onBinding() {
        initViews()
        setUpRecentSell()
        setUpUpForSell()
        setUpRelated()
    }

    private fun setUpRelated() {
        rvRelated.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = ProductAdapter(CustomProductCell.State.NORMAL)
        }
    }

    private fun setUpUpForSell() {
        rvUpForSell.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = ProductAdapter(CustomProductCell.State.FOR_SALE)
        }
    }

    private fun setUpRecentSell() {
        rvRecentSell.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RecentSellAdapter()
        }
    }

    private fun initViews() {
        mViewBinding.let {
            rvRecentSell = it.rvRecentSell
            rvUpForSell = it.rvUpForSell
            rvRelated = it.rvRelated
        }
    }

}