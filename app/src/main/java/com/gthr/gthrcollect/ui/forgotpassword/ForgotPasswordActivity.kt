package com.gthr.gthrcollect.ui.forgotpassword

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
import com.gthr.gthrcollect.databinding.ActivityForgotPasswordBinding
import com.gthr.gthrcollect.ui.base.BaseActivity

class ForgotPasswordActivity :
    BaseActivity<ForgotPasswordViewModel, ActivityForgotPasswordBinding>() {
    override val mViewModel: ForgotPasswordViewModel by viewModels()
    override fun getViewBinding() = ActivityForgotPasswordBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration


    override fun onBinding() {
        setSupportActionBar(mViewBinding.toolbar)
        setUpNavigationAndActionBar()
    }

    private fun setUpNavigationAndActionBar() {
        mNavController = this.findNavController(R.id.nav_host_fragment)
        mAppBarConfiguration = /*AppBarConfiguration(navController.graph)*/
            AppBarConfiguration.Builder().build()

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration)

        mNavController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            mViewBinding.toolbar.title = ""     //Set Title as empty as we have used custom title
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_up_button) //Set up button as <
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

    companion object {
        fun getInstance(context: Context) = Intent(context, ForgotPasswordActivity::class.java)
    }
}