package com.gthr.gthrcollect.ui.productdetail.productdetailscreen


import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.databinding.*
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModel
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModelFactory
import com.gthr.gthrcollect.ui.productdetail.adapter.ProductAdapter
import com.gthr.gthrcollect.ui.productdetail.adapter.RecentSellAdapter
import com.gthr.gthrcollect.ui.settings.SettingsActivity
import com.gthr.gthrcollect.utils.customviews.CustomProductButton
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.customviews.CustomSeeAllView
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.enums.SettingFlowType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.getProductCategory


class ProductDetailFragment : BaseFragment<ProductDetailsViewModel, ProductDetailFragmentBinding>() {

    override val mViewModel: ProductDetailsViewModel by activityViewModels{
        ProductDetailsViewModelFactory(
            ProductDetailsRepository()
        )
    }
    override fun getViewBinding() = ProductDetailFragmentBinding.inflate(layoutInflater)

    private lateinit var rvRecentSell : RecyclerView
    private lateinit var rvUpForSell : RecyclerView
    private lateinit var rvRelated : RecyclerView
    private lateinit var mFlTop : FrameLayout
    private lateinit var mFlDetails : FrameLayout
    private lateinit var mMcvDescription: MaterialCardView

    private lateinit var mBtnBuy: CustomProductButton
    private lateinit var mBtnCollect: CustomProductButton
    private lateinit var mBtnSell: CustomProductButton

    private lateinit var mRecentSellSeeAll: CustomSeeAllView
    private lateinit var mUpForSellSeeAll: CustomSeeAllView

    private val args by navArgs<ProductDetailFragmentArgs>()
    private lateinit var mProductType: ProductType
    private lateinit var mProductCategory: ProductCategory

    override fun onBinding() {
        mProductType = args.type
        mProductCategory = getProductCategory(mProductType)!!

        setHasOptionsMenu(true)
        initViews()
        setUpOnClickListeners()
        setUpRecentSell()
        setUpUpForSell()
        setUpRelated()
        setUpProductType()
    }

    private fun setUpOnClickListeners() {
        mRecentSellSeeAll.setOnClickListener {
            recentSellSeeAll()
        }
        mUpForSellSeeAll.setOnClickListener {
            upForSellSeeAll()
        }
        mBtnBuy.setOnClickListener {
            startActivity(
                AskFlowActivity.getInstance(
                    requireContext(),
                    AskFlowType.BUY,
                    mProductCategory
                )
            )
        }
        mBtnCollect.setOnClickListener {
            startActivity(
                AskFlowActivity.getInstance(
                    requireContext(),
                    AskFlowType.COLLECT,
                    mProductCategory
                )
            )
        }
        mBtnSell.setOnClickListener {
            startActivity(
                AskFlowActivity.getInstance(
                    requireContext(),
                    AskFlowType.SELL,
                    mProductCategory
                )
            )
        }
    }

    private fun upForSellSeeAll() {
        val action = ProductDetailFragmentDirections.actionProductDetailFragmentToUpForSellFragment(args.type)
        findNavController().navigate(action)
    }

    private fun recentSellSeeAll() {
        val action = ProductDetailFragmentDirections.actionProductDetailFragmentToRecentSellFragment(args.type)
        findNavController().navigate(action)
    }

    private fun setUpProductType() {
        when (args.type) {
            ProductType.POKEMON -> setUpPokemon()
            ProductType.MAGIC_THE_GATHERING -> setUpMGT()
            ProductType.YUGIOH -> seUpYugioh()
            ProductType.SEALED_POKEMON, ProductType.SEALED_MTG, ProductType.SEALED_YUGIOH -> setUpSealed()
            ProductType.FUNKO -> setUpFunko()
        }
    }

    private fun setUpFunko() {
        val topView = LayoutProductDetailToyTopBinding.inflate(layoutInflater)
        mFlTop.addView(topView.root)

        val detailView = LayoutProductDetailMainDetailsBinding.inflate(layoutInflater)
        mFlDetails.addView(detailView.root)

        detailView.tvRow1Column1.text = getString(R.string.text_release_date_product_detail)
        detailView.tvRow1Column2.text = getString(R.string.text_category_product_detail)
        detailView.tvRow3Column1.text = getString(R.string.text_item_number_product_dtail)
        detailView.tvRow3Column2.text = getString(R.string.text_product_type_product_detail)
        detailView.tvRow5Column1.text = getString(R.string.text_exclusivity_product_detail)
        detailView.tvRow5Column2.text = getString(R.string.text_license_product_detail)

        detailView.groupYugioh.gone()

        mMcvDescription.gone()
    }

    private fun setUpSealed() {
        val topView = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(topView.root)

        topView.llRow2.gone()
        mMcvDescription.visible()
    }

    private fun seUpYugioh() {
        val topView = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(topView.root)

        val detailView = LayoutProductDetailMainDetailsBinding.inflate(layoutInflater)
        mFlDetails.addView(detailView.root)

        detailView.tvRow1Column1.text = getString(R.string.text_number_product_detail)
        detailView.tvRow1Column2.text = getString(R.string.text_type_product_detail)
        detailView.tvRow3Column1.text = getString(R.string.text_set_product_details)
        detailView.tvRow3Column2.text = getString(R.string.text_stats_product_details)
        detailView.tvRow5Column1.text = getString(R.string.text_rarity_product_details)
        detailView.tvRow5Column2.text = ""
        detailView.tvRow6Column2.text = ""

        detailView.groupYugioh.gone()

        mMcvDescription.visible()
    }

    private fun setUpMGT() {
        val topView = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(topView.root)

        val detailView = LayoutProductDetailMtgDetailBinding.inflate(layoutInflater)
        mFlDetails.addView(detailView.root)

        mMcvDescription.gone()
    }

    private fun setUpPokemon() {
        val topView = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(topView.root)

        val detailView = LayoutProductDetailMainDetailsBinding.inflate(layoutInflater)
        mFlDetails.addView(detailView.root)

        detailView.tvRow1Column1.text = getString(R.string.text_number_product_detail)
        detailView.tvRow1Column2.text = getString(R.string.text_type_product_detail)
        detailView.tvRow3Column1.text = getString(R.string.text_set_product_details)
        detailView.tvRow3Column2.text = getString(R.string.text_hp_product_details)
        detailView.tvRow5Column1.text = getString(R.string.text_rarity_product_details)
        detailView.tvRow5Column2.text = getString(R.string.text_stage_product_details)
        detailView.tvRow7Column1.text = getString(R.string.text_jap_number_product_detail)
        detailView.tvRow7Column2.text = getString(R.string.text_jap_set_product_detail)

        mMcvDescription.gone()
    }

    private fun setUpRelated() {
        rvRelated.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = ProductAdapter(args.type,CustomProductCell.State.NORMAL)
        }
    }

    private fun setUpUpForSell() {
        rvUpForSell.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = ProductAdapter(args.type,CustomProductCell.State.FOR_SALE)
        }
    }

    private fun setUpRecentSell() {
        rvRecentSell.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RecentSellAdapter(5)
        }
    }

    private fun initViews() {
        mViewBinding.let {
            rvRecentSell = it.rvRecentSell
            rvUpForSell = it.rvUpForSell
            rvRelated = it.rvRelated
            mFlTop = it.flTop
            mFlDetails = it.flDetail
            mMcvDescription = it.cvCardDescription
            mRecentSellSeeAll = it.csavRecentSalesSeeAll
            mUpForSellSeeAll = it.csavUpForSalesSeeAll

            mBtnBuy = it.btnBuy
            mBtnCollect = it.btnCollect
            mBtnSell = it.btnSell
        }
    }
}