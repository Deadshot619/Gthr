package com.gthr.gthrcollect.ui.productdetail.upforsell

import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.LayoutProductDetailCardTopBinding
import com.gthr.gthrcollect.databinding.LayoutProductDetailToyTopBinding
import com.gthr.gthrcollect.databinding.UpForSellFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.recentsell.RecentSellFragmentArgs
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class UpForSellFragment : BaseFragment<UpForSellViewModel,UpForSellFragmentBinding>() {

    override val mViewModel: UpForSellViewModel by viewModels()
    override fun getViewBinding() =  UpForSellFragmentBinding.inflate(layoutInflater)

    private lateinit var mFlTop : FrameLayout
    private lateinit var rvUpForSell : RecyclerView

    private val args by navArgs<RecentSellFragmentArgs>()

    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
        setUpProductType()
        setUpUpForSell()
    }

    private fun setUpUpForSell() {
        rvUpForSell.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = UpForSellAdapter(args.type){}
        }
    }

    private fun initViews() {
        mViewBinding.let {
            mFlTop = it.flTop
            rvUpForSell = it.rvUpForSell
        }
    }

    private fun setUpProductType() {
        when (args.type) {
            ProductType.POKEMON -> setUpCardTopView(true)
            ProductType.MAGIC_THE_GATHERING -> setUpCardTopView(true)
            ProductType.YUGIOH -> setUpCardTopView(true)
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> setUpCardTopView(
                false
            )
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

}