package com.gthr.gthrcollect.ui.productdetail.recentsell

import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.databinding.RecentSellFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModel
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModelFactory
import com.gthr.gthrcollect.ui.productdetail.adapter.RecentSellAdapter
import com.gthr.gthrcollect.utils.extensions.setProductImage

class RecentSellFragment : BaseFragment<ProductDetailsViewModel, RecentSellFragmentBinding>() {


    override val mViewModel: ProductDetailsViewModel by activityViewModels{
        ProductDetailsViewModelFactory(
            ProductDetailsRepository(),
            DynamicLinkRepository()
        )
    }
    override fun getViewBinding() = RecentSellFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvProduct: AppCompatImageView
    private lateinit var rvRecentSell : RecyclerView

    private val args by navArgs<RecentSellFragmentArgs>()
    private lateinit var recentSaleAdapter : RecentSellAdapter

    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
        setUpRecentSell()
        setUpObserver()
    }

    private fun setUpObserver() {
        mViewModel.mRecentSaleList.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> showProgressBar(false)
                    is State.Success -> {
                        recentSaleAdapter.submitList(it.data)
                        showProgressBar(false)
                    }
                }
            }
        }

        mViewModel.mMtgProductDetails.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        mIvProduct.setProductImage(it.data.firImageURL)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.mFunkoProductDetails.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        mIvProduct.setProductImage(it.data.firImageURL)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.mPokemonProductDetails.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        mIvProduct.setProductImage(it.data.firImageURL)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.mSealedProductDetails.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        mIvProduct.setProductImage(it.data.firImageURL)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.mYugiohProductDetails.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        mIvProduct.setProductImage(it.data.firImageURL)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
    }

    private fun initViews() {
        mViewBinding.let {
            rvRecentSell = it.rvRecentSell
            mIvProduct = it.ivProduct
        }
    }

    private fun setUpRecentSell() {
        recentSaleAdapter = RecentSellAdapter(args.type)
        rvRecentSell.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recentSaleAdapter
        }
    }




}