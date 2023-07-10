package com.demoapp.framework.datasource.remote.mapper

import com.demoapp.business.model.ACCEPT_STATE_UNKNOWN
import com.demoapp.business.model.AcceptState
import com.demoapp.business.model.User
import com.demoapp.framework.datasource.remote.model.user.UserNetworkEntity


fun UserNetworkEntity.toDomain(): User {
    return User(
        id = login?.uuid.orEmpty(),
        firstName = name?.first.orEmpty(),
        lastName = name?.last.orEmpty(),
        city = location?.city.orEmpty(),
        state = location?.state.orEmpty(),
        country = location?.country.orEmpty(),
        profilePictureUrl = picture?.medium.orEmpty(),
        acceptState = ACCEPT_STATE_UNKNOWN
    )
}