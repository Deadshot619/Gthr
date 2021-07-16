package com.gthr.gthrcollect.utils.extensions

fun String.toMobileNumber() : String{
    val splitPhoneNumber = this.split("-")
    return if(splitPhoneNumber.size==2){
        splitPhoneNumber[1]
    } else ""
}

fun String.toCountryCode() : Int{
    return try {
        val splitPhoneNumber = this.split("-")
        if(splitPhoneNumber.size==2){
            splitPhoneNumber[0].replace("+","").toInt()
        }
        else -1
    }
    catch (e : Exception){
        -1
    }

}

fun String.toMM() : String = this.substringBefore("/")

fun String.toDD() : String = this.substringAfter("/").substringBefore("/")

fun String.toYYYY() : String = this.substringAfterLast("/")