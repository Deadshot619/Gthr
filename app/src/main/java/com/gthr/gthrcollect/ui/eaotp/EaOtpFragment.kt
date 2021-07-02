package com.gthr.gthrcollect.ui.eaotp

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
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

        mPinView.mPinView.doOnTextChanged { text, start, before, count ->
        }

        mPinView.mPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 6) {
                    mPinView.mPinView.setText("")
                    findNavController().navigate(EaOtpFragmentDirections.actionEaOtpFragmentToEaIdVerificationFragment())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}