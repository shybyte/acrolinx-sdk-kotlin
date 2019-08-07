package com.acrolinx.client.sdk.check

import kotlinx.serialization.Serializable

@Serializable
data class Issue(
    val displayNameHtml: String,
    val guidanceHtml: String,
    val displaySurface: String,
    val suggestions: List<Suggestion>,
    val positionalInformation: PositionalInformation
) {
    @Serializable
    data class Suggestion(val surface: String)

    @Serializable
    data class PositionalInformation(val matches: List<Match>)

    @Serializable
    data class Match(val originalBegin: Long, val originalEnd: Long, val originalPart: String)
}
