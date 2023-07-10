package com.demoapp.business.model

import androidx.annotation.IntDef


@Retention(AnnotationRetention.RUNTIME)
@IntDef(ACCEPT_STATE_UNKNOWN, ACCEPT_STATE_DECLINE, ACCEPT_STATE_ACCEPT)
annotation class AcceptState

const val ACCEPT_STATE_UNKNOWN = -1
const val ACCEPT_STATE_DECLINE = 0
const val ACCEPT_STATE_ACCEPT = 1

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val city: String,
    val state: String,
    val country: String,
    val profilePictureUrl: String,
    @AcceptState val acceptState: Int
)