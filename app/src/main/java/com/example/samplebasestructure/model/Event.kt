package com.example.samplebasestructure.model

class Event<T>(private val content: T) {
    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    val contentIfNotHandled: T?
        get() = if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T {
        return content
    }
}