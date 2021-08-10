package com.gthr.gthrcollect.ui.receiptdetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.navigation.ProfileNavigationFragmentArgs
import com.gthr.gthrcollect.ui.receiptdetail.purchasedetails.PurchaseDetailsFragmentArgs
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.enums.ReceiptType
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class ReceiptDetailActivity : BaseActivity<ReceiptDetailViewModel, ActivityReceiptDetailBinding>() {
    override val mViewModel: ReceiptDetailViewModel by viewModels()
    override fun getViewBinding() = ActivityReceiptDetailBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mToolbar: Toolbar
    private lateinit var mType: ReceiptType

    override fun onBinding() {
        mType = intent.getSerializableExtra(KEY_RECEIPT_TYPE) as ReceiptType

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
                PurchaseDetailsFragmentArgs(receiptType = mType).toBundle()
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
                    if (mType == ReceiptType.PURCHASED)
                        setToolbarTitle(getString(R.string.text_purchase_detail))
                    else if (mType == ReceiptType.SOLD)
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

        fun getInstance(context: Context, receiptType: ReceiptType) = Intent(context, ReceiptDetailActivity::class.java).apply {
            putExtra(KEY_RECEIPT_TYPE, receiptType)
        }
    }
}