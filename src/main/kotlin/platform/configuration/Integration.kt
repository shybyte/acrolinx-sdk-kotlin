package com.acrolinx.client.sdk.platform.configuration

import kotlinx.serialization.Serializable

@Serializable
class Integration(
    val properties: Map<String, String>
)