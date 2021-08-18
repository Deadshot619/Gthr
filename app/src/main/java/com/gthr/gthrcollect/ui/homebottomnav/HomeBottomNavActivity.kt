package com.gthr.gthrcollect.ui.homebottomnav

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ActivityHomeBottomNavBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.homebottomnav.search.SearchFragmentArgs
import com.gthr.gthrcollect.utils.enums.ProductCategoryFilter
import com.gthr.gthrcollect.utils.enums.ProductSortFilter
import com.gthr.gthrcollect.utils.enums.SearchType

class HomeBottomNavActivity : BaseActivity<HomeBottomNavViewModel, ActivityHomeBottomNavBinding>() {
    override val mViewModel: HomeBottomNavViewModel by viewModels()
    override fun getViewBinding() = ActivityHomeBottomNavBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mNavHostFragment: NavHostFragment
    private lateinit var mBottomNavView: BottomNavigationView

    override fun onBinding() {
        initViews()
        initBottomView()
    }

    private fun initViews(){
        mNavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = mNavHostFragment.navController
        mBottomNavView = mViewBinding.bnvHome
    }

    private fun initBottomView(){
        mBottomNavView.setupWithNavController(mNavController)
        mBottomNavView.menu.getItem(2).isChecked = true
    }

    fun goToSearch(type : SearchType,sortFilter : ProductSortFilter,categoryFilter : ProductCategoryFilter){
        findNavController(R.id.nav_host_fragment).navigate(R.id.searchFragment, SearchFragmentArgs(type = type,sortFilter =sortFilter,categoryFilter = categoryFilter).toBundle())
    }

    companion object {
        fun getInstance(context: Context) = Intent(context, HomeBottomNavActivity::class.java)
    }
}