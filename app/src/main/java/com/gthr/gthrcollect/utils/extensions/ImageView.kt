package com.gthr.gthrcollect.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImageByUrl(url :String){
    Glide.with(this).load(url).into(  this)
}