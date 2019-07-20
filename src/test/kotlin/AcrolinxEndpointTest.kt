import com.acrolinx.client.sdk.AcrolinxEndpoint
import io.github.cdimascio.dotenv.dotenv
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

val dotenv = dotenv()
val ACROLINX_URL = dotenv["ACROLINX_URL"]!!

const val DEVELOPMENT_SIGNATURE = "SW50ZWdyYXRpb25EZXZlbG9wbWVudERlbW9Pbmx5"

@kotlinx.serialization.UnstableDefault
class AcrolinxEndpointTest {
    @Test
    fun testGetPlatformInformation() {
        val acrolinxEndpoint = AcrolinxEndpoint(DEVELOPMENT_SIGNATURE, "1.2.3.4", "en", ACROLINX_URL)

        val platformInformation = acrolinxEndpoint.getPlatformInformation()

        assertTrue { platformInformation.server.version.startsWith("2019.08") }
        assertEquals("Acrolinx Core Platform", platformInformation.server.name)
        assertEquals(listOf("en", "fr", "de", "ja", "pt", "sv", "zh"), platformInformation.locales)
    }
}
