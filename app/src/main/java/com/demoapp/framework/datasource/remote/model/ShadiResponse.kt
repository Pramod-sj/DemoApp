package com.demoapp.framework.datasource.remote.model


import com.google.gson.annotations.SerializedName

data class ShadiResponse<T>(
    @SerializedName("info")
    val info: Info?,
    @SerializedName("results")
    val results: T?
)