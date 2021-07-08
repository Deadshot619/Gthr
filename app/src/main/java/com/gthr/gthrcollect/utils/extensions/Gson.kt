package com.gthr.gthrcollect.utils.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson
    get() = Gson()

inline fun <reified T> Gson.fromJsonString(value: String): T? =
    fromJson(value, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.toJsonString(value: T): String = toJson(value)
