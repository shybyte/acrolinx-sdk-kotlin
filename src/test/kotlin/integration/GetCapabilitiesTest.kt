package integration

import assertk.assertThat
import assertk.assertions.isNotEmpty
import integration.common.HasApiTokenTest
import kotlin.test.Test


class GetCapabilitiesTest : HasApiTokenTest() {
    @Test
    fun testCapabilities() {
        val capabilities = acrolinxEndpoint.getCapabilities(apiToken)
        assertThat(capabilities.checkingCapabilities.guidanceProfiles).isNotEmpty()
    }
}
