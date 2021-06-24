package com.example.samplebasestructure.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.samplebasestructure.R
import com.example.samplebasestructure.databinding.ActivityMainBinding
import com.example.samplebasestructure.ui.base.BaseActivity

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val mViewModel: MainViewModel = MainViewModel()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
}