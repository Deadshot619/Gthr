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
import com.gthr.gthrcollect.databinding.ActivityEditAccountInfoBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.utils.enums.EditAccountSection
import com.gthr.gthrcollect.utils.extensions.getBackgroundDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class EditAccountInfoActivity :
    BaseActivity<EditAccountInfoViewModel, ActivityEditAccountInfoBinding>() {
    override val mViewModel: EditAccountInfoViewModel by viewModels()
    override fun getViewBinding(): ActivityEditAccountInfoBinding =
        ActivityEditAccountInfoBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration


    override fun onBinding() {
        setSupportActionBar(mViewBinding.toolbar)
        setUpNavigationAndActionBar()
    }

    private fun setUpNavigationAndActionBar() {
        mNavController = this.findNavController(R.id.nav_host_fragment)
        mAppBarConfiguration = /*AppBarConfiguration(navController.graph)*/
            AppBarConfiguration.Builder().build()   //To show up button in start destination

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration)

        mNavController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            mViewBinding.toolbar.title = ""     //Set Title as empty as we have used custom title
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_up_button) //Set up button as <

            when (nd.id) {
                R.id.eaProfileFragment -> {
                    setSectionSelection(EditAccountSection.PROFILE)
                }
                R.id.eaUserInfoFragment -> {
                    setSectionSelection(EditAccountSection.USER_INFO)
                }
                R.id.eaOtpFragment -> {
                    setSectionSelection(EditAccountSection.USER_INFO)
                }
                R.id.eaIdVerificationFragment -> {
                    setSectionSelection(EditAccountSection.ID_VERIFICATION)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (NavigationUI.navigateUp(mNavController, mAppBarConfiguration))
            true
        else {
            finish()
            false
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

    companion object {
        fun getInstance(context: Context) = Intent(context, EditAccountInfoActivity::class.java)
    }


}