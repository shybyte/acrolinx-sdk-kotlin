package com.acrolinx.client.sdk.check

import kotlinx.serialization.Serializable

@Serializable
class CheckCancelledResponse(val data: Data) {
    @Serializable
    class Data(val id: String)
}
