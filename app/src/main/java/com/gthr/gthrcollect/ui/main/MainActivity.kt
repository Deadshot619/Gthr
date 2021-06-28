package com.gthr.gthrcollect.ui.main

import com.gthr.gthrcollect.databinding.ActivityMainBinding
import com.gthr.gthrcollect.ui.base.BaseActivity

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val mViewModel: MainViewModel = MainViewModel()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
}