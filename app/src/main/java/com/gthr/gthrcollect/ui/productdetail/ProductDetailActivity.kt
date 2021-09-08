package com.gthr.gthrcollect.ui.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.databinding.*
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.productdetail.productdetailscreen.ProductDetailFragmentArgs
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger

class ProductDetailActivity :
    BaseActivity<ProductDetailsViewModel, ActivityProductDetailBinding>() {
    override fun getViewBinding() = ActivityProductDetailBinding.inflate(layoutInflater)
    override val mViewModel: ProductDetailsViewModel by viewModels {
        ProductDetailsViewModelFactory(
            ProductDetailsRepository()
        )
    }

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mToolbar: Toolbar

    private lateinit var mProductDisplayModel: ProductDisplayModel

    private lateinit var mFlTop: FrameLayout
    //Top View for pokemon, mtg, Sealed, Yugioh
    private lateinit var mLayoutProductDetailCardTopBinding: LayoutProductDetailCardTopBinding
    //Top View for funko
    private lateinit var mLayoutProductDetailToyTopBinding: LayoutProductDetailToyTopBinding

    override fun onBinding() {
        mProductDisplayModel = intent.getParcelableExtra<ProductDisplayModel>(KEY_PRODUCT_DISPLAY_MODEL)!!

        initViews()
        setUpNavGraph()
        setSupportActionBar(mToolbar)
        setUpNavigationAndActionBar()
        setUpProductType()
        setUpObserver()
    }

    private fun setUpObserver() {

        mViewModel.mMtgProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        if(it.data.objectID.isNotEmpty())
                            mViewModel.getRecentSaleList(it.data.objectID)
                        if(it.data.setName.isNotEmpty())
                            mViewModel.getRelatedProductList(it.data.setName,ProductType.MAGIC_THE_GATHERING)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.mFunkoProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        if(it.data.objectID.isNotEmpty())
                            mViewModel.getRecentSaleList(it.data.objectID)
                        if(it.data.license.isNotEmpty())
                            mViewModel.getRelatedProductList(it.data.license,ProductType.FUNKO)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.mPokemonProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        if(it.data.objectID.isNotEmpty())
                            mViewModel.getRecentSaleList(it.data.objectID)
                        if(it.data.set.isNotEmpty())
                            mViewModel.getRelatedProductList(it.data.set,ProductType.POKEMON)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.mSealedProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        if(it.data.objectID.isNotEmpty())
                            mViewModel.getRecentSaleList(it.data.objectID)
                        if(it.data.set.isNotEmpty())
                            mViewModel.getRelatedProductList(it.data.set,ProductType.SEALED_POKEMON)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
        mViewModel.mYugiohProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        GthrLogger.i("dschjds", "Product: ${it.data}")
                        showProgressBar(false)
                        if(it.data.objectID.isNotEmpty())
                            mViewModel.getRecentSaleList(it.data.objectID)
                        if(it.data.set.isNotEmpty())
                            mViewModel.getRelatedProductList(it.data.set,ProductType.YUGIOH)
                        setViewData(it.data)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }
    }

    private fun setUpProductType() {
        when (mProductDisplayModel.productType) {
            ProductType.POKEMON -> setUpPokemon()
            ProductType.MAGIC_THE_GATHERING -> setUpMGT()
            ProductType.YUGIOH -> seUpYugioh()
            ProductType.SEALED_POKEMON, ProductType.SEALED_MTG, ProductType.SEALED_YUGIOH -> setUpSealed()
            ProductType.FUNKO -> setUpFunko()
        }
        when (mProductDisplayModel.productType) {
            ProductType.POKEMON -> mViewModel.getProductDetails(
                mProductDisplayModel.refKey!!,
                ProductType.POKEMON
            )
            ProductType.MAGIC_THE_GATHERING -> mViewModel.getProductDetails(
                mProductDisplayModel.refKey!!,
                ProductType.MAGIC_THE_GATHERING
            )
            ProductType.YUGIOH -> mViewModel.getProductDetails(
                mProductDisplayModel.refKey!!,
                ProductType.YUGIOH
            )
            ProductType.SEALED_POKEMON, ProductType.SEALED_MTG, ProductType.SEALED_YUGIOH -> mViewModel.getProductDetails(
                mProductDisplayModel.refKey!!,
                ProductType.SEALED_POKEMON
            )
            ProductType.FUNKO -> mViewModel.getProductDetails(
                mProductDisplayModel.refKey!!,
                ProductType.FUNKO
            )
        }
    }

    private fun setUpPokemon() {
        mLayoutProductDetailCardTopBinding = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(mLayoutProductDetailCardTopBinding.root)
    }

    private fun setUpMGT() {
        mLayoutProductDetailCardTopBinding = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(mLayoutProductDetailCardTopBinding.root)
    }

    private fun seUpYugioh() {
        mLayoutProductDetailCardTopBinding = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mFlTop.addView(mLayoutProductDetailCardTopBinding.root)
    }

    private fun setUpSealed() {
        mLayoutProductDetailCardTopBinding = LayoutProductDetailCardTopBinding.inflate(layoutInflater)
        mLayoutProductDetailCardTopBinding.run {
            mFlTop.addView(root)
            llRow2.gone()
        }
    }

    private fun setUpFunko() {
        mLayoutProductDetailToyTopBinding = LayoutProductDetailToyTopBinding.inflate(layoutInflater)
        mFlTop.addView(mLayoutProductDetailToyTopBinding.root)
    }

    private fun setUpNavGraph() { //Setting NavGraph manually so that we can pass data to start destination
        findNavController(R.id.nav_host_fragment)
            .setGraph(
                R.navigation.product_detail_nav_graph,
                ProductDetailFragmentArgs(productDisplayModel = mProductDisplayModel).toBundle()
            )
    }

    private fun initViews() {
        mViewBinding.run {
            mToolbar = toolbar
            mFlTop = flTop
        }
    }

    private fun setUpNavigationAndActionBar() {
        mNavController = this.findNavController(R.id.nav_host_fragment)
        mAppBarConfiguration = /*AppBarConfiguration(navController.graph)*/
            AppBarConfiguration.Builder().build()   //To show up button in start destination

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration)

        mNavController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            mToolbar.title = ""     //Set Title as empty as we have used custom title
            upButtonVisibility(isVisible = true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_up_button) //Set up button as <

            when (nd.id) {
                R.id.productDetailFragment -> {
                    setToolbarTitle("")
                }
            }
        }
    }


    fun setToolbarTitle(title: String) {
        mViewBinding.toolbarTitle.text = title
    }

    private fun upButtonVisibility(isVisible: Boolean) {
        supportActionBar?.setDisplayShowHomeEnabled(isVisible)
        supportActionBar?.setDisplayHomeAsUpEnabled(isVisible)
    }

    override fun onSupportNavigateUp(): Boolean {
        return when {
            NavigationUI.navigateUp(mNavController, mAppBarConfiguration) -> true
            else -> {
                finish()
                false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> showToast("Share")
            R.id.menu_favourite -> showToast("Favourite")
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val KEY_PRODUCT_DISPLAY_MODEL = "key_product_display_model"

        fun getInstance(context: Context, productDisplayModel: ProductDisplayModel?) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(KEY_PRODUCT_DISPLAY_MODEL, productDisplayModel)
            }
    }

    private fun setViewData(data: SealedDomainModel) {
        mLayoutProductDetailCardTopBinding.run {
            tvTitle.text = data.name
            tvRow1Column2.text = data.set
        }
    }

    private fun setViewData(data: MTGDomainModel) {
        mLayoutProductDetailCardTopBinding.run {
            tvTitle.text = data.name
            tvRow1Column2.text = data.setName
            tvRow2Collum2.text = getString(R.string.text_desh_product_detail)
            tvRow2Collum4.text = getString(R.string.text_desh_product_detail)
        }
    }

    private fun setViewData(data: YugiohDomainModel) {
        mLayoutProductDetailCardTopBinding.run {
            tvTitle.text = data.name
            tvRow1Column2.text = data.set
            tvRow2Collum2.text = data.number
            tvRow2Collum4.text = data.rarity
        }
    }

    private fun setViewData(data: PokemonDomainModel) {
        mLayoutProductDetailCardTopBinding.run {
            tvTitle.text = data.name
            tvRow1Column2.text = data.set
            tvRow2Collum2.text = data.number
            tvRow2Collum4.text = data.rarity
        }
    }

    private fun setViewData(data: FunkoDomainModel) {
        mLayoutProductDetailToyTopBinding.run {
            tvTitle.text = data.name
            tvRow1Column2.text = data.license
            tvRow2Column2.text = data.itemNumber
        }
    }
}