package com.gthr.gthrcollect.ui.receiptdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ActivityReceiptDetailBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.receiptdetail.purchasedetails.PurchaseDetailsFragmentArgs
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ReceiptType

class ReceiptDetailActivity : BaseActivity<ReceiptDetailViewModel, ActivityReceiptDetailBinding>() {
    override val mViewModel: ReceiptDetailViewModel by viewModels()
    override fun getViewBinding() = ActivityReceiptDetailBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mToolbar: Toolbar

    private lateinit var mReceiptType: ReceiptType
    private lateinit var mProductCategory: ProductCategory
    private lateinit var mButtonType: CustomDeliveryButton.Type

    override fun onBinding() {
        mReceiptType = intent.getSerializableExtra(KEY_RECEIPT_TYPE) as ReceiptType
        mProductCategory = intent.getSerializableExtra(KEY_PRODUCT_CATEGORY) as ProductCategory
        mButtonType = intent.getSerializableExtra(KEY_BUTTON_TYPE) as CustomDeliveryButton.Type

        initViews()
        setUpNavGraph()
        setSupportActionBar(mToolbar)
        setUpNavigationAndActionBar()
    }

    private fun initViews(){
        mViewBinding.run {
            mToolbar = toolbar
        }
    }

    private fun setUpNavGraph() { //Setting NavGraph manually so that we can pass data to start destination
        findNavController(R.id.nav_host_fragment)
            .setGraph(
                R.navigation.receipt_detail_nav_graph,
                PurchaseDetailsFragmentArgs(
                    receiptType = mReceiptType,
                    mProductCategory,
                    mButtonType
                ).toBundle()
            )
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
                R.id.purchaseDetailsFragment -> {
                    if (mReceiptType == ReceiptType.PURCHASED)
                        setToolbarTitle(getString(R.string.text_purchase_detail))
                    else if (mReceiptType == ReceiptType.SOLD)
                        setToolbarTitle(getString(R.string.text_sold_details))
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
        finish()
        return false
    }

    companion object {
        private const val KEY_RECEIPT_TYPE = "key_receipt_type"
        private const val KEY_PRODUCT_CATEGORY = "key_product_category"
        private const val KEY_BUTTON_TYPE = "key_button_type"

        fun getInstance(
            context: Context,
            receiptType: ReceiptType,
            productCategory: ProductCategory,
            buttonType: CustomDeliveryButton.Type = CustomDeliveryButton.Type.DELIVERED
        ) =
            Intent(context, ReceiptDetailActivity::class.java).apply {
                putExtra(KEY_RECEIPT_TYPE, receiptType)
                putExtra(KEY_PRODUCT_CATEGORY, productCategory)
                putExtra(KEY_BUTTON_TYPE, buttonType)
            }
    }
}