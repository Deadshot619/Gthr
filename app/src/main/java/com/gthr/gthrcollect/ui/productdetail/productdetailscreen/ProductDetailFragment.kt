package com.gthr.gthrcollect.ui.productdetail.productdetailscreen


import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.*
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModel
import com.gthr.gthrcollect.ui.productdetail.adapter.ProductAdapter
import com.gthr.gthrcollect.ui.productdetail.adapter.RecentSellAdapter
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible


class ProductDetailFragment : BaseFragment<ProductDetailsViewModel, ProductDetailFragmentBinding>() {

    override val mViewModel: ProductDetailsViewModel by viewModels()
    override fun getViewBinding() = ProductDetailFragmentBinding.inflate(layoutInflater)

    private lateinit var rvRecentSell : RecyclerView
    private lateinit var rvUpForSell : RecyclerView
    private lateinit var rvRelated : RecyclerView
    private lateinit var mFlTop : FrameLayout
    private lateinit var mFlDetails : FrameLayout
    private lateinit var mMcvDescription : MaterialCardView

    private val args by navArgs<ProductDetailFragmentArgs>()

    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
        setUpRecentSell()
        setUpUpForSell()
        setUpRelated()
        setUpProductType()
    }

    private fun setUpProductType() {
        when (args.type) {
            ProductType.POKEMON -> setUpPokemon()
            ProductType.MTG -> setUpMGT()
            ProductType.YUGIOH -> seUpYugioh()
        }
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
        detailView.tvRow7Column1.text = getString(R.string.text_jap_number_product_detail)
        detailView.tvRow7Column2.text = getString(R.string.text_jap_set_product_detail)

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
            mFlTop = it.flTop
            mFlDetails = it.flDetail
            mMcvDescription = it.cvCardDescription
        }
    }

}