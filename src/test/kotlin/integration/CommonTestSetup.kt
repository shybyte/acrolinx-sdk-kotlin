package integration

import com.acrolinx.client.sdk.AccessToken
import com.acrolinx.client.sdk.AcrolinxEndpoint
import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv()
val ACROLINX_URL = dotenv["ACROLINX_URL"]

val ACROLINX_API_SSO_TOKEN = dotenv["ACROLINX_API_SSO_TOKEN"]
val ACROLINX_API_USERNAME = dotenv["ACROLINX_API_USERNAME"]
val ACROLINX_API_TOKEN = dotenv["ACROLINX_API_TOKEN"]?.let { AccessToken(it) }

const val DEVELOPMENT_SIGNATURE = "SW50ZWdyYXRpb25EZXZlbG9wbWVudERlbW9Pbmx5"

fun createTestAcrolinxEndpoint() =
    AcrolinxEndpoint(DEVELOPMENT_SIGNATURE, "1.2.3.4", "en", ACROLINX_URL!!, enableHttpLogging = false)
