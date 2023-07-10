package com.demoapp.business.interactor

import com.demoapp.business.ApiResult
import com.demoapp.business.data.local.UserCacheDataSource
import com.demoapp.business.data.remote.UserNetworkDataSource
import com.demoapp.business.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * UseCase which fetches random shadi users from network and save it to local cache
 * To refresh local db we need to pass forceRefresh = true
 * else it will always return data from local db
 * This UseCase emits updated user list whenever there's a change in local db
 */
class GetRandomShadiUserUseCase @Inject constructor(
    private val userCacheDataSource: UserCacheDataSource,
    private val userNetworkDataSource: UserNetworkDataSource
) {

    companion object {
        private const val ERROR_MESSAGE = "Something went wrong!"
    }

    sealed class UseCaseResult {

        abstract val data: List<User>

        data class Success(override val data: List<User>) : UseCaseResult()

        data class GenericFailure(
            val message: String,
            override val data: List<User>
        ) : UseCaseResult()

        //can have other classes if any specific issue
    }

    operator fun invoke(forceFetch: Boolean): Flow<UseCaseResult> {
        return flow {
            val users = userCacheDataSource.getAll().firstOrNull()
            val flow = if (users.isNullOrEmpty() || forceFetch) {
                when (val apiResult = userNetworkDataSource.getUsers()) {
                    is ApiResult.Failure -> {
                        userCacheDataSource.getAll().map {
                            UseCaseResult.GenericFailure(apiResult.message.orEmpty(), it)
                        }
                    }

                    is ApiResult.Success -> {
                        userCacheDataSource.deleteAllUsers()
                        userCacheDataSource.addAllUsers(apiResult.data)
                        userCacheDataSource.getAll().map { UseCaseResult.Success(it) }
                    }
                }
            } else {
                userCacheDataSource.getAll().map { UseCaseResult.Success(it) }
            }
            emitAll(flow)
        }
    }

}