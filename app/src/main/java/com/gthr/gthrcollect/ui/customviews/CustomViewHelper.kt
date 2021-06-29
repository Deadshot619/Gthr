package com.gthr.gthrcollect.ui.customviews

import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.textfield.TextInputLayout

object CustomViewHelper {
    fun setErrorEmail(layout : LinearLayoutCompat, imageView : AppCompatImageView,edittext : AppCompatEditText,error : Boolean){
        layout.isSelected = !error
        imageView.isSelected = !error
        edittext.isSelected = !error
        if(error){ edittext.error = "Please enter email." }
    }

    fun setErrorPassword(layout : LinearLayoutCompat, textInputLayout : TextInputLayout, imageView : AppCompatImageView, edittext : AppCompatEditText, error : Boolean, text : String? = null){
        layout.isSelected = !error
        imageView.isSelected = !error
        edittext.isSelected = !error

        if(error){
            textInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
            edittext.error = "Please enter password."
        }
    }



}