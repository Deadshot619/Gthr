package com.gthr.gthrcollect.ui.customviews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gthr.gthrcollect.databinding.ActivityCustomViewsBinding
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.isValidEmail
import com.gthr.gthrcollect.utils.extensions.isValidPassword

class CustomViewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMain.setOnClickListener {
            if (binding.etCustomEmail.isErrorEnabled)
                binding.etCustomEmail.hideError()
            else
                binding.etCustomEmail.showError("Holaaaaa")

            if (!binding.etEmail.text.toString().isValidEmail()) {
                CustomViewHelper.setErrorEmail(
                    binding.llEmail,
                    binding.ivEmail,
                    binding.etEmail,
                    true
                )
                binding.etEmail.afterTextChanged {
                    CustomViewHelper.setErrorEmail(
                        binding.llEmail,
                        binding.ivEmail,
                        binding.etEmail,
                        !binding.etEmail.text.toString().isValidEmail()
                    )
                }
            }

            if (!binding.etPassword.text.toString().isValidPassword()) {
                CustomViewHelper.setErrorPassword(
                    binding.llPassword,
                    binding.tilPassword,
                    binding.ivPassword,
                    binding.etPassword,
                    true
                )
                binding.etPassword.afterTextChanged {
                    CustomViewHelper.setErrorPassword(
                        binding.llPassword,
                        binding.tilPassword,
                        binding.ivPassword,
                        binding.etPassword,
                        !binding.etPassword.text.toString().isValidPassword()
                    )
                }
            }

            /* binding.etCustomEmail.mEtEmail.addTextChangedListener(object : TextWatcher{
                 override fun beforeTextChanged(
                     s: CharSequence?,
                     start: Int,
                     count: Int,
                     after: Int
                 ) {}

                 override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                     showToast("hello")

                     if (s.isNullOrEmpty())
                         binding.etCustomEmail.hideError()
                     else
                         binding.etCustomEmail.showError("HOLA")
                 }

                 override fun afterTextChanged(s: Editable?) {}

             })*/

        }




//        binding.llEmail.isSelected = true
//        binding.ivEmail.isSelected = true
//        binding.etEmail.isSelected = true

//        binding.llErrorEmail.isSelected = false
//        binding.ivErrorEmail.isSelected = false
//        binding.etErrorEmail.isSelected = false
//        binding.etErrorEmail.error = "Please enter email."


//        binding.llPassword.isSelected = true
//        binding.ivPassword.isSelected = true
//        binding.etPassword.isSelected = true


//        binding.tilErrorPassword.endIconMode = TextInputLayout.END_ICON_NONE
//        binding.llErrorPassword.isSelected = false
//        binding.ivErrorPassword.isSelected = false
//        binding.etErrorPassword.isSelected = false
//        binding.etErrorPassword.error = "Please enter email."
//
        binding.etDisplayNameWithIcon.isSelected = true
        binding.etDisplayNameFalse.isSelected = true
        binding.etDisplayNameTrue.isSelected = false

        binding.etDisplayNameWithIcon.afterTextChanged{

        }

    }




}