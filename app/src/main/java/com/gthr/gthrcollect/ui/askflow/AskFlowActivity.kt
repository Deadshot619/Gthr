package com.gthr.gthrcollect.ui.askflow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ActivityAskFlowBinding
import com.gthr.gthrcollect.databinding.LayoutProductDetailCardTopBinding
import com.gthr.gthrcollect.databinding.LayoutProductDetailToyTopBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductTypeOld

class AskFlowActivity : BaseActivity<AskFlowViewModel, ActivityAskFlowBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = ActivityAskFlowBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mToolbar: Toolbar
    private lateinit var mAskFlowType: AskFlowType

    private lateinit var mFlInfo: FrameLayout
    private lateinit var mProductItem: CustomProductCell

    override fun onBinding() {
        mAskFlowType = intent?.getSerializableExtra(KEY_ASK_FLOW_TYPE) as AskFlowType

        initViews()
        setSupportActionBar(mToolbar)
        setUpNavigationAndActionBar()
        setInfoView(ProductCategory.CARDS)
    }

    private fun initViews(){
        mViewBinding.run {
            mToolbar = toolbar
            mFlInfo = flInfo
            mProductItem = cpcProductItem

            mProductItem.mTvPrice.text = "$-"
            mProductItem.mTvGlob.text = "-"
            mProductItem.mTvPsaValue.text = "-"
            mProductItem.mTvFoil.text = "-"
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
            setToolbarTitle(mAskFlowType)

            when (nd.id) {
                /*R.id.placeAskFragment -> {
                }*/
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    fun setToolbarTitle(askFlowType: AskFlowType) {
        mViewBinding.toolbarTitle.text = when (askFlowType) {
            AskFlowType.BUY -> getString(R.string.text_add_to_buylist)
            AskFlowType.COLLECT -> getString(R.string.text_add_to_collection)
            AskFlowType.SELL -> getString(R.string.text_place_an_ask)
        }
    }

    private fun upButtonVisibility(isVisible: Boolean) {
        supportActionBar?.setDisplayShowHomeEnabled(isVisible)
        supportActionBar?.setDisplayHomeAsUpEnabled(isVisible)
    }

    private fun setInfoView(type: ProductCategory){
        when(type){
            ProductCategory.TOYS -> {
                mFlInfo.addView(LayoutProductDetailToyTopBinding.inflate(layoutInflater).root)
            }
            ProductCategory.SEALED -> {
                mFlInfo.addView(LayoutProductDetailCardTopBinding.inflate(layoutInflater).root)
            }
            ProductCategory.CARDS -> {
                mFlInfo.addView(LayoutProductDetailCardTopBinding.inflate(layoutInflater).root)
            }
        }
    }

    internal fun getAskFlowType(): AskFlowType = mAskFlowType

    companion object {
        private const val KEY_ASK_FLOW_TYPE = "key_ask_flow_type"

        fun getInstance(context: Context, askFlowType: AskFlowType) = Intent(context, AskFlowActivity::class.java).apply {
            putExtra(KEY_ASK_FLOW_TYPE, askFlowType)
        }
    }
}