package com.gthr.gthrcollect.ui.homebottomnav.account

import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AccountFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.my_profile.MyProfileFragment
import com.gthr.gthrcollect.ui.profile.reciepts.ReceiptsFragment
import com.gthr.gthrcollect.ui.settings.SettingsActivity
import com.gthr.gthrcollect.utils.extensions.setStyleOfTabLayout
import com.gthr.gthrcollect.utils.viewpager.CommonPagerAdapter

class AccountFragment : BaseFragment<AccountViewModel, AccountFragmentBinding>() {
    override val mViewModel: AccountViewModel by viewModels()
    override fun getViewBinding() = AccountFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvSettings: AppCompatImageView
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager2
    private lateinit var mAdapter: CommonPagerAdapter
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
            mAdapter = CommonPagerAdapter(this@AccountFragment, mViews)
        }
    }

    private fun initClickListeners() {
        mIvSettings.setOnClickListener {
            startActivity(SettingsActivity.getInstance(requireContext()))
        }
    }

    private fun initViewPager() {
        mViewPager.adapter = mAdapter

        mViewPager.isUserInputEnabled = false
        TabLayoutMediator(mTabLayout, mViewPager) { tab, pos ->
            tab.text = mViews[pos].first
            mViewPager.setCurrentItem(tab.position, true)
        }.attach()

        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {  mTabLayout.setStyleOfTabLayout(it, R.font.lato_bold) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {  mTabLayout.setStyleOfTabLayout(it, R.font.lato) }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}