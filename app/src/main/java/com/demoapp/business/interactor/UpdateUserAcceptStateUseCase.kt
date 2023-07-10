package com.demoapp.business.interactor

import com.demoapp.business.data.local.UserCacheDataSource
import com.demoapp.business.model.AcceptState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateUserAcceptStateUseCase @Inject constructor(
    private val userCacheDataSource: UserCacheDataSource
) {

    suspend operator fun invoke(userId: String, @AcceptState acceptState: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userCacheDataSource.get(userId)
            if (user != null) {
                userCacheDataSource.update(user.copy(acceptState = acceptState))
                true
            } else false
        }
    }

}