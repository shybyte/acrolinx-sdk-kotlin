package com.acrolinx.client.sdk

import com.acrolinx.client.sdk.internal.SuccessResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.Url
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

@kotlinx.serialization.UnstableDefault
class AcrolinxEndpoint(
    private val clientSignature: String,
    private val clientVersion: String,
    private var clientLocale: String,
    acrolinxURL: String
) {
    private val acrolinxURL = Url(acrolinxURL)
    private val client: HttpClient = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json.nonstrict).apply {
                register(SuccessResponse.serializer(PlatformInformation.serializer()))
            }
        }
    }

    fun getPlatformInformation(): PlatformInformation = getData("")

    private fun <T> getData(path: String): T =
        getData(acrolinxURL.copy(encodedPath = acrolinxURL.encodedPath + "api/v1/" + path.encodeURLPath()))

    private fun <T> getData(url: Url): T {
        val response = runBlocking {
            client.get<SuccessResponse<T>>(url) {
                header("X-Acrolinx-Base-Url", acrolinxURL)
                header("X-Acrolinx-Client-Locale", clientLocale)
                header("X-Acrolinx-Client", "$clientSignature; $clientVersion")
            }
        }
        return response.data
    }
}