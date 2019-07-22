package com.acrolinx.client.sdk.internal

import com.acrolinx.client.sdk.SignInSuccess
import internal.ProgressData
import kotlinx.serialization.Decoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

sealed class SignInResponse {
    @Serializable
    class Success(val data: SignInSuccess) : SignInResponse()

    @Serializable
    class SignInLinks(val data: SignInLinksData, val links: com.acrolinx.client.sdk.internal.SignInLinks) :
        SignInResponse()
}

@Serializer(forClass = SignInResponse::class)
object SignInResponseSerializer : KSerializer<SignInResponse> {
    override fun deserialize(decoder: Decoder): SignInResponse = try {
        SignInResponse.Success.serializer().deserialize(decoder)
    } catch (e: Throwable) {
        SignInResponse.SignInLinks.serializer().deserialize(decoder)
    }
}

sealed class SignInPollResponse {
    @Serializable
    class Success(val data: SignInSuccess) : SignInPollResponse()

    @Serializable
    class Progress(val progress: ProgressData) : SignInPollResponse()
}

@Serializer(forClass = SignInPollResponse::class)
object SignInPollResponseSerializer : KSerializer<SignInPollResponse> {
    override fun deserialize(decoder: Decoder): SignInPollResponse = try {
        SignInPollResponse.Success.serializer().deserialize(decoder)
    } catch (e: Throwable) {
        SignInPollResponse.Progress.serializer().deserialize(decoder)
    }
}


@Serializable
class SignInLinksData(
    /**
     * Duration in seconds
     */
    val interactiveLinkTimeout: Double
)

@Serializable
class SignInLinks(
    val interactive: String,
    val poll: String
)
