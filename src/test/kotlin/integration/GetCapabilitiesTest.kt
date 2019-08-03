package integration

import assertk.assertThat
import assertk.assertions.isNotEmpty
import integration.common.ACROLINX_API_TOKEN
import integration.common.BaseIntegrationTest
import org.junit.Assume.assumeTrue
import kotlin.test.BeforeTest
import kotlin.test.Test


class GetCapabilitiesTest: BaseIntegrationTest(){
    @BeforeTest
    fun beforeTest() {
        assumeTrue(ACROLINX_API_TOKEN !== null)
    }

    @Test
    fun testCapabilities() {
        val capabilities = acrolinxEndpoint.getCapabilities(ACROLINX_API_TOKEN!!)
        assertThat(capabilities.checkingCapabilities.guidanceProfiles).isNotEmpty()
    }
}
