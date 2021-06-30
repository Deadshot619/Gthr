package com.gthr.gthrcollect.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.GthrAuthButtonBinding

class GthrAuthButton(context : Context, attrs : AttributeSet) : FrameLayout(context,attrs) {

   var binding : GthrAuthButtonBinding = GthrAuthButtonBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
        val a = context.obtainStyledAttributes(attrs, R.styleable.GthrAuthButton)

        a.getString(R.styleable.GthrAuthButton_gthr_auth_text)?.let { binding.btnMain.text = it}
    }

    @JvmName("setText1")
    fun setText(text : String){
        binding.btnMain.text = text
    }

}