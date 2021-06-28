package com.gthr.gthrcollect.ui.main

import android.content.Context
import android.content.Intent
import com.gthr.gthrcollect.databinding.ActivityMainBinding
import com.gthr.gthrcollect.ui.base.BaseActivity

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val mViewModel: MainViewModel = MainViewModel()
    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    override fun onBinding() {}

    companion object {
        fun getInstance(context: Context) = Intent(context, MainActivity::class.java)
    }
}