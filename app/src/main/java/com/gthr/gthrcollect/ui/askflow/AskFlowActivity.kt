package com.gthr.gthrcollect.ui.askflow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.ActivityAskFlowBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.ui.askflow.afcardlanguage.AfCardLanguageFragmentArgs
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.receiptdetail.purchasedetails.FullProductImage
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.enums.*
import com.gthr.gthrcollect.utils.extensions.*
import com.gthr.gthrcollect.utils.logger.GthrLogger
import de.hdodenhof.circleimageview.CircleImageView

class AskFlowActivity : BaseActivity<AskFlowViewModel, ActivityAskFlowBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels {
        AskFlowViewModelFactory(AskFlowRepository())
    }

    override fun getViewBinding() = ActivityAskFlowBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mToolbar: Toolbar
    private var mMenu: Menu? = null

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
        mProductDisplayModel = intent.getParcelableExtra<ProductDisplayModel>(KEY_PRODUCT_DISPLAY_MODEL)!!
        mProductType = mProductDisplayModel.productType!!
        mProductCategory = mProductDisplayModel.productCategory!!

        mViewModel.setProductType(mProductType)
        mViewModel.setProductDisplayModel(mProductDisplayModel)
        mViewModel.setIsEdit(intent.getBooleanExtra(KEY_IS_EDIT, false))

        initViews()
        setSupportActionBar(mToolbar)
        setUpNavGraph()
        setUpNavigationAndActionBar()
        setUpClickListeners()
        setUpObservers()
        setData()
    }

    private fun setData() {
        if (mAskFlowType != AskFlowType.BUY_DIRECTLY_FROM_SOMEONE)
            if (mViewModel.isEdit) {
                mProductItem.setValue(mProductDisplayModel)
                mProductDisplayModel.forsaleItemNodel?.backImageURL?.let {
                    if (it.isEmpty()) return
                    mCvBackImage.visible()
                    mIvBackImage.setProductImage(it)
                }
            } else
                mViewModel.getProductDetails(mProductDisplayModel.refKey!!, mProductType)
        else {
            GthrLogger.i("sdhbsd", "${mProductDisplayModel.forsaleItemNodel}")
            mProductDisplayModel.forsaleItemNodel?.backImageURL?.let {
                mViewModel.setBackImageDownloadUrl(it)
            }
            mViewModel.setBuyingDirFromSomeOneProPrice(mProductDisplayModel.forsaleItemNodel?.price!!)
            mViewModel.getUserImage(mProductDisplayModel.forsaleItemNodel?.collectionFirebaseRef!!)
            mViewModel.getUserDisplayName(mProductDisplayModel.forsaleItemNodel?.collectionFirebaseRef!!)
            mProductItem.setValue(mProductDisplayModel)
        }
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

        mTvUserName.setOnClickListener {
            mProductDisplayModel.forsaleItemNodel?.collectionFirebaseRef?.let {
                if(it != GthrCollect.prefs?.getUserCollectionId())
                    startActivity(ProfileActivity.getInstance(this, ProfileNavigationType.PROFILE,it))
            }
        }

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

        mViewModel.mDisplayName.observe(this) {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        mTvUserName.text = it.data
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                    }
                }
            }
        }

        mViewModel.mUserImage.observe(this) {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        mIvUserProfile.setProductImage(it.data)
                        mViewModel.getUserDisplayName(mProductDisplayModel.forsaleItemNodel?.collectionFirebaseRef!!)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                    }
                }
            }
        }

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

        //Delete
        mViewModel.deleteAsk.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Loading -> {
                        showProgressBar(true)
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        showToast(it.data.toString())
                        openHomePage()
                    }
                }
            }
        })
        mViewModel.deleteCollection.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Loading -> {
                        showProgressBar(true)
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        openHomePage()
                    }
                }
            }
        })
        mViewModel.deleteBid.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Loading -> {
                        showProgressBar(true)
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        openHomePage()
                    }
                }
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
            if (mViewModel.isEdit)
                startActivity(
                    FullProductImage.getInstance(
                        this,
                        mViewModel.productDisplayModel?.forsaleItemNodel?.backImageURL.toString()
                    )
                )
            else
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
                R.id.afPlaceYourAskFragment -> {
                    if (mViewModel.isEdit)
                        mMenu?.findItem(R.id.menu_delete)?.isVisible = false
                }
                else -> {
                    if (mViewModel.isEdit)
                        mMenu?.findItem(R.id.menu_delete)?.isVisible = true
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

            if (mViewModel.isEdit) return
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mMenu = menu
        if (mViewModel.isEdit)
            menuInflater.inflate(R.menu.ask_flow_edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            when {
                AskFlowType.SELL == mAskFlowType -> {
//                    showToast(mViewModel.productDisplayModel?.forsaleItemNodel?.askRefKey.toString())
                    MaterialAlertDialogBuilder(this).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this Ask?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            mViewModel.deleteAsk(mViewModel.productDisplayModel?.forsaleItemNodel?.askRefKey.toString())
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                AskFlowType.COLLECT == mAskFlowType -> {
//                    showToast(mViewModel.productDisplayModel?.forsaleItemNodel?.collectionItemRefKey.toString())
                    MaterialAlertDialogBuilder(this).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this Collection?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            mViewModel.deleteCollection(mViewModel.productDisplayModel?.forsaleItemNodel?.collectionItemRefKey.toString())
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                AskFlowType.BUY == mAskFlowType -> {
//                    showToast(mViewModel.productDisplayModel?.searchBidsDomainModel?.bidRefKey.toString())
                    MaterialAlertDialogBuilder(this).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this Bid?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            mViewModel.deleteBid(mViewModel.productDisplayModel?.searchBidsDomainModel?.bidRefKey.toString())
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openHomePage() {
        startActivity(HomeBottomNavActivity.getInstance(this, goToProfileSignUp = true).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    internal fun getAskFlowType(): AskFlowType = mAskFlowType

    companion object {
        private const val KEY_ASK_FLOW_TYPE = "key_ask_flow_type"
        private const val KEY_PRODUCT_DISPLAY_MODEL = "key_product_display_model"
        private const val KEY_IS_EDIT = "key_is_edit"

        fun getInstance(
            context: Context,
            askFlowType: AskFlowType,
            productDisplayModel: ProductDisplayModel?,
            isEdit: Boolean = false
        ) = Intent(context, AskFlowActivity::class.java).apply {
            putExtra(KEY_ASK_FLOW_TYPE, askFlowType)
            putExtra(KEY_PRODUCT_DISPLAY_MODEL, productDisplayModel)
            putExtra(KEY_IS_EDIT, isEdit)
        }
    }
}