package com.gthr.gthrcollect.ui.homebottomnav

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ActivityHomeBottomNavBinding
import com.gthr.gthrcollect.ui.base.BaseActivity

class HomeBottomNavActivity : BaseActivity<HomeBottomNavViewModel, ActivityHomeBottomNavBinding>() {
    override val mViewModel: HomeBottomNavViewModel by viewModels()
    override fun getViewBinding() = ActivityHomeBottomNavBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mNavHostFragment: NavHostFragment

    override fun onBinding() {
        mNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = mNavHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bnv_home).setupWithNavController(mNavController)
    }

    companion object {
        fun getInstance(context: Context) = Intent(context, HomeBottomNavActivity::class.java)
    }
}