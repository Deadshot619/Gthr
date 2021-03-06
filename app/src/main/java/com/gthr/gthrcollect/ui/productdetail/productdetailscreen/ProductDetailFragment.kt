package com.gthr.gthrcollect.ui.productdetail.productdetailscreen

import android.app.Activity
import android.content.Intent
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.databinding.LayoutProductDetailMainDetailsBinding
import com.gthr.gthrcollect.databinding.LayoutProductDetailMtgDetailBinding
import com.gthr.gthrcollect.databinding.ProductDetailFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoActivity
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.ui.homebottomnav.market.AskAdapter
import com.gthr.gthrcollect.ui.productdetail.ProductDetailActivity
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModel
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModelFactory
import com.gthr.gthrcollect.ui.productdetail.adapter.ProductAdapter
import com.gthr.gthrcollect.ui.productdetail.adapter.RecentSellAdapter
import com.gthr.gthrcollect.utils.customviews.CustomLegalityView
import com.gthr.gthrcollect.utils.customviews.CustomProductButton
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.customviews.CustomSeeAllView
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.EditAccountInfoFlow
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.*
import com.gthr.gthrcollect.utils.getProductCategory
import com.gthr.gthrcollect.utils.helper.isUserVerified
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.launch

class ProductDetailFragment : BaseFragment<ProductDetailsViewModel, ProductDetailFragmentBinding>() {

    override val mViewModel: ProductDetailsViewModel by activityViewModels {
        ProductDetailsViewModelFactory(
            ProductDetailsRepository(),
            DynamicLinkRepository(),
            SearchRepository()
        )
    }

    override fun getViewBinding() = ProductDetailFragmentBinding.inflate(layoutInflater)

    private lateinit var rvRecentSell: RecyclerView
    private lateinit var rvUpForSell: RecyclerView
    private lateinit var rvRelated: RecyclerView
    private lateinit var mFlDetails: FrameLayout
    private lateinit var mMcvDescription: MaterialCardView
    private lateinit var mTvDescription: AppCompatTextView
    private lateinit var mIvProduct: AppCompatImageView

    private lateinit var mBtnBuy: CustomProductButton
    private lateinit var mBtnCollect: CustomProductButton
    private lateinit var mBtnSell: CustomProductButton
    private lateinit var mGroupUpForSell: Group
    private lateinit var mGroupRelated: Group

    private lateinit var mRecentSellSeeAll: CustomSeeAllView
    private lateinit var mUpForSellSeeAll: CustomSeeAllView

    private val args by navArgs<ProductDetailFragmentArgs>()
    private lateinit var mProductDisplayModel: ProductDisplayModel
    private lateinit var mProductType: ProductType
    private lateinit var mProductCategory: ProductCategory
    private lateinit var mRelatedAdapter: ProductAdapter
    private lateinit var mUpForSaleAdapter: AskAdapter


    //Mtg Details view
    private lateinit var mLayoutProductDetailMtgDetailBinding: LayoutProductDetailMtgDetailBinding

    //Funko,Pokemon Details view
    private lateinit var mLayoutProductDetailMainDetailsBinding: LayoutProductDetailMainDetailsBinding

    private lateinit var recentSaleAdapter: RecentSellAdapter

    override fun onBinding() {
        mViewBinding.lifecycleOwner = viewLifecycleOwner
        mProductDisplayModel = args.productDisplayModel
        mProductType = mProductDisplayModel.productType!!
        mProductCategory = getProductCategory(mProductType)!!

        setHasOptionsMenu(true)
        initViews()
        setUpOnClickListeners()
        setUpRecentSell()
        setUpRelated()
        setUpUpForSell()
        setUpProductType()

        mViewModel.fetchUpForSale(
            searchTerm = null,
            limit = 20,
            page = 0,
            sortBy = "price",
            isAscending = 1,
            productType = mProductType.title,
            productCategory = getProductCategory(mProductType)?.title,
            objectId = mProductDisplayModel.objectID
        )

        setUpObserver()
    }

    private fun setUpObserver() {
        mViewModel.mRelatedProductList.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> showProgressBar(false)
                    is State.Success -> {
                        GthrLogger.i("dskjvjnkdf", "ProductDisplayModel : ${it.data}")
                        if (it.data.isEmpty()) {
                            mGroupRelated.gone()
                            mGroupUpForSell.gone()
                        } else {
                            mGroupUpForSell.visible()
                            mGroupRelated.visible()
                            mRelatedAdapter.submitList(it.data)
                        }
                        showProgressBar(false)
                    }
                }
            }
        }
        mViewModel.mRecentSaleList.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> showProgressBar(false)
                    is State.Success -> {
                        if (it.data.size < 6)
                            recentSaleAdapter.submitList(it.data)
                        else {
                            val list = it.data.take(5)
                            recentSaleAdapter.submitList(list)
                        }
                        GthrLogger.i("dsbkjsdn", "Recent Sale : ${it.data}")
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
                        GthrLogger.i("dsbkjsdn", "Product hello : ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
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
                        GthrLogger.i("dsbkjsdn", "Product hello : ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
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
                        GthrLogger.i("dsbkjsdn", "Product hello : ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
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
                        GthrLogger.i("dsbkjsdn", "Product hello : ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
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
                        GthrLogger.i("dsbkjsdn", "Product hello : ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }

        mViewModel.upForSaleList.observe(viewLifecycleOwner) {  it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Success -> {
                        if(it.data.size>0){
                            mGroupUpForSell.visible()
                            mUpForSaleAdapter.submitList(it.data.take(10))
                        }
                        else{
                            mGroupUpForSell.gone()
                        }
                        showProgressBar(false)
                    }
                }
            }
        }

    }

    private fun setUpOnClickListeners() {
        mRecentSellSeeAll.setOnClickListener {
            recentSellSeeAll()
        }
        mUpForSellSeeAll.setOnClickListener {
            upForSellSeeAll()
        }
        mBtnBuy.setOnClickListener {
            if (GthrCollect.prefs?.isUserLoggedIn() == true)
                    startActivity(
                        AskFlowActivity.getInstance(
                            requireContext(),
                            AskFlowType.BUY,
                            mProductDisplayModel
                        )
                    )
            else
                startActivity(
                    HomeBottomNavActivity.getInstance(
                        requireContext(),
                        goToProfileSignUp = true
                    )
                )
        }
        mBtnCollect.setOnClickListener {
            if (GthrCollect.prefs?.isUserLoggedIn()==true)
                startActivity(
                    AskFlowActivity.getInstance(
                        requireContext(),
                        AskFlowType.COLLECT,
                        mProductDisplayModel
                    )
                )
            else
                startActivity(
                    HomeBottomNavActivity.getInstance(
                        requireContext(),
                        goToProfileSignUp = true
                    )
                )
        }
        mBtnSell.setOnClickListener {
            if (GthrCollect.prefs?.isUserLoggedIn() == true) {
                lifecycleScope.launch {
                    showProgressBar()
                    activity?.isUserVerified(runEverytime = {
                        showProgressBar(false)
                    }, verified = {
                        startActivity(
                            AskFlowActivity.getInstance(
                                requireContext(),
                                AskFlowType.SELL,
                                mProductDisplayModel
                            )
                        )
                    }, notVerified = {
                        startActivityForResult(
                            EditAccountInfoActivity.getInstance(
                                requireContext(),
                                EditAccountInfoFlow.GOV_ID
                            ), REQUEST_CODE_ID_VERIFICATION_SELL
                        )
                    })
                }
            } else {
                startActivity(
                    HomeBottomNavActivity.getInstance(
                        requireContext(),
                        goToProfileSignUp = true
                    )
                )
            }

        }
    }

    private fun upForSellSeeAll() {
        val action =
            ProductDetailFragmentDirections.actionProductDetailFragmentToUpForSellFragment(
                mProductType
            )
        findNavController().navigate(action)
    }

    private fun recentSellSeeAll() {
        val action =
            ProductDetailFragmentDirections.actionProductDetailFragmentToRecentSellFragment(
                mProductType
            )
        findNavController().navigate(action)
    }

    private fun setUpProductType() {
        when (mProductType) {
            ProductType.POKEMON -> setUpPokemon()
            ProductType.MAGIC_THE_GATHERING -> setUpMGT()
            ProductType.YUGIOH -> seUpYugioh()
            ProductType.SEALED_POKEMON, ProductType.SEALED_MTG, ProductType.SEALED_YUGIOH -> setUpSealed()
            ProductType.FUNKO -> setUpFunko()
        }
    }

    private fun setUpFunko() {
        mLayoutProductDetailMainDetailsBinding =
            LayoutProductDetailMainDetailsBinding.inflate(layoutInflater)
        mFlDetails.addView(mLayoutProductDetailMainDetailsBinding.root)

        mLayoutProductDetailMainDetailsBinding.tvRow1Column1.text =
            getString(R.string.text_release_date_product_detail)
        mLayoutProductDetailMainDetailsBinding.tvRow1Column2.text =
            getString(R.string.text_category_product_detail)
        mLayoutProductDetailMainDetailsBinding.tvRow3Column1.text =
            getString(R.string.text_item_number_product_dtail)
        mLayoutProductDetailMainDetailsBinding.tvRow3Column2.text =
            getString(R.string.text_product_type_product_detail)
        mLayoutProductDetailMainDetailsBinding.tvRow5Column1.text =
            getString(R.string.text_exclusivity_product_detail)
        mLayoutProductDetailMainDetailsBinding.tvRow5Column2.text =
            getString(R.string.text_license_product_detail)

        mLayoutProductDetailMainDetailsBinding.groupYugioh.gone()

        mMcvDescription.gone()
    }

    private fun setUpSealed() {
        mMcvDescription.visible()
    }

    private fun seUpYugioh() {
        mLayoutProductDetailMainDetailsBinding =
            LayoutProductDetailMainDetailsBinding.inflate(layoutInflater)
        mFlDetails.addView(mLayoutProductDetailMainDetailsBinding.root)

        mLayoutProductDetailMainDetailsBinding.run {
            tvRow1Column1.text = getString(R.string.text_number_product_detail)
            tvRow1Column2.text = getString(R.string.text_type_product_detail)
            tvRow3Column1.text = getString(R.string.text_set_product_details)
            tvRow3Column2.text = getString(R.string.text_stats_product_details)
            tvRow5Column1.text = getString(R.string.text_rarity_product_details)
            tvRow5Column2.text = ""

            groupYugioh.gone()
        }
        mMcvDescription.visible()
    }

    private fun setUpMGT() {
        mLayoutProductDetailMtgDetailBinding =
            LayoutProductDetailMtgDetailBinding.inflate(layoutInflater)
        mFlDetails.addView(mLayoutProductDetailMtgDetailBinding.root)

        mMcvDescription.gone()
    }

    private fun setUpPokemon() {
        mLayoutProductDetailMainDetailsBinding =
            LayoutProductDetailMainDetailsBinding.inflate(layoutInflater)
        mLayoutProductDetailMainDetailsBinding.run {
            mFlDetails.addView(root)
            tvRow1Column1.text = getString(R.string.text_number_product_detail)
            tvRow1Column2.text = getString(R.string.text_type_product_detail)
            tvRow3Column1.text = getString(R.string.text_set_product_details)
            tvRow3Column2.text = getString(R.string.text_hp_product_details)
            tvRow5Column1.text = getString(R.string.text_rarity_product_details)
            tvRow5Column2.text = getString(R.string.text_stage_product_details)
            tvRow7Column1.text = getString(R.string.text_jap_number_product_detail)
            tvRow7Column2.text = getString(R.string.text_jap_set_product_detail)
        }

        mMcvDescription.gone()
    }

    private fun setUpRelated() {
        mRelatedAdapter = ProductAdapter(mProductType, CustomProductCell.State.NORMAL) {
            startActivity(ProductDetailActivity.getInstance(requireContext(), it.objectID!!,it.productType!!))
        }

        rvRelated.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = mRelatedAdapter
        }
    }

    private fun setUpUpForSell() {
        rvUpForSell.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            mUpForSaleAdapter = AskAdapter(CustomProductCell.State.FOR_SALE) {
                startActivity(
                    AskFlowActivity.getInstance(
                        requireContext(),
                        AskFlowType.BUY_DIRECTLY_FROM_SOMEONE,
                        it
                    )
                )
            }

            rvUpForSell.adapter = mUpForSaleAdapter
        }
    }

    private fun setUpRecentSell() {
        recentSaleAdapter = RecentSellAdapter(mProductType)
        rvRecentSell.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recentSaleAdapter
        }
    }

    private fun initViews() {
        mViewBinding.let {
            rvRecentSell = it.rvRecentSell
            rvUpForSell = it.rvUpForSell
            rvRelated = it.rvRelated
            mFlDetails = it.flDetail
            mMcvDescription = it.cvCardDescription
            mTvDescription = it.tvDescription
            mRecentSellSeeAll = it.csavRecentSalesSeeAll
            mUpForSellSeeAll = it.csavUpForSalesSeeAll
            mBtnBuy = it.btnBuy
            mBtnCollect = it.btnCollect
            mBtnSell = it.btnSell
            mIvProduct = it.ivProduct
            mGroupUpForSell = it.groupUpForSell
            mGroupRelated = it.groupRelated
            initProgressBar(it.layoutProgress)
        }
    }

    private fun setViewData(data: SealedDomainModel) {
        mIvProduct.setProductImage(data.firImageURL)
        mTvDescription.text = data.description
    }

    private fun setViewData(data: MTGDomainModel) {
        mIvProduct.setProductImage(data.imageUris)
        mLayoutProductDetailMtgDetailBinding.run {
            clvStandard.setType(if (data.standard) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)
            clvBrawl.setType(if (data.brawl) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)
            clvPioneer.setType(if (data.pioneer) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)
            clvModern.setType(if (data.modern) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)
            clvPauper.setType(if (data.pauper) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)
            clvLegacy.setType(if (data.legacy) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)
            clvPenny.setType(if (data.penny) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)
            clvCommander.setType(if (data.commander) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)
            clvVintage.setType(if (data.vintage) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)
            clvHistoric.setType(if (data.historic) CustomLegalityView.Type.LEGAL else CustomLegalityView.Type.NOT_LEGAL)

            tvTitle.text = data.typeLine
            tvTextLine1.text = data.flavorText
            tvOutOf.text = (data.power/*/data.toughness*/).toString()
        }
    }

    private fun setViewData(data: YugiohDomainModel) {
        mIvProduct.setProductImage(data.firImageURL)
        mTvDescription.text = data.firstDescription
        mLayoutProductDetailMainDetailsBinding.run {
            tvRow2Column1.text = data.number
            tvRow2Column2.text = data.cardType
            tvRow4Column1.text = data.set
            tvRow4Column2.text = data.stats
            tvRow6Column1.text = data.rarity
            tvRow6Column2.text = ""
        }
    }

    private fun setViewData(data: PokemonDomainModel) {
        mIvProduct.setProductImage(data.firImageURL)
        mLayoutProductDetailMainDetailsBinding.run {
            tvRow2Column1.text = data.number
            tvRow2Column2.text = data.cardType
            tvRow4Column1.text = data.set
            tvRow4Column2.text = data.hp
            tvRow6Column1.text = data.rarity
            tvRow6Column2.text = data.stage
            tvRow8Column1.text = data.japaneseNumber
            tvRow8Column2.text = data.japaneseSet
        }
    }

    private fun setViewData(data: FunkoDomainModel) {
        mIvProduct.setProductImage(data.firImageURL)
        mLayoutProductDetailMainDetailsBinding.run {
            tvRow2Column1.text = data.releaseDate
            tvRow2Column2.text = data.category

            tvRow4Column2.text = data.productType.toString()
            tvRow4Column1.text = data.itemNumber

            tvRow6Column2.text = data.license
            tvRow6Column1.text = data.exclusivity
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ID_VERIFICATION_SELL)
                showToast(getString(R.string.text_id_under_review))
        }
    }

    companion object {
        private const val REQUEST_CODE_ID_VERIFICATION_SELL = 420
    }
}