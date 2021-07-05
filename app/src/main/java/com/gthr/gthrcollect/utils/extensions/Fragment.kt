package com.gthr.gthrcollect.utils.extensions

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.constants.SimpleDateFormatConstants
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showToast(text: String, durationLong: Boolean = false) {
    activity?.showToast(text, durationLong)
}

fun Fragment.getBackgroundDrawable(@DrawableRes id: Int): Drawable? =
    ContextCompat.getDrawable(requireContext(), id)

fun Fragment.getResolvedColor(@ColorRes id: Int): Int = ContextCompat.getColor(requireContext(), id)

fun Fragment.showBirthDayPicker(selectedDate : Long?,positiveButtonClick : (Long) -> Unit){
    val validators: ArrayList<CalendarConstraints.DateValidator> = ArrayList()
    validators.add(DateValidatorPointBackward.before(MaterialDatePicker.todayInUtcMilliseconds()))

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(this.getString(R.string.birth_day_dialog_title))
        .setSelection(selectedDate)
        .setCalendarConstraints(
            CalendarConstraints.Builder().setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                .setValidator(CompositeDateValidator.allOf(validators)).build()
        )
        .build()


    datePicker.addOnPositiveButtonClickListener {
        positiveButtonClick(it)
    }

    datePicker.show(this.childFragmentManager, "")

}