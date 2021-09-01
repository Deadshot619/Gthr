package com.gthr.gthrcollect.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.gthr.gthrcollect.databinding.LayoutProgressBarBinding
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

abstract class BaseFragment<VM : ViewModel, VB : ViewBinding> : Fragment() {

    protected abstract val mViewModel: VM

    protected lateinit var mViewBinding: VB

    private var mProgressBar: LayoutProgressBarBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewBinding = getViewBinding()

        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBinding()
    }

    abstract fun onBinding()

    /**
     * It returns [VB] which is assigned to [mViewBinding] and used in [onCreate]
     */
    abstract fun getViewBinding(): VB

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