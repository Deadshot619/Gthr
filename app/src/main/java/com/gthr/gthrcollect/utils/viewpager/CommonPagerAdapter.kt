package com.gthr.gthrcollect.utils.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gthr.gthrcollect.ui.profile.follow.FollowFragment

class CommonPagerAdapter(fragment: Fragment, val views: List<Pair<String, Fragment>>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = views.size
    override fun createFragment(position: Int): Fragment = views[position].second
}