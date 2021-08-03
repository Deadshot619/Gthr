package com.gthr.gthrcollect.ui.homebottomnav.bottomfeed

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.BottomFeedFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavViewModel
import com.gthr.gthrcollect.ui.homebottomnav.feed.FeedFragment
import com.gthr.gthrcollect.ui.homebottomnav.market.MarketFragment
import com.gthr.gthrcollect.utils.viewpager.CommonPagerAdapter
import com.gthr.gthrcollect.ui.profile.my_profile.MyProfileFragment
import com.gthr.gthrcollect.ui.profile.reciepts.ReceiptsFragment
import com.gthr.gthrcollect.utils.extensions.setStyleOfTabLayout

class BottomFeedFragment : BaseFragment<HomeBottomNavViewModel, BottomFeedFragmentBinding>() {

    override val mViewModel: HomeBottomNavViewModel by viewModels()
    override fun getViewBinding() = BottomFeedFragmentBinding.inflate(layoutInflater)

    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager2

    private lateinit var mAdapter: CommonPagerAdapter
    private lateinit var mViews: List<Pair<String, Fragment>>

    override fun onBinding() {
        initViews()
        initViewPager()
    }

    private fun initViews() {
        mViewBinding.run {
            mTabLayout = tabLayout
            mViewPager = viewPager
            mViews = listOf(
                Pair(getString(R.string.title_market), MarketFragment()),
                Pair(getString(R.string.menu_feed_title), FeedFragment())
            )
            mAdapter = CommonPagerAdapter(this@BottomFeedFragment, mViews)
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
                tab?.let {  mTabLayout.setStyleOfTabLayout(it, R.font.lato_bold) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {  mTabLayout.setStyleOfTabLayout(it, R.font.lato) }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}