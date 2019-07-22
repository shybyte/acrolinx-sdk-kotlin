package com.acrolinx.client.sdk

import kotlinx.serialization.*

@Serializable
data class AccessToken(val token: String)

@Serializer(forClass = AccessToken::class)
object AccessTokenSerializer : KSerializer<AccessToken> {
    override fun deserialize(decoder: Decoder): AccessToken = AccessToken(decoder.decodeString())
    override fun serialize(encoder: Encoder, obj: AccessToken) = encoder.encodeString(obj.token)
}
