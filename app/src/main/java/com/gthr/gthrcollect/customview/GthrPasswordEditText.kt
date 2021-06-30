package com.gthr.gthrcollect.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.textfield.TextInputLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.GthrPasswordEditTextBinding
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.isValidPassword

@SuppressLint("Recycle", "CustomViewStyleable")
class GthrPasswordEditText(context : Context, attrs : AttributeSet):  LinearLayoutCompat(context,attrs)  {

    private var binding : GthrPasswordEditTextBinding = GthrPasswordEditTextBinding.inflate(LayoutInflater.from(context), this, true)
    private var errorText : String = "Please enter password."
    private var hintText : String = ""
    private var text : String = ""

    init{
        val a = context.obtainStyledAttributes(attrs, R.styleable.GthrPasswordEditText)

        a.getString(R.styleable.GthrPasswordEditText_gthr_password_hint_text)?.let { hintText = it }
        a.getString(R.styleable.GthrPasswordEditText_gthr_password_error_text)?.let { errorText = it }
        a.getString(R.styleable.GthrPasswordEditText_gthr_password_text)?.let { text = it }

        setErrorPassword(
            binding.llPassword,
            binding.tilPassword,
            binding.ivPassword,
            binding.etPassword,
            false
        )

    }
    fun addValidation(){
        if(!binding.etPassword.text.toString().isValidPassword()){
           setErrorPassword(
                binding.llPassword,
                binding.tilPassword,
                binding.ivPassword,
                binding.etPassword,
                true
            )
            binding.etPassword.afterTextChanged {
               setErrorPassword(
                    binding.llPassword,
                    binding.tilPassword,
                    binding.ivPassword,
                    binding.etPassword,
                    !binding.etPassword.text.toString().isValidPassword()
                )
            }
        }
    }

    private fun setErrorPassword(layout : LinearLayoutCompat, textInputLayout : TextInputLayout, imageView : AppCompatImageView, edittext : AppCompatEditText, error : Boolean, text : String? = null){
        layout.isSelected = !error
        imageView.isSelected = !error
        edittext.isSelected = !error

        if(error){
            textInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
            edittext.error = errorText
        }
    }

    @JvmName("setErrorText1")
    fun setErrorText(text : String){
        errorText = text
    }
}