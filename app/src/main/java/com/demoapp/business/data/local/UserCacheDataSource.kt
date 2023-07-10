package com.demoapp.business.data.local

import com.demoapp.business.model.User
import kotlinx.coroutines.flow.Flow

interface UserCacheDataSource {

    suspend fun addAllUsers(users: List<User>)

    suspend fun update(user: User)

    suspend fun deleteAllUsers()

    fun getAll(): Flow<List<User>>

    suspend fun get(id: String): User?
}