package com.acrolinx.client.sdk

import com.acrolinx.client.sdk.exceptions.SSOException
import com.acrolinx.client.sdk.exceptions.SignInException
import com.acrolinx.client.sdk.internal.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject
import platform.Capabilities
import java.net.URL

private const val HOUR_MS = 60L * 60 * 1000

class AcrolinxEndpoint(
    private val clientSignature: String,
    private val clientVersion: String,
    private var clientLocale: String,
    acrolinxUrl: String,
    private var enableHttpLogging: Boolean = false
) {
    private val acrolinxUrl = Url(acrolinxUrl)
    private val jsonDeserializer = Json(JsonConfiguration.Stable.copy(strictMode = false))
    private val httpClient: HttpClient = HttpClient(Apache) {
        if (enableHttpLogging) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
        install(JsonFeature)
    }

    fun getPlatformInformation(): PlatformInformation = fetchDataFromApiPath("", PlatformInformation.serializer())

    @Throws(SSOException::class)
    fun signInWithSSO(genericToken: String, username: String): SignInSuccess = try {
        fetchDataFromApiPath("auth/sign-ins", SignInSuccess.serializer(), HttpMethod.Post) {
            header("username", username)
            header("password", genericToken)
        }
    } catch (e: Exception) {
        throw SSOException()
    }

    @Throws(SignInException::class)
    fun singInInteractive(
        callback: InteractiveCallback<URL>,
        accessToken: AccessToken? = null,
        timeoutMs: Long = HOUR_MS
    ): SignInSuccess {
        val signInResponse =
            fetchFromApiPath("auth/sign-ins", SignInResponseSerializer, HttpMethod.Post, accessToken = accessToken)

        return when (signInResponse) {
            is SignInResponse.Success -> signInResponse.data
            is SignInResponse.SignInLinks -> {
                callback.run(URL(signInResponse.links.interactive))
                pollForInteractiveSignIn(signInResponse, timeoutMs)
            }
        }
    }

    @Throws(SignInException::class)
    private fun pollForInteractiveSignIn(signInResponse: SignInResponse.SignInLinks, timeoutMs: Long): SignInSuccess {
        val endTime = System.currentTimeMillis() + timeoutMs

        while (endTime > System.currentTimeMillis()) {
            when (val pollResponse = fetchFromUrl(Url(signInResponse.links.poll), SignInPollResponseSerializer)) {
                is SignInPollResponse.Success -> return pollResponse.data
                is SignInPollResponse.Progress -> {
                    val sleepTimeMs = (pollResponse.progress.retryAfter * 1000).toLong()
                    if (sleepTimeMs + System.currentTimeMillis() > endTime) {
                        throw SignInException()
                    }
                    Thread.sleep(sleepTimeMs)
                }
            }
        }

        throw SignInException()
    }

    fun getCapabilities(accessToken: AccessToken): Capabilities =
        fetchDataFromApiPath("capabilities", Capabilities.serializer(), accessToken = accessToken)

    private fun <T> fetchDataFromApiPath(
        path: String,
        deserializer: KSerializer<T>,
        httpMethod: HttpMethod = HttpMethod.Get,
        accessToken: AccessToken? = null,
        block: (HttpRequestBuilder.() -> Unit) = {}
    ): T = fetchFromApiPath(path, SuccessResponse.serializer(deserializer), httpMethod, accessToken, block).data

    private fun <T> fetchFromApiPath(
        path: String,
        deserializer: KSerializer<T>,
        httpMethod: HttpMethod = HttpMethod.Get,
        accessToken: AccessToken? = null,
        block: (HttpRequestBuilder.() -> Unit) = {}
    ): T =
        fetchFromUrl(
            acrolinxUrl.copy(encodedPath = acrolinxUrl.encodedPath + "api/v1/" + path.encodeURLPath()),
            deserializer,
            httpMethod,
            accessToken,
            block
        )

    private fun <T> fetchFromUrl(
        url: Url,
        deserializer: KSerializer<T>,
        httpMethod: HttpMethod = HttpMethod.Get,
        accessToken: AccessToken? = null,
        block: (HttpRequestBuilder.() -> Unit) = {}
    ): T {
        val jsonObject = runBlocking {
            httpClient.request<JsonObject>(url) {
                method = httpMethod
                contentType(ContentType.Application.Json)
                header("X-Acrolinx-Base-Url", acrolinxUrl)
                header("X-Acrolinx-Client-Locale", clientLocale)
                header("X-Acrolinx-Client", "$clientSignature; $clientVersion")
                if (accessToken != null) {
                    header("X-Acrolinx-Auth", accessToken.token)
                }
                this.apply(block)
            }
        }
        // TODO: Error Handling
        return jsonDeserializer.fromJson(deserializer, jsonObject)
    }
}