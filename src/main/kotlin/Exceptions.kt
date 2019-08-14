package com.acrolinx.client.sdk.exceptions

import internal.AcrolinxServiceError
import io.ktor.http.HttpMethod
import io.ktor.http.Url

sealed class AcrolinxException(message: String? = null) : Exception(message)
class SignInException : AcrolinxException()
class SSOException : AcrolinxException()

class AcrolinxServiceException(val error: AcrolinxServiceError, val request: HttpRequest) : AcrolinxException() {
    enum class Type { auth }
    class HttpRequest(val url: Url, val method: HttpMethod)
}

