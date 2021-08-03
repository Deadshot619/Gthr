package com.gthr.gthrcollect.utils.extensions

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout

/**
 * Method to set typeface of given TabLayout
 */
fun TabLayout.setStyleOfTabLayout(tab: TabLayout.Tab, @FontRes fontFamilyRes: Int){
    val tabLayout = (getChildAt(0) as ViewGroup).getChildAt(tab.position) as LinearLayout
    val tabText = (tabLayout.getChildAt(1) as TextView)
    val typeface = ResourcesCompat.getFont(this.context, fontFamilyRes)
    tabText.typeface = typeface
}