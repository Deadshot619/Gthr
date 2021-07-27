package com.gthr.gthrcollect.ui.homebottomnav.account

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AccountFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.follow.FollowFragment
import com.gthr.gthrcollect.ui.profile.my_profile.MyProfileFragment
import com.gthr.gthrcollect.ui.profile.reciepts.ReceiptsFragment
import com.gthr.gthrcollect.ui.settings.SettingsActivity

class AccountFragment : BaseFragment<AccountViewModel, AccountFragmentBinding>() {
    override val mViewModel: AccountViewModel by viewModels()
    override fun getViewBinding() = AccountFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvSettings: AppCompatImageView
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager2
    private lateinit var mAdapter: AccountPagerAdapter
    private lateinit var mViews: List<Pair<String, Fragment>>

    override fun onBinding() {
        initViews()
        initClickListeners()
        initViewPager()
    }

    private fun initViews() {
        mViewBinding.run {
            mIvSettings = ivSettings
            mTabLayout = tabLayout
            mViewPager = viewPager
            mViews = listOf(
                Pair(getString(R.string.title_my_profile), MyProfileFragment()),
                Pair(getString(R.string.title_receipts), ReceiptsFragment())
            )
            mAdapter = AccountPagerAdapter(this@AccountFragment, mViews)
        }
    }

    private fun initClickListeners() {
        mIvSettings.setOnClickListener {
            startActivity(SettingsActivity.getInstance(requireContext()))
        }
    }

    private fun initViewPager() {
        mViewPager.adapter = mAdapter

        TabLayoutMediator(mTabLayout, mViewPager) { tab, pos ->
            tab.text = mViews[pos].first
            mViewPager.setCurrentItem(tab.position, true)
        }.attach()

        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {  setStyleOfTabLayout(it, R.font.lato_bold) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {  setStyleOfTabLayout(it, R.font.lato) }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    fun setStyleOfTabLayout(tab: TabLayout.Tab, @FontRes fontFamilyRes: Int){
        val tabLayout = (mTabLayout.getChildAt(0) as ViewGroup).getChildAt(tab.position) as LinearLayout
        val tabText = (tabLayout.getChildAt(1) as TextView)
        val typeface = ResourcesCompat.getFont(requireContext(), fontFamilyRes)
        tabText.typeface = typeface
    }
}