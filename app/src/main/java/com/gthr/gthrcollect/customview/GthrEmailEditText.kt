package com.gthr.gthrcollect.customview


import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.GthrEmailEditTextBinding
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.isValidEmail


@SuppressLint("Recycle", "CustomViewStyleable")
class GthrEmailEditText(context : Context, attrs : AttributeSet) : LinearLayoutCompat(context,attrs  ) {

    private var binding : GthrEmailEditTextBinding = GthrEmailEditTextBinding.inflate(LayoutInflater.from(context), this, true)
    private var hintText : String = ""
    private var errorText : String = "Please enter email."
    private var text : String = ""


    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.GthrEmailEditText)

        a.getString(R.styleable.GthrEmailEditText_gthr_email_hint_text)?.let { hintText = it }
        a.getString(R.styleable.GthrEmailEditText_gthr_email_error_text)?.let { errorText = it }
        a.getString(R.styleable.GthrEmailEditText_gthr_email_text)?.let { text = it }


        setErrorEmail(
            binding.llEmail,
            binding.ivEmail,
            binding.etEmail,
            false
        )



    }

    private fun setErrorEmail(layout : LinearLayoutCompat, imageView : AppCompatImageView, edittext : AppCompatEditText, error : Boolean){
        layout.isSelected = !error
        imageView.isSelected = !error
        edittext.isSelected = !error
        if(error){ edittext.error = errorText }
    }

    fun addValidation(){
        if(!binding.etEmail.text.toString().isValidEmail()){
            setErrorEmail(
                binding.llEmail,
                binding.ivEmail,
                binding.etEmail,
                true
            )
            binding.etEmail.afterTextChanged {
                setErrorEmail(
                    binding.llEmail,
                    binding.ivEmail,
                    binding.etEmail,
                    !binding.etEmail.text.toString().isValidEmail()
                )
            }
        }
    }

    @JvmName("setErrorText1")
    fun setErrorText(text : String){
        errorText = text
    }

}