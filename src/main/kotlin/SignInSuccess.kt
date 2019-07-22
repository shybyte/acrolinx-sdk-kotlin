package com.acrolinx.client.sdk

import com.acrolinx.client.sdk.platform.configuration.Integration
import kotlinx.serialization.Serializable

@Serializable
class SignInSuccess(
    @Serializable(with = AccessTokenSerializer::class)
    val accessToken: AccessToken,

    val user: User,
    val integration: Integration,
    val authorizedUsing: String
)
