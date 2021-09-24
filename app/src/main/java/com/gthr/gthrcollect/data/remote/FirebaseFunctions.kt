package com.gthr.gthrcollect.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.utils.logger.GthrLogger

fun <T> fetchData(funName: String, parameter: HashMap<*, *>): Task<T> {
    // Create the arguments to the callable function.
    val functions: FirebaseFunctions = Firebase.functions

    return functions
        .getHttpsCallable(funName)
        .call(parameter)
        .continueWith { task ->
            val result = task.result?.data as T
            GthrLogger.d("result", "Result: ${task.toString()}")
            GthrLogger.d("result", "Result: ${result}")
            result
        }
}

fun <T> fetchDataWithoutParameter(funName: String): Task<T> {
    // Create the arguments to the callable function.
    val functions: FirebaseFunctions = Firebase.functions
    Firebase.auth.currentUser

    return functions
        .getHttpsCallable(funName)
        .call()
        .continueWith { task ->
            val result = task.result?.data as T
            GthrLogger.d("result", "Result: ${task.toString()}")
            GthrLogger.d("result", "Result: ${result}")
            result
        }
}