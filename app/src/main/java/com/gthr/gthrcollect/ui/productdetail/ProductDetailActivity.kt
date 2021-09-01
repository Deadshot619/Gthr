package com.gthr.gthrcollect.ui.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.databinding.ActivityProductDetailBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.productdetail.productdetailscreen.ProductDetailFragmentArgs
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger

class ProductDetailActivity :
    BaseActivity<ProductDetailsViewModel, ActivityProductDetailBinding>() {
    override fun getViewBinding() = ActivityProductDetailBinding.inflate(layoutInflater)
    override val mViewModel: ProductDetailsViewModel by viewModels{
        ProductDetailsViewModelFactory(
            ProductDetailsRepository()
        )
    }

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mToolbar: Toolbar

    override fun onBinding() {
        initViews()
        setUpNavGraph()
        setSupportActionBar(mToolbar)
        setUpNavigationAndActionBar()
        setUpObserver()

        mViewModel.getProductDetails("-MiTNOMShh5j-097pdyz",ProductType.MAGIC_THE_GATHERING)
        mViewModel.getProductDetails("0",ProductType.SEALED_POKEMON)
        mViewModel.getProductDetails("-MiTOfdj0XCDttwhYf-Q",ProductType.POKEMON)
        mViewModel.getProductDetails("-MiTSTt3dbVfOQYDmswu",ProductType.YUGIOH)
        mViewModel.getProductDetails("-MiTpkeK3aeS5L4lvUO0",ProductType.FUNKO)

    }

    private fun setUpObserver() {
        mViewModel.mtgProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> GthrLogger.i("dschjds", "Loading: ")
                    is State.Success -> GthrLogger.i("dschjds", "Product : ${it.data}")
                    is State.Failed ->GthrLogger.i("dschjds", "Failed: ${it.message}")
                }
            }
        }
        mViewModel.funkoProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> GthrLogger.i("dschjds", "Loading: ")
                    is State.Success -> GthrLogger.i("dschjds", "Product: ${it.data}")
                    is State.Failed ->GthrLogger.i("dschjds", "Failed: ${it.message}")
                }
            }
        }
        mViewModel.pokemonProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> GthrLogger.i("dschjds", "Loading: ")
                    is State.Success -> GthrLogger.i("dschjds", "Product: ${it.data}")
                    is State.Failed ->GthrLogger.i("dschjds", "Failed: ${it.message}")
                }
            }
        }
        mViewModel.sealedProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> GthrLogger.i("dschjds", "Loading: ")
                    is State.Success -> GthrLogger.i("dschjds", "Product: ${it.data}")
                    is State.Failed ->GthrLogger.i("dschjds", "Failed: ${it.message}")
                }
            }
        }
        mViewModel.yugiohProductDetails.observe(this) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> GthrLogger.i("dschjds", "Loading: ")
                    is State.Success -> GthrLogger.i("dschjds", "Product: ${it.data}")
                    is State.Failed ->GthrLogger.i("dschjds", "Failed: ${it.message}")
                }
            }
        }

    }

    private fun setUpNavGraph() { //Setting NavGraph manually so that we can pass data to start destination
        val type = intent.getSerializableExtra(PRODUCT_TYPE) as ProductType

        findNavController(R.id.nav_host_fragment)
            .setGraph(
                R.navigation.product_detail_nav_graph,
                ProductDetailFragmentArgs(type = type).toBundle()
            )
    }

    private fun initViews() {
        mViewBinding.run {
            mToolbar = toolbar
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
        private const val PRODUCT_TYPE = "product_type"

        fun getInstance(context: Context, productType: ProductType) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_TYPE, productType)
            }
    }
}