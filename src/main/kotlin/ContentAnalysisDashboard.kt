package com.acrolinx.client.sdk

import com.acrolinx.client.sdk.platform.Link
import kotlinx.serialization.Serializable

@Serializable
class ContentAnalysisDashboard(val links: List<Link>)
