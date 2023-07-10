package com.demoapp.framework.datasource.remote.impl

import com.demoapp.business.ApiResult
import com.demoapp.business.ErrorType
import com.demoapp.business.data.remote.UserNetworkDataSource
import com.demoapp.business.model.User
import com.demoapp.business.safeApiCall
import com.demoapp.framework.datasource.remote.mapper.toDomain
import com.demoapp.framework.datasource.remote.service.UserApiService
import javax.inject.Inject

class UserNetworkDataSourceImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserNetworkDataSource {

    override suspend fun getUsers(): ApiResult<List<User>> {
        return safeApiCall { userApiService.getUsers().results?.map { it.toDomain() }.orEmpty() }
    }

}