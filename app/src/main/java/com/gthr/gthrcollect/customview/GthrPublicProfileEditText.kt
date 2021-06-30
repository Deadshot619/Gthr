package com.gthr.gthrcollect.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.GthrPublicProfileEdittextBinding

class GthrPublicProfileEditText(context : Context, attrs : AttributeSet) : FrameLayout(context,attrs){

    private var binding : GthrPublicProfileEdittextBinding= GthrPublicProfileEdittextBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
        val a = context.obtainStyledAttributes(attrs, R.styleable.GthrAuthButton)

        a.getString(R.styleable.GthrPublicProfileButton_gthr_pp_text)?.let { binding.etMain.setText(it)}
    }

    @JvmName("setText1")
    fun setText(text : String){
        binding.etMain.setText(text)
    }



}