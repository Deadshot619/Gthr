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
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.productdetail.productdetailscreen.ProductDetailFragmentArgs
import com.gthr.gthrcollect.utils.extensions.showToast

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

    override fun onBinding() {
        mProductDisplayModel =
            intent.getParcelableExtra<ProductDisplayModel>(KEY_PRODUCT_DISPLAY_MODEL)!!

        initViews()
        setUpNavGraph()
        setSupportActionBar(mToolbar)
        setUpNavigationAndActionBar()
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
}