package com.gthr.gthrcollect.ui.askflow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.ActivityAskFlowBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.ui.askflow.afcardlanguage.AfCardLanguageFragmentArgs
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.receiptdetail.purchasedetails.FullProductImage
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.ConditionType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.invisible
import com.gthr.gthrcollect.utils.extensions.setImage
import com.gthr.gthrcollect.utils.extensions.visible
import de.hdodenhof.circleimageview.CircleImageView

class AskFlowActivity : BaseActivity<AskFlowViewModel, ActivityAskFlowBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels {
        AskFlowViewModelFactory(AskFlowRepository())
    }

    override fun getViewBinding() = ActivityAskFlowBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mToolbar: Toolbar

    private lateinit var mAskFlowType: AskFlowType
    private lateinit var mProductDisplayModel: ProductDisplayModel
    private lateinit var mProductCategory: ProductCategory
    private lateinit var mProductType: ProductType

    private lateinit var mIvUserProfile: CircleImageView
    private lateinit var mTvUserName: AppCompatTextView
    private lateinit var mProductItem: CustomProductCell
    private lateinit var mCvBackImage: MaterialCardView
    private lateinit var mIvBackImage: AppCompatImageView

    override fun onBinding() {
        mAskFlowType = intent?.getSerializableExtra(KEY_ASK_FLOW_TYPE) as AskFlowType
        mProductDisplayModel =
            intent.getParcelableExtra<ProductDisplayModel>(KEY_PRODUCT_DISPLAY_MODEL)!!
        mProductType = mProductDisplayModel.productType!!
        mProductCategory = mProductDisplayModel.productCategory!!

        mViewModel.setProductType(mProductType)
        mViewModel.setProductDisplayModel(mProductDisplayModel)

        initViews()
        setSupportActionBar(mToolbar)
        setUpNavGraph()
        setUpNavigationAndActionBar()
        setUpClickListeners()
        setUpObservers()

        mViewModel.getProductDetails(mProductDisplayModel.refKey!!, mProductType)
    }

    private fun initViews() {
        mViewBinding.run {
            mToolbar = toolbar
            mIvUserProfile = ivUserProfile
            mTvUserName = tvUserName
            mProductItem = cpcProductItem
            mCvBackImage = cvBackImage
            mIvBackImage = ivBackImage

            when (mAskFlowType) {
                AskFlowType.BUY -> mProductItem.setState(CustomProductCell.State.WANT)
                AskFlowType.SELL -> {
                    mProductItem.setState(CustomProductCell.State.FOR_SALE)
                    mViewModel.setSell(true)
                }
                AskFlowType.COLLECT -> {
                    mProductItem.setState(CustomProductCell.State.FOR_SALE)
                    mProductItem.setLabelVisibility(isVisible = false)
                }
                AskFlowType.BUY_DIRECTLY_FROM_SOMEONE -> {
                    mProductItem.setState(CustomProductCell.State.FOR_SALE)
                    mProductItem.setLabelVisibility(isVisible = false)
                    mIvUserProfile.visible()
                    mTvUserName.visible()
                }
            }

            initProgressBar(layoutProgress)
        }
    }

    private fun setUpObservers() {
        /* Front & Back Image */
        mViewModel.frontImageUrl.observe(this, {
            if (it != null)
                mProductItem.mIvMain.setImage(it)
        })

        mViewModel.backImageUrl.observe(this, {
            if (it != null) {
                mCvBackImage.visible()
                mIvBackImage.setImage(it)
            } else
                mCvBackImage.invisible()
        })

        /* Product Details */
        mViewModel.yugiohProductDetails.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        setInitialData(ProductDisplayModel(it.data))
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                    }
                }
            }
        })
        mViewModel.pokemonProductDetails.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        setInitialData(ProductDisplayModel(it.data))
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                    }
                }
            }
        })
        mViewModel.mtgProductDetails.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        setInitialData(ProductDisplayModel(it.data))
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                    }
                }
            }
        })
        mViewModel.funkoProductDetails.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        setInitialData(ProductDisplayModel(it.data))
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                    }
                }
            }
        })
        mViewModel.sealedProductDetails.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        setInitialData(ProductDisplayModel(it.data))
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                    }
                }
            }
        })


        /*Selected Language, Edition & Condition*/
        mViewModel.askPrice.observe(this, {
            it?.let {
                mProductItem.setPrice(it.toString())
            }
        })
        mViewModel.selectedLanguage.observe(this, {
            it.contentIfNotHandled?.let {
                mProductItem.setLanguage(it.abbreviatedName)
            }
        })
        mViewModel.selectedEdition.observe(this, {
            it.contentIfNotHandled?.let {
                mProductItem.setEdition(it.title)
            }
        })
        mViewModel.selectedConditionTitle.observe(this, {
            it.contentIfNotHandled?.let {
                mProductItem.setConditionTitle(
                    when (it) {
                        ConditionType.UG -> getString(R.string.raw)
                        ConditionType.PSA -> getString(R.string.psa)
                        ConditionType.BGS -> getString(R.string.bgs)
                        ConditionType.CGC -> getString(R.string.cgc)
                        else -> getString(R.string.raw)
                    }
                )
            }
        })
        mViewModel.selectedCondition.observe(this, {
            it.contentIfNotHandled?.let {
                mProductItem.setConditionValue(it.abbreviatedName)
            }
        })
    }

    private fun setUpClickListeners() {
        mProductItem.setOnClickListener {
            startActivity(
                FullProductImage.getInstance(
                    this,
                    (mViewModel.frontImageUrl.value ?: mProductDisplayModel.firImageURL)
                )
            )
        }

        mCvBackImage.setOnClickListener {
            mViewModel.backImageUrl.value?.let {
                startActivity(FullProductImage.getInstance(this, it))
            }
        }
    }

    private fun setUpNavGraph() { //Setting NavGraph manually so that we can pass data to start destination
        findNavController(R.id.nav_host_fragment)
            .setGraph(
                R.navigation.ask_flow_nav_graph,
                AfCardLanguageFragmentArgs(productDisplayModel = mProductDisplayModel).toBundle()
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
            setToolbarTitleByType(mAskFlowType)

            when (nd.id) {
                R.id.afBuyListDetailsFragment -> {
                    setToolbarTitle(getString(R.string.text_buylist_details))
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    override fun onBackPressed() {
        if (mNavController.currentDestination?.id == R.id.afBuyListDetailsFragment)
            finish()
        else
            finish()
    }

    private fun setToolbarTitleByType(askFlowType: AskFlowType) {
        when (askFlowType) {
            AskFlowType.BUY -> setToolbarTitle(getString(R.string.text_add_to_buylist))
            AskFlowType.COLLECT -> setToolbarTitle(getString(R.string.text_add_to_collection))
            AskFlowType.SELL -> setToolbarTitle(getString(R.string.text_place_an_ask))
            AskFlowType.BUY_DIRECTLY_FROM_SOMEONE -> setToolbarTitle(getString(R.string.text_purchase_detail))
        }
    }

    fun setToolbarTitle(title: String) {
        mViewBinding.toolbarTitle.text = title
    }

    private fun upButtonVisibility(isVisible: Boolean) {
        supportActionBar?.setDisplayShowHomeEnabled(isVisible)
        supportActionBar?.setDisplayHomeAsUpEnabled(isVisible)
    }

    /**
     * Set initial data to [mProductItem] view
     */
    private fun setInitialData(productDisplayModel: ProductDisplayModel) {
        mProductItem.run {
            setValue(productDisplayModel)
            setPrice("-")

            if (mProductCategory == ProductCategory.CARDS) {
                setLanguage("-")
                setConditionTitle(getString(R.string.raw))
                setConditionValue("-")
                setEdition("-")
                mViewModel.retrieveLanguageList(productDisplayModel.productType!!)
            }
        }
    }

    internal fun getAskFlowType(): AskFlowType = mAskFlowType

    companion object {
        private const val KEY_ASK_FLOW_TYPE = "key_ask_flow_type"
        private const val KEY_PRODUCT_DISPLAY_MODEL = "key_product_display_model"

        fun getInstance(
            context: Context,
            askFlowType: AskFlowType,
            productDisplayModel: ProductDisplayModel?
        ) = Intent(context, AskFlowActivity::class.java).apply {
            putExtra(KEY_ASK_FLOW_TYPE, askFlowType)
            putExtra(KEY_PRODUCT_DISPLAY_MODEL, productDisplayModel)
        }
    }
}