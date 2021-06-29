package com.gthr.gthrcollect.ui.customviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import com.gthr.gthrcollect.databinding.ActivityCustomViewsBinding
import com.gthr.gthrcollect.ui.customviews.ValidationHelper.isValidEmail
import com.gthr.gthrcollect.ui.customviews.ValidationHelper.isValidPassword
import com.gthr.gthrcollect.utils.extensions.afterTextChanged

class CustomViewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMain.setOnClickListener {

            if(!isValidEmail(binding.etEmail.text.toString())){
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
                        !isValidEmail(binding.etEmail.text.toString())
                    )
                }
            }

            if(!isValidPassword(binding.etPassword.text.toString())){
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
                        !isValidPassword(binding.etPassword.text.toString())
                    )
                }
            }




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