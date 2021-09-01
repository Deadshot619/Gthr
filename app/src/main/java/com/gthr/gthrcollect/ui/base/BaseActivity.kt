package com.gthr.gthrcollect.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.gthr.gthrcollect.databinding.LayoutProgressBarBinding
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

/**
 * Abstract Activity which binds [ViewModel] [VM] and [ViewBinding] [VB]
 */
abstract class BaseActivity<VM : ViewModel, VB : ViewBinding> : AppCompatActivity() {

    protected abstract val mViewModel: VM

    protected lateinit var mViewBinding: VB

    private var mProgressBar: LayoutProgressBarBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = getViewBinding()
        setContentView(mViewBinding.root)

        onBinding()
    }

    /**
     * It returns [VB] which is assigned to [mViewBinding] and used in [onCreate]
     */
    abstract fun getViewBinding(): VB

    abstract fun onBinding()

    /**
     * Method to initialize Progress bar layout.
     * Must be initialized at the start of Activity/Fragment
     */
    fun initProgressBar(progressBarLayout: LayoutProgressBarBinding) {
        mProgressBar = progressBarLayout
    }

    /**
     * Method to show/hide the progress bar
     */
    fun showProgressBar(isShown: Boolean = true) {
        mProgressBar?.run {
            if (isShown) {
                llProgressLayout.visible()
                pbProgressBar.visible()
            } else {
                llProgressLayout.gone()
                pbProgressBar.gone()
            }
        }
    }
}
