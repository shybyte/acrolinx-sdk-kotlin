package internal

import kotlinx.serialization.Serializable

@Serializable
data class AcrolinxServiceError(
    val type: String, val title: String, val detail: String, val status: Int, val reference: String? = null
)