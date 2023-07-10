package com.demoapp.framework.di

import android.content.Context
import com.demoapp.framework.datasource.local.dao.ShadiDatabase
import com.demoapp.framework.datasource.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDB(@ApplicationContext context: Context): ShadiDatabase = ShadiDatabase.getInstance(context)

    @Singleton
    @Provides
    fun providePostDao(appDB: ShadiDatabase): UserDao = appDB.getUserDao()

}