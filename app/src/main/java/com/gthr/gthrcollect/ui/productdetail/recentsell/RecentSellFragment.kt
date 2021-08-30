package com.gthr.gthrcollect.ui.productdetail.recentsell

import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.databinding.LayoutProductDetailCardTopBinding
import com.gthr.gthrcollect.databinding.LayoutProductDetailToyTopBinding
import com.gthr.gthrcollect.databinding.RecentSellFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModel
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModelFactory
import com.gthr.gthrcollect.ui.productdetail.adapter.RecentSellAdapter
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class RecentSellFragment : BaseFragment<ProductDetailsViewModel, RecentSellFragmentBinding>() {


    override val mViewModel: ProductDetailsViewModel by viewModels{
        ProductDetailsViewModelFactory(
            ProductDetailsRepository()
        )
    }
    override fun getViewBinding() = RecentSellFragmentBinding.inflate(layoutInflater)

    private lateinit var mFlTop : FrameLayout
    private lateinit var rvRecentSell : RecyclerView

    private val args by navArgs<RecentSellFragmentArgs>()

    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
        setUpRecentSell()
        setUpProductType()
    }

    private fun initViews() {
        mViewBinding.let {
            mFlTop = it.flTop
            rvRecentSell = it.rvRecentSell
        }
    }

    private fun setUpProductType() {
        when (args.type) {
            ProductType.POKEMON -> setUpCardTopView(true)
            ProductType.MAGIC_THE_GATHERING -> setUpCardTopView(true)
            ProductType.YUGIOH -> setUpCardTopView(true)
            ProductType.SEALED_YUGIOH -> setUpCardTopView(false)
            ProductType.FUNKO -> setUpFunko()
        }
    }

    private fun setUpFunko() {
        val topView = LayoutProductDetailToyTopBinding.inflate(layoutInflater)
        mFlTop.addView(topView.root)
    }

    private fun setUpCardTopView(row2Visibility : Boolean) {
        val topView = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(topView.root)
        if(row2Visibility)
            topView.llRow2.visible()
        else
            topView.llRow2.gone()
    }

    private fun setUpRecentSell() {
        rvRecentSell.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RecentSellAdapter(15)
        }
    }




}