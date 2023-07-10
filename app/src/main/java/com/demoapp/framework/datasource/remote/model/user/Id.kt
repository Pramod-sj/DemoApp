package com.demoapp.framework.datasource.remote.model.user


import com.google.gson.annotations.SerializedName

data class Id(
    @SerializedName("name")
    val name: String?,
    @SerializedName("value")
    val value: String?
)