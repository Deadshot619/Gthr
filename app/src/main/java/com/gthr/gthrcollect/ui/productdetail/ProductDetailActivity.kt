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
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.databinding.*
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.ui.productdetail.productdetailscreen.ProductDetailFragmentArgs
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger

class ProductDetailActivity : BaseActivity<ProductDetailsViewModel, ActivityProductDetailBinding>() {
    override fun getViewBinding() = ActivityProductDetailBinding.inflate(layoutInflater)
    override val mViewModel: ProductDetailsViewModel by viewModels {
        ProductDetailsViewModelFactory(
            ProductDetailsRepository(),
            DynamicLinkRepository()
        )
    }

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mToolbar: Toolbar

    private var mObjectId : String? = null
    private var mProductType: ProductType? = null

    private lateinit var mFlTop: FrameLayout
    //Top View for pokemon, mtg, Sealed, Yugioh
    private lateinit var mLayoutProductDetailCardTopBinding: LayoutProductDetailCardTopBinding
    //Top View for funko
    private lateinit var mLayoutProductDetailToyTopBinding: LayoutProductDetailToyTopBinding

    override fun onBinding() {
        mObjectId = intent.getStringExtra(KEY_OBJECT_ID)
        mProductType = intent.getSerializableExtra(KEY_PRODUCT_TYPE) as ProductType

        mViewModel.checkProductFavorite(mProductType!!, mObjectId!!)
        initViews()
        setUpProductType()
        setUpObserver()
    }

    private fun setUpObserver() {
        mViewModel.mProductDynamicLink.observe(this){
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        val intent = Intent()
                       // val msg = "Click and install this application $shortLink Refer code : mayankbaba"
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT, it.data)
                        intent.type = "text/plain"
                        startActivity(intent)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }

        mViewModel.mProductDisplayModel.observe(this){
            it.contentIfNotHandled?.let {
                setUpNavGraph(it)
                setSupportActionBar(mToolbar)
                setUpNavigationAndActionBar()
            }
        }

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
                        mViewModel.setProductDisplayModel(ProductDisplayModel(it.data))
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
                        mViewModel.setProductDisplayModel(ProductDisplayModel(it.data))
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
                        mViewModel.setProductDisplayModel(ProductDisplayModel(it.data))
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
                        mViewModel.setProductDisplayModel(ProductDisplayModel(it.data))
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
                        if (it.data.set.isNotEmpty())
                            mViewModel.getRelatedProductList(it.data.set, ProductType.YUGIOH)
                        setViewData(it.data)
                        mViewModel.setProductDisplayModel(ProductDisplayModel(it.data))
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }

        mViewModel.addFavorites.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        showToast("Added!")
                        mViewModel.checkProductFavorite(mProductType!!, mObjectId!!)
                    }
                }
            }
        })

        mViewModel.removeFavorites.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        showToast("Removed!")
                        mViewModel.checkProductFavorite(mProductType!!, mObjectId!!)
                    }
                }
            }
        })
    }

    private fun setUpProductType() {
        if(mObjectId!=null&&mProductType!=null){
            when (mProductType) {
                ProductType.POKEMON -> setUpPokemon()
                ProductType.MAGIC_THE_GATHERING -> setUpMGT()
                ProductType.YUGIOH -> seUpYugioh()
                ProductType.SEALED_POKEMON, ProductType.SEALED_MTG, ProductType.SEALED_YUGIOH -> setUpSealed()
                ProductType.FUNKO -> setUpFunko()
            }
            when (mProductType) {
                ProductType.POKEMON -> mViewModel.getProductDetails(
                    mObjectId!!,
                    ProductType.POKEMON
                )
                ProductType.MAGIC_THE_GATHERING -> mViewModel.getProductDetails(
                    mObjectId!!,
                    ProductType.MAGIC_THE_GATHERING
                )
                ProductType.YUGIOH -> mViewModel.getProductDetails(
                    mObjectId!!,
                    ProductType.YUGIOH
                )
                ProductType.SEALED_POKEMON, ProductType.SEALED_MTG, ProductType.SEALED_YUGIOH -> mViewModel.getProductDetails(
                    mObjectId!!,
                    ProductType.SEALED_POKEMON
                )
                ProductType.FUNKO -> mViewModel.getProductDetails(
                    mObjectId!!,
                    ProductType.FUNKO
                )
            }
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

    private fun setUpNavGraph(mProductDisplayModel : ProductDisplayModel ) { //Setting NavGraph manually so that we can pass data to start destination
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
            initProgressBar(layoutProgress)
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
            R.id.menu_share -> {
                mViewModel.getProductDynamicLink(mObjectId!!,mProductType!!)
            }
            R.id.menu_favourite -> {
                if(isUserLoggedIn())
                    addRemoveFavourite()
                else
                    startActivity(HomeBottomNavActivity.getInstance(this))
            }
        }
        return super.onOptionsItemSelected(item)
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
            tvRow2Collum2.text = data.collectorNumber
            tvRow2Collum4.text = data.rarity
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

    private fun isUserLoggedIn(): Boolean {
        GthrCollect.prefs?.signedInUser?.let {
            return@isUserLoggedIn !it.email.isNullOrEmpty() && it.uid.isNotEmpty()
        } ?: return false
    }

    private fun addRemoveFavourite() {
        val isFavorite = (mViewModel.isFavorite.value?.peekContent() as State.Success).data
        if (isFavorite)
            mViewModel.removeFavorites(mProductType!!, mObjectId!!)
        else
            mViewModel.addFavorites(mProductType!!, mObjectId!!)
    }

    companion object {

        private const val KEY_PRODUCT_TYPE = "key_product_type"
        private const val KEY_OBJECT_ID = "key_object_id"

        fun getInstance(context: Context, objectId: String?, type: ProductType?) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(KEY_PRODUCT_TYPE, type)
                putExtra(KEY_OBJECT_ID, objectId)
            }
    }
}