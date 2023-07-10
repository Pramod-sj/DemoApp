package com.demoapp.framework.datasource.local.impl

import com.demoapp.business.data.local.UserCacheDataSource
import com.demoapp.business.model.User
import com.demoapp.framework.datasource.local.dao.UserDao
import com.demoapp.framework.datasource.local.mapper.toDomain
import com.demoapp.framework.datasource.local.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserCacheDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : UserCacheDataSource {

    override suspend fun addAllUsers(users: List<User>) {
        userDao.addAll(users.map { it.toEntity() })
    }

    override suspend fun deleteAllUsers() {
        userDao.deleteAll()
    }

    override fun getAll(): Flow<List<User>> {
        return userDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun get(id: String): User? {
        return userDao.get(id)?.toDomain()
    }

    override suspend fun update(user: User) {
        userDao.update(user.toEntity())
    }

}