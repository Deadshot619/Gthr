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
import com.gthr.gthrcollect.ui.settings.settingsscreen.SettingsFragmentDirections
import com.gthr.gthrcollect.utils.enums.SettingFlowType

class SettingsActivity : BaseActivity<SettingsViewModel, ActivitySettingsBinding>() {
    override val mViewModel: SettingsViewModel by viewModels()
    override fun getViewBinding() = ActivitySettingsBinding.inflate(layoutInflater)


    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mSettingsType: SettingFlowType


    override fun onBinding() {
        mSettingsType = intent?.getSerializableExtra(GOTO) as SettingFlowType
        initProgressBar(mViewBinding.layoutProgress)
        setUpNavGraph()
        setSupportActionBar(mViewBinding.toolbar)
        setUpNavigationAndActionBar()

        if (mSettingsType == SettingFlowType.SHIPPING_ADDRESS){
            mNavController.navigate(SettingsFragmentDirections.actionSettingsFragmentToEditShippingAddressFragment(true))
        }
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
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
            // If we moving directly to Settings Shipping Fragment
        if (mNavController.currentDestination?.id==R.id.editShippingAddressFragment && getSettingFlowType()== SettingFlowType.SHIPPING_ADDRESS){
            finish()
            return false
        }else{
            // Finishing when root fragment is Setting fragment
            if (mNavController.currentDestination?.id==R.id.settingsFragment){
                finish()

            }else{// Popup fragments from Activity
                NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
            }
          return  true
        }
    }

    fun setToolbarTitle(title: String) {
        mViewBinding.toolbarTitle.text = title
    }

    private fun setUpNavGraph() { //Setting NavGraph manually so that we can pass data to start destination
        findNavController(R.id.nav_host_fragment)
            .setGraph(
                R.navigation.settings_nav_graph
            )
    }


    internal fun getSettingFlowType(): SettingFlowType = mSettingsType

    companion object {

        const val GOTO :String ="goto"

        fun getInstance(context: Context, goTo: SettingFlowType =SettingFlowType.SETTING ) = Intent(context, SettingsActivity::class.java).apply{
            putExtra(GOTO,goTo)
        }
    }

    override fun onBackPressed() {
        if (mNavController.currentDestination?.id==R.id.editShippingAddressFragment && getSettingFlowType()== SettingFlowType.SHIPPING_ADDRESS){
            finish()
        }else{
            super.onBackPressed()
        }
    }

}