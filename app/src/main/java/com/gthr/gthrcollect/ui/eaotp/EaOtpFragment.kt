package com.gthr.gthrcollect.ui.eaotp

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.EaOtpFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomPinView

class EaOtpFragment : BaseFragment<EaOtpViewModel, EaOtpFragmentBinding>() {

    override fun getViewBinding() = EaOtpFragmentBinding.inflate(layoutInflater)
    override val mViewModel: EaOtpViewModel by viewModels()

    private lateinit var mTvSendAgain: TextView
    private lateinit var mPinView: CustomPinView

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mTvSendAgain = tvSendAgain
            mPinView = cpnPinView
        }
    }

    private fun setUpClickListeners() {
        mTvSendAgain.setOnClickListener {
            findNavController().navigate(EaOtpFragmentDirections.actionEaOtpFragmentToEaIdVerificationFragment())
        }
    }
}