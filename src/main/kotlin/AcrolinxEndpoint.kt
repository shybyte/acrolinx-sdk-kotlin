package com.acrolinx.client.sdk

import com.acrolinx.client.sdk.exceptions.SSOException
import com.acrolinx.client.sdk.exceptions.SignInException
import kotlinx.coroutines.runBlocking
import platform.Capabilities

private const val HOUR_MS = 60L * 60 * 1000

class AcrolinxEndpoint(
    private val clientSignature: String,
    private val clientVersion: String,
    private var clientLocale: String,
    acrolinxUrl: String,
    private var enableHttpLogging: Boolean = false
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
}