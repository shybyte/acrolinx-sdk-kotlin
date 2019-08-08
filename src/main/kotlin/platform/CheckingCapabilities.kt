package com.acrolinx.client.sdk.platform

import com.acrolinx.client.sdk.check.CheckType
import com.acrolinx.client.sdk.check.ContentEncoding
import com.acrolinx.client.sdk.check.ReportType
import kotlinx.serialization.Serializable

@Serializable
class CheckingCapabilities(
    val guidanceProfiles: List<GuidanceProfile>,
    val contentFormats: List<ContentFormat>,
    val contentEncodings: List<ContentEncoding>,
    val referencePattern: String,
    val checkTypes: List<CheckType>,
    val reportTypes: List<ReportType>
    )

@Serializable
class ContentFormat(val id: String, val displayName: String)
