package com.acrolinx.client.sdk.platform

import kotlinx.serialization.Serializable

typealias GuidanceProfileId = String

@Serializable
class GuidanceProfile(
    val id: GuidanceProfileId,
    val displayName: String,
    val language: Language
)
