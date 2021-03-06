package com.gthr.gthrcollect.utils.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.gthr.gthrcollect.utils.constants.CalendarConstants.RECENT_SALE_DATE_DISPLAY_FORMAT
import com.gthr.gthrcollect.utils.constants.CalendarConstants.RECENT_SALE_DATE_NETWORK_FORMAT
import com.gthr.gthrcollect.utils.constants.CalendarConstants.Receipt_DATE_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.removeSlash() = this.replace("\\", "").replace("\"{", "{").replace("}\"", "}")

fun String.toMobileNumber() : String{
    val splitPhoneNumber = this.split("(")
    return if(splitPhoneNumber.size==2){
        splitPhoneNumber[1].removeRange(3..4).replace("-", "")
    } else ""
}

fun String.toCountryCode() : Int{
    return try {
        val splitPhoneNumber = this.split("(")
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

fun String.toRecentSaleDate() : String? {
    val input = SimpleDateFormat(RECENT_SALE_DATE_NETWORK_FORMAT)
    val output = SimpleDateFormat(RECENT_SALE_DATE_DISPLAY_FORMAT)
    try {
        val getAbbreviate = input.parse(this)    // parse input
        return output.format(getAbbreviate)    // format output
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.toReceiptDate() : String? {
    val input = SimpleDateFormat(RECENT_SALE_DATE_NETWORK_FORMAT)
    val output = SimpleDateFormat(Receipt_DATE_FORMAT)
    try {
        val getAbbreviate = input.parse(this)    // parse input
        return output.format(getAbbreviate)    // format output
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.toDate() : Date? {
    val input = SimpleDateFormat(RECENT_SALE_DATE_NETWORK_FORMAT)
    try {
        return input.parse(this)     // format output
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.isValidPrice(): String {
    this.toDoubleOrNull()?.let {
        if (it > 0.0 && it < 999999999.0) return String.format("%.2f", it)
    }
    return "-.--"
}

fun String.urlToBitmap(): Bitmap? = BitmapFactory.decodeFile(this)

fun String.formatMobileNo() : String {
  val number= String.format("(%s) %s-%s", this.substring(0, 3), this.substring(3, 6), this.substring(6, 10))
    return number
}