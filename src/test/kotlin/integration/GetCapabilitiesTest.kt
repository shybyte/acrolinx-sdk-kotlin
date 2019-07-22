package integration

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.startsWith
import com.acrolinx.client.sdk.AcrolinxEndpoint
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class GetCapabilitiesTest {
    private lateinit var acrolinxEndpoint: AcrolinxEndpoint

    @BeforeTest
    fun beforeTest() {
        org.junit.Assume.assumeTrue(ACROLINX_URL != null)
        org.junit.Assume.assumeTrue(ACROLINX_API_TOKEN !== null)
        acrolinxEndpoint = createTestAcrolinxEndpoint()
    }

    @Test
    fun testCapabilities() {
        val capabilities = acrolinxEndpoint.getCapabilities(ACROLINX_API_TOKEN!!)
        assertThat(capabilities.checkingCapabilities.guidanceProfiles).isNotEmpty()
    }
}
