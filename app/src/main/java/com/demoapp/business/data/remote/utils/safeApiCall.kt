package com.demoapp.business.data.remote.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class ErrorType {
    HTTP_EXCEPTION,
    NETWORK, // IO
    TIMEOUT, // Socket
    UNKNOWN //Anything else
}


suspend inline fun <T> safeApiCall(crossinline responseFunction: suspend () -> T): ApiResult<T> {
    return try {
        withContext(Dispatchers.IO) {
            ApiResult.Success(responseFunction())
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            e.printStackTrace()
            ApiResult.Failure(e.message)
        }
    }
}