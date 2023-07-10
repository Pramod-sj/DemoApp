package com.demoapp.framework.datasource.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demoapp.framework.datasource.local.model.UserCacheEntity

@Database(
    entities = [UserCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShadiDatabase : RoomDatabase() {

    companion object {

        private const val APP_DB_NAME = "shadi_db"

        fun getInstance(context: Context): ShadiDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ShadiDatabase::class.java,
                APP_DB_NAME
            ).build()
        }
    }

    abstract fun getUserDao(): UserDao

}