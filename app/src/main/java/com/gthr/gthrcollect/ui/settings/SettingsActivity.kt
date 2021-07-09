package com.gthr.gthrcollect.ui.settings

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
import com.gthr.gthrcollect.databinding.ActivitySettingsBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class SettingsActivity : BaseActivity<SettingsViewModel, ActivitySettingsBinding>() {
    override val mViewModel: SettingsViewModel by viewModels()
    override fun getViewBinding() = ActivitySettingsBinding.inflate(layoutInflater)


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
                R.id.settingsFragment -> {
                    setToolbarTitle(getString(R.string.settings_text))
                    mViewBinding.toolbar.visible()
                }
                R.id.editAccountInfoFragment -> {
                    setToolbarTitle(getString(R.string.edit_account_info_title))
                }
                R.id.activeOffersFragment -> {
                    setToolbarTitle(getString(R.string.active_offers_text))
                }
                R.id.editShippingAddressFragment -> {
                    setToolbarTitle(getString(R.string.edit_shipping_address_text))
                }
                R.id.addNewAddressFragment -> {
                    setToolbarTitle(getString(R.string.add_new_address_text))
                }
                R.id.termsAndConditionsFragment, R.id.faqAndHelpFragment -> {
                    mViewBinding.toolbar.gone()
                }
            }
        }
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

    fun setToolbarTitle(title: String) {
        mViewBinding.toolbarTitle.text = title
    }


    companion object {
        fun getInstance(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}