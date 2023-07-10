package com.demoapp.framework.datasource.remote.service

import com.demoapp.framework.datasource.remote.model.ShadiResponse
import com.demoapp.framework.datasource.remote.model.user.UserNetworkEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {

    @GET("/api")
    suspend fun getUsers(@Query("results") count: Int = 10): ShadiResponse<List<UserNetworkEntity>>

}