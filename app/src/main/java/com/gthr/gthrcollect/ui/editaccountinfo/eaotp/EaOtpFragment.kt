package com.gthr.gthrcollect.ui.editaccountinfo.eaotp

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.GthrCollect.Companion.prefs
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.EditAccountInfoRepository
import com.gthr.gthrcollect.databinding.EaOtpFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.mapper.toCollectionInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toRealtimeDatabaseModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoViewModel
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoViewModelFactory
import com.gthr.gthrcollect.utils.customviews.CustomPinView
import com.gthr.gthrcollect.utils.extensions.hideKeyboard
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.extensions.updateCollectionInfoModelData
import com.gthr.gthrcollect.utils.extensions.updateUserInfoModelData
import com.gthr.gthrcollect.utils.logger.GthrLogger
import java.util.concurrent.TimeUnit


class EaOtpFragment : BaseFragment<EditAccountInfoViewModel, EaOtpFragmentBinding>() {

    private val TAG: String = this.javaClass.name

    private val repository = EditAccountInfoRepository()

    override fun getViewBinding() = EaOtpFragmentBinding.inflate(layoutInflater)
    override val mViewModel: EditAccountInfoViewModel by activityViewModels {
        EditAccountInfoViewModelFactory(
            repository
        )
    }

    private val args by navArgs<EaOtpFragmentArgs>()

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken
    private var storedVerificationId: String? = ""

    private lateinit var mTvPhoneNo: TextView
    private lateinit var mTvSendAgain: TextView
    private lateinit var mPinView: CustomPinView

    private lateinit var mPhoneNumber: String

    override fun onBinding() {
        this.mAuth = Firebase.auth
        initViews()
        setUpClickListeners()
        setUpObservers()

        mPhoneNumber = args.phoneNumber/*TEST_PHONE_NUMBER*/
        mTvPhoneNo.text = mPhoneNumber
        startPhoneNumberVerification(mPhoneNumber)
    }

    private fun initViews() {
        mViewBinding.run {
            mTvPhoneNo = tvPhoneNo
            mTvSendAgain = tvSendAgain
            mPinView = cpnPinView
            initProgressBar(layoutProgress)
        }
    }

    private fun setUpClickListeners() {
        mTvSendAgain.setOnClickListener {
            resendVerificationCode(mPhoneNumber, mResendToken)
        }

        mPinView.mPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 6) {
                    mPinView.mPinView.setText("")
                    requireContext().hideKeyboard(mViewBinding.root)

                    verifyPhoneNumberWithCode(storedVerificationId, s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setUpObservers() {
        mViewModel.otpVerified.observe(viewLifecycleOwner, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        mPinView.mPinView.setText("")

                        val user = GthrCollect.prefs?.signUpCred
                        user?.let {
                            mViewModel.signUp(it.email, it.password)
                        }
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                    }
                }
            }
        })

        mViewModel.userInfo.observe(viewLifecycleOwner, {
            when (it) {
                is State.Loading -> {
                    showProgressBar()
                }
                is State.Success -> {
                    showProgressBar(false)
                    prefs?.signedInUser = it.data
                    mViewModel.addCollectionInfoModel(mViewModel.userInfoLiveData.value!!)
                }
                is State.Failed -> {
                    showProgressBar(false)
                    showToast(it.message)
                }
            }
        })

        mViewModel.userCollectionInfoModelKey.observe(viewLifecycleOwner, {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        mViewModel.addUserDataFirestore(mViewModel.userInfoLiveData.value!!.apply {
                            this.collectionId = it.data
                        }, it.data)
                        prefs?.updateCollectionInfoModelData(mViewModel.userInfoLiveData.value!!.toRealtimeDatabaseModel().toCollectionInfoDomainModel())
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        })

        mViewModel.userDataAddedFirestore.observe(viewLifecycleOwner, {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        findNavController().navigate(EaOtpFragmentDirections.actionEaOtpFragmentToEaIdVerificationFragment())
                        prefs?.updateUserInfoModelData(mViewModel.userInfoLiveData.value!!)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        })
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        verificationId?.let {
            val credential = PhoneAuthProvider.getCredential(it, code)
            mViewModel.verifyOtp(credential)
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(TIMEOUT_PHONE_NO_VERIFICATION, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        showProgressBar()
        activity?.showToast(getString(R.string.sending_otp))
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(TIMEOUT_PHONE_NO_VERIFICATION, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())

        showProgressBar()
        activity?.showToast(getString(R.string.resend_otp))
    }

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.

            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            showProgressBar(false)
            GthrLogger.d(TAG, "onVerificationCompleted:$credential")
            mViewModel.verifyOtp(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            showProgressBar(false)
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    // Invalid request
                    showToast(getString(R.string.invalid_request))
                }
                is FirebaseTooManyRequestsException -> {
                    showToast(getString(R.string.sms_limit_exceed))
                    // The SMS quota for the project has been exceeded
                }
                else -> {
                    showToast(e.message.toString())
                    GthrLogger.i("Firebaseee", e.message.toString())
                }
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            GthrLogger.d(TAG, "onCodeSent:$verificationId")
            showToast(getString(R.string.otp_sent))
            storedVerificationId = verificationId
            mResendToken = token
            showProgressBar(false)
            // Save verification ID and resending token so we can use them later
        }
    }

    companion object {
        private const val TIMEOUT_PHONE_NO_VERIFICATION = 60L
        private const val TEST_PHONE_NUMBER = "+919999999999"
    }
}