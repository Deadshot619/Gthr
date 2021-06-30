package com.gthr.gthrcollect.ui.customviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import com.gthr.gthrcollect.databinding.ActivityCustomViewsBinding
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.isValidPassword

class CustomViewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnMain.setOnClickListener {
//
//            binding.customEmail.addValidation()
//
//            binding.customPassword.addValidation()
//
//
//        }

        binding.etDisplayNameWithIcon.isSelected = true
        binding.etDisplayNameFalse.isSelected = true
        binding.etDisplayNameTrue.isSelected = false

        binding.etDisplayNameWithIcon.afterTextChanged{

        }

    }




}