package com.example.myapplication.utils

import android.util.Log

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T, U, R> combine(first: Resource<T> , second: Resource<U>, block: (T?, U?) -> R) : Resource<R> {
            if (first.status == Status.ERROR || second.status == Status.ERROR) {
                var error = ""
                error +=  if (first.status == Status.ERROR) first.message + ", " else ""
                error +=  if (second.status == Status.ERROR) second.message else ""
                return error(error, block(null, null))
            }

            if (first.status == Status.LOADING || second.status == Status.LOADING) {
                return loading(block(null, null))
            }

            return success(block(first.data, second.data))
        }
    }
}