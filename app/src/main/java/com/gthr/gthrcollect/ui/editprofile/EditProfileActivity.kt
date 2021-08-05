package com.gthr.gthrcollect.ui.editprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ActivityEditProfileBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.profile.editprofile.EditProfileFragment
import com.gthr.gthrcollect.ui.settings.SettingsActivity

class EditProfileActivity : BaseActivity<EditProfileViewModel, ActivityEditProfileBinding>() {

    override val mViewModel: EditProfileViewModel by viewModels()
    override fun getViewBinding() = ActivityEditProfileBinding.inflate(layoutInflater)

    lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    override fun onBinding() {
        setSupportActionBar(mViewBinding.toolbar)
        setUpNavigationAndActionBar()

        if (intent != null) {
            val bundle = Bundle()
            bundle.putString(EditProfileFragment.ABOUT_KEY, aboutt)
            bundle.putString(EditProfileFragment.D_NAME_KEY, display_namee)
            bundle.putString(EditProfileFragment.PROFILE_URL, profile_URL)
            mNavController.setGraph(mNavController.graph, bundle)
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
                R.id.editProfileFragment -> {
                    setToolbarTitle(getString(R.string.title_edit_profile_info))
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

    private fun setToolbarTitle(title: String) {
        mViewBinding.toolbarTitle.text = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings)
            startActivity(SettingsActivity.getInstance(this))
        return super.onOptionsItemSelected(item)
    }

    companion object {
        var aboutt: String = ""
        var display_namee: String = ""
        var profile_URL: String = ""

        fun getInstance(context: Context, display_name: String, about: String, imageURl: String) =
            Intent(context, EditProfileActivity::class.java).apply {
                aboutt = about
                display_namee = display_name
                profile_URL=imageURl
            }
    }
}