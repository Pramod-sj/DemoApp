package com.demoapp.framework.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.demoapp.framework.datasource.local.model.UserCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(users: List<UserCacheEntity>)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<UserCacheEntity>>

    @Query("SELECT * FROM user WHERE id=:id")
    suspend fun get(id: String): UserCacheEntity?

    @Update
    suspend fun update(user: UserCacheEntity)

}