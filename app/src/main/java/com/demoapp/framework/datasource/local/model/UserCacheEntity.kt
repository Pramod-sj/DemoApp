package com.demoapp.framework.datasource.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demoapp.business.model.AcceptState

@Entity("user")
data class UserCacheEntity(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val city: String,
    val state: String,
    val country: String,
    val profilePictureUrl: String,
    val acceptState: Int
)