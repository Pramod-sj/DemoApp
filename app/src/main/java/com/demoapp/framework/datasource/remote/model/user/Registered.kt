package com.demoapp.framework.datasource.remote.model.user


import com.google.gson.annotations.SerializedName

data class Registered(
    @SerializedName("age")
    val age: Int?,
    @SerializedName("date")
    val date: String?
)