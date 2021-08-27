package com.gthr.gthrcollect.ui.askflow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ActivityAskFlowBinding
import com.gthr.gthrcollect.ui.askflow.afcardlanguage.AfCardLanguageFragmentArgs
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.extensions.invisible
import com.gthr.gthrcollect.utils.extensions.visible

class AskFlowActivity : BaseActivity<AskFlowViewModel, ActivityAskFlowBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = ActivityAskFlowBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mToolbar: Toolbar

    private lateinit var mAskFlowType: AskFlowType
    private lateinit var mProductCategory: ProductCategory

    private lateinit var mProductItem: CustomProductCell
    private lateinit var mCvBackImage: MaterialCardView
    private lateinit var mIvBackImage: AppCompatImageView

    override fun onBinding() {
        mAskFlowType = intent?.getSerializableExtra(KEY_ASK_FLOW_TYPE) as AskFlowType
        mProductCategory = intent?.getSerializableExtra(KEY_PRODUCT_CATEGORY) as ProductCategory

        initViews()
        setUpNavGraph()
        setSupportActionBar(mToolbar)
        setUpNavigationAndActionBar()
        setUpObservers()
    }

    private fun initViews(){
        mViewBinding.run {
            mToolbar = toolbar
            mProductItem = cpcProductItem
            mCvBackImage = cvBackImage
            mIvBackImage = ivBackImage

            mProductItem.mTvPrice.text = "$-"
            mProductItem.mTvGlob.text = "-"
            mProductItem.mTvPsaValue.text = "-"
            mProductItem.mTvFoil.text = "-"

            when (mAskFlowType) {
                AskFlowType.BUY -> mProductItem.setState(CustomProductCell.State.WANT)
                AskFlowType.SELL -> mViewModel.setSell(true)
                AskFlowType.COLLECT -> {
                }
            }
        }
    }

    private fun setUpObservers() {
        mViewModel.frontImageBitmap.observe(this, {
            if (it != null)
                mProductItem.mIvMain.setImageBitmap(it)
        })

        mViewModel.backImageBitmap.observe(this, {
            if (it != null) {
                mCvBackImage.visible()
                mIvBackImage.setImageBitmap(it)
            } else
                mCvBackImage.invisible()
        })
    }

    private fun setUpNavGraph() { //Setting NavGraph manually so that we can pass data to start destination
        findNavController(R.id.nav_host_fragment)
            .setGraph(
                R.navigation.ask_flow_nav_graph,
                AfCardLanguageFragmentArgs(productCategory = mProductCategory).toBundle()
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
            setToolbarTitle(mAskFlowType)
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

    internal fun getAskFlowType(): AskFlowType = mAskFlowType

    companion object {
        private const val KEY_ASK_FLOW_TYPE = "key_ask_flow_type"
        private const val KEY_PRODUCT_CATEGORY = "key_product_category"

        fun getInstance(
            context: Context,
            askFlowType: AskFlowType,
            productCategory: ProductCategory
        ) = Intent(context, AskFlowActivity::class.java).apply {
            putExtra(KEY_ASK_FLOW_TYPE, askFlowType)
            putExtra(KEY_PRODUCT_CATEGORY, productCategory)
        }
    }
}