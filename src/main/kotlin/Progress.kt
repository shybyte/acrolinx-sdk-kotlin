package com.acrolinx.client.sdk

import kotlinx.serialization.Serializable
import kotlin.math.roundToLong

/**
 * @param retryAfter In seconds
 */
@Serializable
data class Progress(val retryAfter: Double, val percent: Double, val message: String) {
    /**
     * In milliseconds
     */
    fun getRetryAfterMs(): Long = (this.retryAfter * 1000.0).roundToLong()
}
