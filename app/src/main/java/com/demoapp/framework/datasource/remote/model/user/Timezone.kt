package com.demoapp.framework.datasource.remote.model.user


import com.google.gson.annotations.SerializedName

data class Timezone(
    @SerializedName("description")
    val description: String?,
    @SerializedName("offset")
    val offset: String?
)