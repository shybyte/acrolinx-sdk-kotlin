package com.acrolinx.client.sdk

import com.acrolinx.client.sdk.check.CheckRequest
import com.acrolinx.client.sdk.check.CheckResponse
import com.acrolinx.client.sdk.check.CheckResult
import com.acrolinx.client.sdk.exceptions.SSOException
import com.acrolinx.client.sdk.exceptions.SignInException
import com.acrolinx.client.sdk.platform.Capabilities
import kotlinx.coroutines.runBlocking

private const val HOUR_MS = 60L * 60 * 1000

class AcrolinxEndpoint(
    clientSignature: String,
    clientVersion: String,
    clientLocale: String,
    acrolinxUrl: String,
    enableHttpLogging: Boolean = false
) {
    private val asyncEndpoint = AcrolinxEndpointAsync(
        clientSignature = clientSignature,
        clientVersion = clientVersion,
        clientLocale = clientLocale,
        acrolinxUrl = acrolinxUrl,
        enableHttpLogging = enableHttpLogging
    )

    fun getPlatformInformation(): PlatformInformation = runBlocking { asyncEndpoint.getPlatformInformation() }

    @Throws(SSOException::class)
    fun signInWithSSO(genericToken: String, username: String): SignInSuccess =
        runBlocking { asyncEndpoint.signInWithSSO(genericToken = genericToken, username = username) }

    @Throws(SignInException::class)
    fun signInInteractive(
        callback: InteractiveCallback,
        accessToken: AccessToken? = null,
        timeoutMs: Long = HOUR_MS
    ): SignInSuccess = runBlocking {
        asyncEndpoint.signInInteractive(accessToken, timeoutMs) {
            callback.onInteractiveUrl(it.toString())
        }
    }

    fun getCapabilities(accessToken: AccessToken): Capabilities =
        runBlocking { asyncEndpoint.getCapabilities(accessToken) }

    fun check(accessToken: AccessToken, checkRequest: CheckRequest): CheckResponse =
        runBlocking { asyncEndpoint.check(accessToken, checkRequest) }

    fun checkAndGetResult(
        accessToken: AccessToken,
        checkRequest: CheckRequest,
        progressListener: ProgressListener
    ): CheckResult =
        runBlocking { asyncEndpoint.checkAndGetResult(accessToken, checkRequest, progressListener::onProgress) }
}