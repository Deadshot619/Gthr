package com.gthr.gthrcollect.ui.homebottomnav.signin

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.SignInFlowRepository
import com.gthr.gthrcollect.databinding.LayoutSignUpHeaderBinding
import com.gthr.gthrcollect.databinding.SignInFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.forgotpassword.ForgotPasswordActivity
import com.gthr.gthrcollect.ui.settings.SettingsActivity
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEmailEditText
import com.gthr.gthrcollect.utils.customviews.CustomPasswordEditText
import com.gthr.gthrcollect.utils.extensions.isValidEmail
import com.gthr.gthrcollect.utils.extensions.isValidPassword
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger

class SignInFragment : BaseFragment<SignInViewModel, SignInFragmentBinding>() {

    private val TAG: String = this.javaClass.name

    private val repository = SignInFlowRepository()

    override fun getViewBinding() = SignInFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SignInViewModel by viewModels { SignInViewModelFactory(repository) }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mLayoutHeader: LayoutSignUpHeaderBinding
    private lateinit var mTvCreateAccount: TextView
    private lateinit var mCetEmail: CustomEmailEditText
    private lateinit var mCetPassword: CustomPasswordEditText
    private lateinit var mBtnSignIn: CustomAuthenticationButton
    private lateinit var mTvForgotPassword: TextView

    val db = Firebase.firestore

    override fun onBinding() {
        mAuth = Firebase.auth
        initViews()

        //Title
        mLayoutHeader.tvTitle.text = getString(R.string.sign_in_text)

        setUpListeners()
        setUpObservers()

        mCetEmail.mEtEmail.setText("abc@gmail.com")
        mCetPassword.mEtPassword.setText("Abc@12345")
    }

    private fun initViews() {
        mViewBinding.run {
            mLayoutHeader = layoutHeader
            mTvCreateAccount = tvCreateAccount
            mCetEmail = cetEmail
            mCetPassword = cetPassword
            mBtnSignIn = btnSignIn
            mTvForgotPassword = tvForgotPassword
            initProgressBar(layoutProgress)
        }
    }

    private fun setUpListeners() {
        mBtnSignIn.setOnClickListener {
            if (validate()) {
                val email = mCetEmail.mEtEmail.text.toString().trim()
                val password = mCetPassword.mEtPassword.text.toString().trim()
                mViewModel.signIn(email, password)
            }
        }

        mTvCreateAccount.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInToSignUpFragment())
        }

        mTvForgotPassword.setOnClickListener {
            startActivity(ForgotPasswordActivity.getInstance(requireContext()))
        }
    }

    private fun setUpObservers() {
        mViewModel.userInfo.observe(viewLifecycleOwner, {
            when (it) {
                is State.Loading -> {
                    showProgressBar()
                }
                is State.Success -> {
                    showProgressBar(false)
                    GthrCollect.prefs?.signedInUser = it.data
                    startActivity(SettingsActivity.getInstance(requireContext()))
                }
                is State.Failed -> {
                    showProgressBar(false)
                    showToast(it.message)
                }
            }
        })
    }

    private fun validate(): Boolean {
        var isValid = true
        if (!mCetEmail.mEtEmail.text.toString().trim().isValidEmail()) {
            mCetEmail.showError(getString(R.string.enter_valid_email_error_text))
            isValid = false
        } else
            mCetEmail.hideError()

        if (!mCetPassword.mEtPassword.text.toString().trim().isValidPassword()) {
            mCetPassword.showError(getString(R.string.enter_valid_password_error_text))
            isValid = false
        } else
            mCetPassword.hideError()

        return isValid
    }

    private fun authUser() {
        val email = mCetEmail.mEtEmail.text.toString().trim()
        val password = mCetPassword.mEtPassword.text.toString().trim()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    GthrLogger.d(TAG, "signInWithEmail:success")

                    val user = mAuth.currentUser

                    activity?.showToast(getString(R.string.sign_successfully))
                    startActivity(SettingsActivity.getInstance(requireContext()))

                    //     fetchDataFromFirestore(user!!.uid)

                    //    getUserDataUsingUID("E1DmUzgkX4e9RaGxiBmAPxtRO0H3")

                    //    addDataToFirestore("quuiGSKVZMQkyo492znegJYYNlG2")

                    //     updateDataWithUID("quuiGSKVZMQkyo492znegJYYNlG2")

                    //   addDataToFirestore(user!!.uid)

                    //    updateData()

                    //      deleteData()
                } else {
                    // If sign in fails, display a message to the user.
                    GthrLogger.w(TAG, "signInWithEmail:failure" + task.exception)

                    activity?.showToast(getString(R.string.auth_fail))
                }
            })
    }

    private fun fetchDataFromFirestore(uid: String) {
        db.collection("userInfo")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    GthrLogger.d(TAG, "${document.id} => ${document.data}")
                    GthrLogger.e("document", document.data["collectionID"].toString())
                    GthrLogger.e("document", document.id.toString())
                    GthrLogger.e("document", document.data.toString())
                }
            }
            .addOnFailureListener { exception ->
                GthrLogger.w(TAG, "Error getting documents." + exception)
            }
    }

    private fun getUserDataUsingUID(uid: String) {
        db.collection("userInfo").document(uid).get().addOnSuccessListener {
            GthrLogger.e("name", it.data.toString())
        }
    }

    private fun updateDataWithUID(uid: String) {
        var address: ArrayList<String> = ArrayList()

        address.add("Address 1")
        address.add("Address 2")
        address.add("Address 3")

        val user = hashMapOf(
            "first" to "Shubham",
            "last" to "Khandelwal",
            "born" to 2022,
            "uid" to uid,
            "address" to address
        )

        db.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {

                activity?.showToast("successfully written!")


                GthrLogger.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                GthrLogger.w(TAG, "Error writing document")

                activity?.showToast(it.message!!)
            }
    }

    private fun updateData() {
        val option = db.collection("users").document("EL130o4XxJYLopOJNwrS")
            .update("last", "EL130o4XxJYLopOJNwrS")

        activity?.showToast(option.isSuccessful.toString())
    }

    private fun deleteData() {
        db.collection("users").document("EL130o4XxJYLopOJNwrS").delete()
    }
}