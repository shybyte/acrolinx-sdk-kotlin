package com.acrolinx.client.sdk.platform

import kotlinx.serialization.Serializable

typealias LanguageId = String

@Serializable
class Language(
    val id: LanguageId,
    val displayName: String
)
