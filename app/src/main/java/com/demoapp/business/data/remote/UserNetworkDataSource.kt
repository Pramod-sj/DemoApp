package com.demoapp.business.data.remote

import com.demoapp.business.ApiResult
import com.demoapp.business.model.User

interface UserNetworkDataSource {

    suspend fun getUsers(): ApiResult<List<User>>

}