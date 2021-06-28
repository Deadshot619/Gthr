package com.gthr.gthrcollect.ui.splash

import androidx.activity.viewModels
import com.gthr.gthrcollect.databinding.ActivitySplashBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {
    override val mViewModel: SplashViewModel by viewModels()
    override fun getViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    private val mainScope = MainScope()

    override fun onBinding() {
        mViewBinding.lifecycleOwner = this
    }

    override fun onResume() {
        super.onResume()
        pauseScreen()
    }

    private fun pauseScreen() {
        mainScope.launch {
            ticker(PAUSE_DURATION).receive()
            goToHomeScreen()
        }
    }

    private fun goToHomeScreen() {
        startActivity(EditAccountInfoActivity.getInstance(this))
        finish()
    }

    companion object {
        private const val PAUSE_DURATION = 3000L
    }
}