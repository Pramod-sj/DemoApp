package com.demoapp.framework.di

import com.demoapp.business.data.remote.UserNetworkDataSource
import com.demoapp.framework.datasource.remote.impl.UserNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkDatasourceModule {

    @Binds
    fun bindUserNetworkDatasource(
        userNetworkDataSourceImpl: UserNetworkDataSourceImpl
    ): UserNetworkDataSource

}