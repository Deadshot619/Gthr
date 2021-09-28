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
import com.gthr.gthrcollect.ui.productdetail.ProductDetailActivity
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.utils.enums.*

class HomeBottomNavActivity : BaseActivity<HomeBottomNavViewModel, ActivityHomeBottomNavBinding>() {
    override val mViewModel: HomeBottomNavViewModel by viewModels()
    override fun getViewBinding() = ActivityHomeBottomNavBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mNavHostFragment: NavHostFragment
    private lateinit var mBottomNavView: BottomNavigationView

    override fun onBinding() {
        initViews()
        initBottomView()

        if(intent.hasExtra(KEY_OBJECT_ID)){
            val objectID = intent.getStringExtra(KEY_OBJECT_ID)
            val mProductType = intent.getSerializableExtra(KEY_PRODUCT_TYPE) as ProductType
            startActivity(ProductDetailActivity.getInstance(this,objectID,mProductType))
            goToSearch(SearchType.PRODUCT,ProductSortFilter.NONE,ProductCategoryFilter.NONE)
        }
        else if (intent.hasExtra(KEY_COLLECTION_ID)) {
            val collectionID = intent.getStringExtra(KEY_COLLECTION_ID)
            startActivity(
                ProfileActivity.getInstance(
                    this,
                    ProfileNavigationType.PROFILE,
                    collectionID!!
                )
            )
        } else if (intent.hasExtra(KEY_PROFILE_PAGE)) {
            if (intent.getBooleanExtra(KEY_PROFILE_PAGE, false))
                goToProfileSignUp()
        }
    }

    private fun initViews(){
        mNavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = mNavHostFragment.navController
        mBottomNavView = mViewBinding.bnvHome
    }

    private fun initBottomView() {
        mBottomNavView.setupWithNavController(mNavController)
        mBottomNavView.menu.getItem(0).isChecked = true
    }

    fun goToSearch(
        type: SearchType,
        sortFilter: ProductSortFilter,
        categoryFilter: ProductCategoryFilter
    ) {
        mBottomNavView.menu.getItem(1).isChecked = true
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.searchFragment,
            SearchFragmentArgs(
                type = type,
                sortFilter = sortFilter,
                categoryFilter = categoryFilter
            ).toBundle()
        )
    }

    fun goToProfileSignUp() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.signInFragment)
        mBottomNavView.menu.getItem(2).isChecked = true
    }

    companion object {

        private const val KEY_PRODUCT_TYPE = "key_product_type"
        private const val KEY_OBJECT_ID = "key_object_id"
        private const val KEY_COLLECTION_ID = "key_collection_id"
        private const val KEY_PROFILE_PAGE = "key_profile_page"

        fun getInstance(context: Context) = Intent(context, HomeBottomNavActivity::class.java)
        fun getInstance(context: Context, objectID: String, productType: ProductType) =
            Intent(context, HomeBottomNavActivity::class.java).apply {
                putExtra(KEY_PRODUCT_TYPE, productType)
                putExtra(KEY_OBJECT_ID, objectID)
            }

        fun getInstance(context: Context, collectionId: String) =
            Intent(context, HomeBottomNavActivity::class.java).apply {
                putExtra(KEY_COLLECTION_ID, collectionId)
            }

        fun getInstance(context: Context, goToProfileSignUp: Boolean) =
            Intent(context, HomeBottomNavActivity::class.java).apply {
                putExtra(KEY_PROFILE_PAGE, goToProfileSignUp)
            }
    }
}