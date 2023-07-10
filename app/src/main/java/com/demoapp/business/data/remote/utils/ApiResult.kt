package com.demoapp.business.data.remote.utils

sealed class ApiResult<out T> {

    data class Success<out T>(val data: T) : ApiResult<T>()

    data class Failure(val message: String? = null) : ApiResult<Nothing>()

}