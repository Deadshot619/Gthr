package com.gthr.gthrcollect.ui.customviews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gthr.gthrcollect.databinding.ActivityCustomViewsBinding
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.isValidEmail

class CustomViewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setAuthBtnEnable()
        binding.btn.setAuthBtnText("text")

        binding.btnMain.setOnClickListener {
            if (binding.etCustomEmail.isErrorEnabled)
                binding.etCustomEmail.hideError()
            else
                binding.etCustomEmail.showError("Holaaaaa")

            if (binding.etCustomPassword.isErrorEnabled) {
                binding.etCustomPassword.hideError()
            }
            else{
                binding.etCustomPassword.showError("Holaaaaa")
            }

        }


    }


}