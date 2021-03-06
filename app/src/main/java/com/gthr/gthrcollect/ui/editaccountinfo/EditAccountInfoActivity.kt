package com.gthr.gthrcollect.ui.editaccountinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.EditAccountInfoRepository
import com.gthr.gthrcollect.databinding.ActivityEditAccountInfoBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.utils.enums.EditAccountInfoFlow
import com.gthr.gthrcollect.utils.enums.EditAccountSection
import com.gthr.gthrcollect.utils.extensions.getBackgroundDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class EditAccountInfoActivity :
    BaseActivity<EditAccountInfoViewModel, ActivityEditAccountInfoBinding>() {

    private val repository = EditAccountInfoRepository()

    override val mViewModel: EditAccountInfoViewModel by viewModels {
        EditAccountInfoViewModelFactory(
            repository
        )
    }

    override fun getViewBinding(): ActivityEditAccountInfoBinding =
        ActivityEditAccountInfoBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    private lateinit var mEditAccountInfoFlow: EditAccountInfoFlow

    override fun onBinding() {
        mEditAccountInfoFlow =
            intent.getSerializableExtra(EDIT_ACCOUNT_INFO_FLOW) as EditAccountInfoFlow

        checkEditAccountFlowType()
        setUpNavGraph()
        setSupportActionBar(mViewBinding.toolbar)
        setUpNavigationAndActionBar()
    }

    private fun setUpNavGraph() { //Setting NavGraph manually so that we can pass data to start destination
        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.edit_account_info_nav_graph)
    }

    private fun setUpNavigationAndActionBar() {
        mNavController = this.findNavController(R.id.nav_host_fragment)
        mAppBarConfiguration = /*AppBarConfiguration(navController.graph)*/
            AppBarConfiguration.Builder().build()   //To show up button in start destination

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration)

        mNavController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            mViewBinding.toolbar.title = ""     //Set Title as empty as we have used custom title
            setToolbarTitle(getString(R.string.edit_account_info_title))
            upButtonVisibility(isVisible = true)

            if (getEditAccountFlowType() == EditAccountInfoFlow.GOV_ID)
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_icon) //Set up button as x
            else
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_up_button) //Set up button as <

            when (nd.id) {
                R.id.eaProfileFragment -> {
                    setSectionSelection(EditAccountSection.PROFILE)
                }
                R.id.eaUserInfoFragment, R.id.eaOtpFragment -> {
                    setSectionSelection(EditAccountSection.USER_INFO)
                }
                R.id.eaIdVerificationFragment -> {
                    setSectionSelection(EditAccountSection.ID_VERIFICATION)
                }
                R.id.welcomeFragment -> {
                    upButtonVisibility(isVisible = false)
                    setToolbarTitle(getString(R.string.welcome_screen_title))
                    setSectionSelection(null)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return when {
            mNavController.currentDestination?.id == R.id.eaIdVerificationFragment -> {
                finish()
                false
            }
            NavigationUI.navigateUp(mNavController, mAppBarConfiguration) -> true
            else -> {
                finish()
                false
            }
        }
    }

    override fun onBackPressed() {
        return when (mNavController.currentDestination?.id) {
            R.id.welcomeFragment, R.id.eaIdVerificationFragment -> {
                finish()
            }
            else -> super.onBackPressed()
        }
    }

    private fun checkEditAccountFlowType() {
        if (getEditAccountFlowType() == EditAccountInfoFlow.GOV_ID) {
            mViewBinding.layoutSectionSelection.run {
                llProfile.gone()
                ivThreeDotsFirst.gone()
                llUserInfo.gone()
                ivThreeDotsSecond.gone()
            }
        }
    }

    /**
     * Method to indicate which section is currently selected
     */
    private fun setSectionSelection(sectionType: EditAccountSection?) {
        mViewBinding.layoutSectionSelection.run {
            when (sectionType) {
                EditAccountSection.PROFILE -> {
                    layoutSelectionSectionVisibility(isVisible = true)
                    resetSection()
                    tvOne.background = getBackgroundDrawable(R.drawable.ic_round_blue_bg)
                    tvOne.setTextColor(getResolvedColor(R.color.white))
                    tvProfile.setTextColor(getResolvedColor(R.color.text_color_black))
                }
                EditAccountSection.USER_INFO, EditAccountSection.OTP -> {
                    layoutSelectionSectionVisibility(isVisible = true)
                    resetSection()
                    tvTwo.background = getBackgroundDrawable(R.drawable.ic_round_green_bg)
                    tvTwo.setTextColor(getResolvedColor(R.color.white))
                    tvUserInfo.setTextColor(getResolvedColor(R.color.text_color_black))
                }
                EditAccountSection.ID_VERIFICATION -> {
                    layoutSelectionSectionVisibility(isVisible = true)
                    resetSection()
                    tvThree.background = getBackgroundDrawable(R.drawable.ic_round_yellow_bg)
                    tvThree.setTextColor(getResolvedColor(R.color.white))
                    tvIdVerification.setTextColor(getResolvedColor(R.color.text_color_black))
                }
                else -> {
                    layoutSelectionSectionVisibility(isVisible = false)
                }
            }
        }
    }

    /**
     * Method to reset all the views in [R.layout.layout_ea_section_selection] to grey circular bg & text color as white
     */
    private fun resetSection() {
        mViewBinding.layoutSectionSelection.run {
            tvOne.background = getBackgroundDrawable(R.drawable.ic_round_grey_bg)
            tvTwo.background = getBackgroundDrawable(R.drawable.ic_round_grey_bg)
            tvThree.background = getBackgroundDrawable(R.drawable.ic_round_grey_bg)

            tvOne.setTextColor(getResolvedColor(R.color.text_color_dark_grey))
            tvTwo.setTextColor(getResolvedColor(R.color.text_color_dark_grey))
            tvThree.setTextColor(getResolvedColor(R.color.text_color_dark_grey))

            tvProfile.setTextColor(getResolvedColor(R.color.text_color_dark_grey))
            tvUserInfo.setTextColor(getResolvedColor(R.color.text_color_dark_grey))
            tvIdVerification.setTextColor(getResolvedColor(R.color.text_color_dark_grey))
        }
    }

    private fun layoutSelectionSectionVisibility(isVisible: Boolean) {
        if (isVisible)
            mViewBinding.layoutSectionSelection.clMainLayout.visible()
        else
            mViewBinding.layoutSectionSelection.clMainLayout.gone()
    }

    private fun setToolbarTitle(title: String) {
        mViewBinding.toolbarTitle.text = title
    }

    private fun upButtonVisibility(isVisible: Boolean) {
        supportActionBar?.setDisplayShowHomeEnabled(isVisible)
        supportActionBar?.setDisplayHomeAsUpEnabled(isVisible)
    }

    internal fun getEditAccountFlowType(): EditAccountInfoFlow = mEditAccountInfoFlow

    companion object {
        private const val EDIT_ACCOUNT_INFO_FLOW = "edit_account_info_flow"

        fun getInstance(context: Context, editAccountInfoFlow: EditAccountInfoFlow) =
            Intent(context, EditAccountInfoActivity::class.java).apply {
                putExtra(EDIT_ACCOUNT_INFO_FLOW, editAccountInfoFlow)
            }
    }
}