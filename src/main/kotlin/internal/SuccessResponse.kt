package com.acrolinx.client.sdk.internal

import kotlinx.serialization.Serializable

@Serializable
class SuccessResponse<T>(val data: T)