package com.gthr.gthrcollect.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ActivityProfileBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.profile.navigation.ProfileNavigationFragmentArgs
import com.gthr.gthrcollect.utils.enums.EditAccountSection
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class ProfileActivity : BaseActivity<ProfileViewModel, ActivityProfileBinding>() {

    override val mViewModel: ProfileViewModel by viewModels()
    override fun getViewBinding() = ActivityProfileBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mType: ProfileNavigationType
    private lateinit var mToolbar: Toolbar

    override fun onBinding() {
        mType = intent.getSerializableExtra(KEY_TYPE) as ProfileNavigationType

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
                R.navigation.profile_nav_graph,
                ProfileNavigationFragmentArgs(type = mType).toBundle()
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
                R.id.myProfileFragment -> {
                    setToolbarTitle("")
                    mToolbar.background = getImageDrawable(R.drawable.toolbar_square_bg)
                }
                else -> {
                    mToolbar.background = getImageDrawable(R.drawable.toolbar_bg)
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
        private const val KEY_TYPE = "type"

        fun getInstance(context: Context, type: ProfileNavigationType) =
            Intent(context, ProfileActivity::class.java).apply {
                putExtra(KEY_TYPE, type)
            }
    }
}