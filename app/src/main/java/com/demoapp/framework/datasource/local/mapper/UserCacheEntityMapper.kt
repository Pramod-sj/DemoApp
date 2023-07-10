package com.demoapp.framework.datasource.local.mapper

import com.demoapp.business.model.User
import com.demoapp.framework.datasource.local.model.UserCacheEntity


fun UserCacheEntity.toDomain(): User {
    return User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        city = city,
        state = state,
        country = country,
        profilePictureUrl = profilePictureUrl,
        acceptState = acceptState
    )
}


fun User.toEntity(): UserCacheEntity {
    return UserCacheEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        city = city,
        state = state,
        country = country,
        profilePictureUrl = profilePictureUrl,
        acceptState = acceptState
    )
}