package com.gthr.gthrcollect.ui.productdetail.upforsell

import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.databinding.LayoutProductDetailCardTopBinding
import com.gthr.gthrcollect.databinding.LayoutProductDetailToyTopBinding
import com.gthr.gthrcollect.databinding.UpForSellFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModel
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModelFactory
import com.gthr.gthrcollect.ui.productdetail.adapter.UpForSellAdapter
import com.gthr.gthrcollect.ui.productdetail.recentsell.RecentSellFragmentArgs
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class UpForSellFragment : BaseFragment<ProductDetailsViewModel,UpForSellFragmentBinding>() {

    override val mViewModel: ProductDetailsViewModel by activityViewModels{
        ProductDetailsViewModelFactory(
            ProductDetailsRepository()
        )
    }
    override fun getViewBinding() =  UpForSellFragmentBinding.inflate(layoutInflater)

    private lateinit var rvUpForSell : RecyclerView
    private val args by navArgs<RecentSellFragmentArgs>()

    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
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
            rvUpForSell = it.rvUpForSell
        }
    }


}