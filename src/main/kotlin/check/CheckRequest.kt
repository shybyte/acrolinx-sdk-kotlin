package com.acrolinx.client.sdk.check


import kotlinx.serialization.Serializable

@Serializable()
class CheckRequest(
    val content: String,
    val checkOptions: CheckOptions,
    val contentEncoding: ContentEncoding? = null,
    val document: DocumentDescriptorRequest? = null
)

@Serializable
class CheckOptions(
    val guidanceProfileId: String? = null,
    val batchId: String? = null,
    val reportTypes: List<ReportType>? = null,
    val checkType: CheckType? = null,
    val contentFormat: String? = null,
    val languageId: String? = null,
    val isDisableCustomFieldValidation: Boolean = false
)

enum class ContentEncoding {
    none,
    base64
}

enum class ReportType {
    termHarvesting,
    scorecard,
    extractedText
}

enum class CheckType {
    batch,
    interactive,
    baseline,
    automated
}

@Serializable
class DocumentDescriptorRequest(val reference: String)
