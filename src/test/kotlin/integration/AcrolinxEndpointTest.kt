package integration

import assertk.assertThat
import assertk.assertions.startsWith
import com.acrolinx.client.sdk.AcrolinxEndpoint
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class AcrolinxEndpointTest {
    private lateinit var acrolinxEndpoint: AcrolinxEndpoint

    @BeforeTest
    fun beforeTest() {
        org.junit.Assume.assumeTrue(ACROLINX_URL != null)
        acrolinxEndpoint = createTestAcrolinxEndpoint()
    }

    @Test
    fun testGetPlatformInformation() {
        org.junit.Assume.assumeTrue(ACROLINX_URL != null)
        val platformInformation = acrolinxEndpoint.getPlatformInformation()

        assertThat(platformInformation.server::version).startsWith("2019")
        assertEquals("Acrolinx Core Platform", platformInformation.server.name)
        assertEquals(listOf("en", "fr", "de", "ja", "pt", "sv", "zh"), platformInformation.locales)
    }
}
