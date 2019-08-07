package com.acrolinx.client.sdk.internal

import kotlinx.serialization.Serializable
import kotlin.math.roundToLong

@Serializable
class ProgressData(
    private val retryAfter: Double
) {
    fun getRetryAfterMs(): Long = (this.retryAfter * 1000.0).roundToLong()
}