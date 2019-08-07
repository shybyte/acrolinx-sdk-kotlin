package com.acrolinx.client.sdk.check

import kotlinx.serialization.Decoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

sealed class CheckPollResponse {
    @Serializable
    class Success(val data: CheckResult) : CheckPollResponse()

    @Serializable
    class Progress(val progress: com.acrolinx.client.sdk.Progress) : CheckPollResponse()
}

@Serializer(forClass = CheckPollResponse::class)
object CheckPollResponseSerializer : KSerializer<CheckPollResponse> {
    override fun deserialize(decoder: Decoder): CheckPollResponse = try {
        CheckPollResponse.Success.serializer().deserialize(decoder)
    } catch (e: Throwable) {
        CheckPollResponse.Progress.serializer().deserialize(decoder)
    }
}
