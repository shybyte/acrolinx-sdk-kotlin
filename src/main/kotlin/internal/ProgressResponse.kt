package internal

import kotlinx.serialization.Serializable

@Serializable
class ProgressData(
    val retryAfter: Double,
    val percent: Double? = null,
    val message: String? = null
)