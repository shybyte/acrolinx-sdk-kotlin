package com.acrolinx.client.sdk

import com.acrolinx.client.sdk.platform.Server
import kotlinx.serialization.Serializable

@Serializable
class PlatformInformation(val server: Server, val locales: List<String>)