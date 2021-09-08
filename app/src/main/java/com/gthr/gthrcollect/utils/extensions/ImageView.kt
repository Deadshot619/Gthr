package com.gthr.gthrcollect.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gthr.gthrcollect.R

fun ImageView.setImageByUrl(url: String){
    Glide.with(this).load(url).into(  this)
}

fun ImageView.setProfileImage(url: String){
    Glide.with(this).load(url).error(R.drawable.profile_pic).into(this)
}

fun ImageView.setProductImage(url: String){
    Glide.with(this).load(url).error(R.drawable.ic_product_error).into(this)
}