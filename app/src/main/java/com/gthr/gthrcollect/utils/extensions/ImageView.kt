package com.gthr.gthrcollect.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gthr.gthrcollect.R

private val colors = listOf(R.color.purple_200, R.color.red, R.color.blue, R.color.yellow_gradient_end_color)

fun ImageView.setImage(url: String) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.setProfileImage(url: String){
    Glide.with(this).load(url).error(R.drawable.user_image).into(this)
}

fun ImageView.setProductImage(url: String){
    Glide.with(this).load(url).error(R.drawable.ic_product_error).into(this)
}


fun ImageView.setCollectionProductImage(url: String){

    val randColor=colors.random()
    Glide.with(this).load(url).error(randColor).into(this)
}

