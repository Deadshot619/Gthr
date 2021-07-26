package com.gthr.gthrcollect.ui.homebottomnav.account

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gthr.gthrcollect.ui.profile.follow.FollowFragment

class AccountPagerAdapter(fragment: Fragment, val views: List<Pair<String, Fragment>>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = views.size
    override fun createFragment(position: Int): Fragment = views[position].second
}