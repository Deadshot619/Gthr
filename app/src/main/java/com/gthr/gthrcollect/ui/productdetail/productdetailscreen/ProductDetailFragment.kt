package com.gthr.gthrcollect.ui.productdetail.productdetailscreen


import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.databinding.*
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
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
import com.gthr.gthrcollect.utils.logger.GthrLogger


class ProductDetailFragment :
    BaseFragment<ProductDetailsViewModel, ProductDetailFragmentBinding>() {


    override val mViewModel: ProductDetailsViewModel by activityViewModels {
        ProductDetailsViewModelFactory(
            ProductDetailsRepository()
        )
    }

    override fun getViewBinding() = ProductDetailFragmentBinding.inflate(layoutInflater)

    private lateinit var rvRecentSell: RecyclerView
    private lateinit var rvUpForSell: RecyclerView
    private lateinit var rvRelated: RecyclerView
    private lateinit var mFlTop: FrameLayout
    private lateinit var mFlDetails: FrameLayout
    private lateinit var mMcvDescription: MaterialCardView
    private lateinit var mTvDescription: AppCompatTextView
    private lateinit var mIvProduct: AppCompatImageView

    private lateinit var mBtnBuy: CustomProductButton
    private lateinit var mBtnCollect: CustomProductButton
    private lateinit var mBtnSell: CustomProductButton

    private lateinit var mRecentSellSeeAll: CustomSeeAllView
    private lateinit var mUpForSellSeeAll: CustomSeeAllView

    private val args by navArgs<ProductDetailFragmentArgs>()
    private lateinit var mProductType: ProductType
    private lateinit var mProductCategory: ProductCategory

    //Top View for pokemon, mtg, Sealed, Yugioh
    private lateinit var mLayoutProductDetailCardTopBinding: LayoutProductDetailCardTopBinding
    //Top View for funko
    private lateinit var mLayoutProductDetailToyTopBinding: LayoutProductDetailToyTopBinding


    //Mtg Details view
    private lateinit var mLayoutProductDetailMtgDetailBinding: LayoutProductDetailMtgDetailBinding
    //Funko,Pokemon Details view
    private lateinit var mLayoutProductDetailMainDetailsBinding: LayoutProductDetailMainDetailsBinding



    override fun onBinding() {
        mViewBinding.lifecycleOwner = viewLifecycleOwner
        mProductType = args.type
        mProductCategory = getProductCategory(mProductType)!!

        setHasOptionsMenu(true)
        initViews()
        setUpOnClickListeners()
        setUpRecentSell()
        setUpUpForSell()
        setUpRelated()
        setUpProductType()
        setUpObserver()
    }

    private fun setUpObserver() {
        mViewModel.mtgProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.funkoProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.pokemonProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.sealedProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.yugiohProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
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
        val action =
            ProductDetailFragmentDirections.actionProductDetailFragmentToUpForSellFragment(args.type)
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
        when (args.type) {
            ProductType.POKEMON -> mViewModel.getProductDetails("-MiTOfdj0XCDttwhYf-Q", ProductType.POKEMON)
            ProductType.MAGIC_THE_GATHERING -> mViewModel.getProductDetails("-MiTNOMShh5j-097pdyz", ProductType.MAGIC_THE_GATHERING)
            ProductType.YUGIOH -> mViewModel.getProductDetails("-MiTSTt3dbVfOQYDmswu", ProductType.YUGIOH)
            ProductType.SEALED_POKEMON, ProductType.SEALED_MTG, ProductType.SEALED_YUGIOH -> mViewModel.getProductDetails("0", ProductType.SEALED_POKEMON)
            ProductType.FUNKO -> mViewModel.getProductDetails("-MiTpkeK3aeS5L4lvUO0", ProductType.FUNKO)
        }
    }

    private fun setUpFunko() {
        mLayoutProductDetailToyTopBinding = LayoutProductDetailToyTopBinding.inflate(layoutInflater)
        mFlTop.addView(mLayoutProductDetailToyTopBinding.root)

        mLayoutProductDetailMainDetailsBinding =
            LayoutProductDetailMainDetailsBinding.inflate(layoutInflater)
        mFlDetails.addView(mLayoutProductDetailMainDetailsBinding.root)

        mLayoutProductDetailMainDetailsBinding.tvRow1Column1.text = getString(R.string.text_release_date_product_detail)
        mLayoutProductDetailMainDetailsBinding.tvRow1Column2.text = getString(R.string.text_category_product_detail)
        mLayoutProductDetailMainDetailsBinding.tvRow3Column1.text = getString(R.string.text_item_number_product_dtail)
        mLayoutProductDetailMainDetailsBinding.tvRow3Column2.text = getString(R.string.text_product_type_product_detail)
        mLayoutProductDetailMainDetailsBinding.tvRow5Column1.text = getString(R.string.text_exclusivity_product_detail)
        mLayoutProductDetailMainDetailsBinding.tvRow5Column2.text = getString(R.string.text_license_product_detail)

        mLayoutProductDetailMainDetailsBinding.groupYugioh.gone()

        mMcvDescription.gone()
    }

    private fun setUpSealed() {
        mLayoutProductDetailCardTopBinding = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mLayoutProductDetailCardTopBinding.run {
            mFlTop.addView(root)
            llRow2.gone()
        }
        mMcvDescription.visible()
    }

    private fun seUpYugioh() {
        mLayoutProductDetailCardTopBinding = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(mLayoutProductDetailCardTopBinding.root)

        mLayoutProductDetailMainDetailsBinding = LayoutProductDetailMainDetailsBinding.inflate(layoutInflater)
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
        mLayoutProductDetailCardTopBinding = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(mLayoutProductDetailCardTopBinding.root)

        mLayoutProductDetailMtgDetailBinding = LayoutProductDetailMtgDetailBinding.inflate(layoutInflater)
        mFlDetails.addView(mLayoutProductDetailMtgDetailBinding.root)

        mMcvDescription.gone()
    }

    private fun setUpPokemon() {
        mLayoutProductDetailCardTopBinding = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(mLayoutProductDetailCardTopBinding.root)

        mLayoutProductDetailMainDetailsBinding = LayoutProductDetailMainDetailsBinding.inflate(layoutInflater)
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
        rvRelated.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = ProductAdapter(args.type, CustomProductCell.State.NORMAL)
        }
    }

    private fun setUpUpForSell() {
        rvUpForSell.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ProductAdapter(args.type, CustomProductCell.State.FOR_SALE)
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
            mTvDescription = it.tvDescription
            mRecentSellSeeAll = it.csavRecentSalesSeeAll
            mUpForSellSeeAll = it.csavUpForSalesSeeAll
            mBtnBuy = it.btnBuy
            mBtnCollect = it.btnCollect
            mBtnSell = it.btnSell
            initProgressBar(it.layoutProgress)
        }
    }

    private fun setViewData(data: SealedDomainModel) {
        mLayoutProductDetailCardTopBinding.run {
            tvTitle.text = data.name
            tvRow1Column2.text = data.set
        }
//        mTvDescription.text = data.description

    }

    private fun setViewData(data: MTGDomainModel) {
        mLayoutProductDetailCardTopBinding.run {
            tvTitle.text = data.name
            tvRow1Column2.text = data.setName
            tvRow2Collum2.text = getString(R.string.text_desh_product_detail)
            tvRow2Collum4.text = getString(R.string.text_desh_product_detail)

//            mLayoutProductDetailMtgDetailBinding.tvLegalRow1Collum1.text = data.pioneer
//            mLayoutProductDetailMtgDetailBinding.tvLegalRow1Collum2.text = data.modern
//            mLayoutProductDetailMtgDetailBinding.tvLegalRow1Collum3.text = data.legacy
//
//            mLayoutProductDetailMtgDetailBinding.tvLegalRow2Collum1.text = data.historic
//            mLayoutProductDetailMtgDetailBinding.tvLegalRow2Collum2.text = data.legacy
//            mLayoutProductDetailMtgDetailBinding.tvLegalRow2Collum3.text = data.commander

//            mLayoutProductDetailMtgDetailBinding.tvNotLegalRow1Collum1.text = data.standard
//            mLayoutProductDetailMtgDetailBinding.tvNotLegalRow1Collum2.text = data.brawl
//            mLayoutProductDetailMtgDetailBinding.tvNotLegalRow1Collum3.text = data.pauper
//
//            mLayoutProductDetailMtgDetailBinding.tvNotLegalRow2Collum1.text = data.penny
//            mLayoutProductDetailMtgDetailBinding.tvNotLegalRow2Collum2.text = data.pioneer
//            mLayoutProductDetailMtgDetailBinding.tvNotLegalRow2Collum3.text = data.pioneer

            mLayoutProductDetailMtgDetailBinding.tvTitle.text = data.typeLine
            mLayoutProductDetailMtgDetailBinding.tvTextLine1.text = data.flavorText
//            mLayoutProductDetailMtgDetailBinding.tvOutOf.text = (data.power/data.toughness)

        }
    }

    private fun setViewData(data: YugiohDomainModel) {
        mLayoutProductDetailCardTopBinding.run {
            tvTitle.text = data.name
            tvRow1Column2.text = data.set
            tvRow2Collum2.text = data.number
            tvRow2Collum4.text = data.rarity
        }

        mLayoutProductDetailMainDetailsBinding.run {
            tvRow2Column1.text = data.number
            tvRow2Column2.text = data.productType.toString()
            tvRow4Column1.text = data.set
            tvRow4Column2.text = data.stats
            tvRow6Column1.text = data.rarity
            tvRow6Column2.text = ""
        }
    }

    private fun setViewData(data: PokemonDomainModel) {
        mLayoutProductDetailCardTopBinding.run {
            tvTitle.text = data.name
            tvRow1Column2.text = data.set
            tvRow2Collum2.text = data.number
            tvRow2Collum4.text = data.rarity
        }

        mLayoutProductDetailMainDetailsBinding.run {
            tvRow2Column1.text = data.number
            tvRow2Column2.text = data.cardType
            tvRow4Column1.text = data.set
            tvRow4Column2.text = data.hp
            tvRow6Column1.text = data.rarity
            tvRow6Column2.text = data.stage
            tvRow8Column1.text = data.japaneseNumber.toString()
//            tvRow8Column2.text = data.japeneseSet
        }
    }

    private fun setViewData(data: FunkoDomainModel) {
        mLayoutProductDetailToyTopBinding.tvTitle.text = data.name
        mLayoutProductDetailToyTopBinding.tvRow1Column2.text = data.license
        mLayoutProductDetailToyTopBinding.tvRow2Column2.text = data.itemNumber

        mLayoutProductDetailMainDetailsBinding.tvRow2Column1.text = if(data.releaseDate.isEmpty())data.releaseDate else "N/A"
        mLayoutProductDetailMainDetailsBinding.tvRow2Column2.text = if(data.category.isEmpty())data.category else "N/A"

        mLayoutProductDetailMainDetailsBinding.tvRow4Column2.text = data.productType.toString()
        mLayoutProductDetailMainDetailsBinding.tvRow4Column1.text = if(data.itemNumber.isEmpty())data.itemNumber else "N/A"

        mLayoutProductDetailMainDetailsBinding.tvRow6Column2.text = if(data.license.isEmpty())data.license else "N/A"
//        mLayoutProductDetailMainDetailsBinding.tvRow6Column1.text = if(data.exclusivity.isEmpty())data.exclusivity else "N/A"
    }
}