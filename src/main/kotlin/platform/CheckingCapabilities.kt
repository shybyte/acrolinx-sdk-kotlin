package com.acrolinx.client.sdk.platform

import kotlinx.serialization.Serializable

@Serializable
class CheckingCapabilities(
    val guidanceProfiles: List<GuidanceProfile>
)
