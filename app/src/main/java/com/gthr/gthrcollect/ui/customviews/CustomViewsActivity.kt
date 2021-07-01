package com.gthr.gthrcollect.ui.customviews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gthr.gthrcollect.databinding.ActivityCustomViewsBinding

class CustomViewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.enableAuthButton()
        binding.btn.text = "text"

        binding.customOtp1.setInitial()
        binding.customOtp2.setSuccess()
        binding.customOtp3.setError("Please Enter otp!")




        binding.btnMain.setOnClickListener {
            if (binding.etCustomEmail.isErrorEnabled)
                binding.etCustomEmail.hideError()
            else
                binding.etCustomEmail.showError("Please Enter email")

            if (binding.etCustomPassword.isErrorEnabled) {
                binding.etCustomPassword.hideError()
            }
            else{
                binding.etCustomPassword.showError("Please Enter password")
            }
        }


    }


}