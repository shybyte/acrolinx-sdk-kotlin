package com.acrolinx.client.sdk.platform

import kotlinx.serialization.Serializable

@Serializable
class Capabilities(private val checking: CheckingCapabilities) {
    val checkingCapabilities: CheckingCapabilities
        get() = checking
}
