package integration

import assertk.assertThat
import assertk.assertions.startsWith
import integration.common.BaseIntegrationTest
import kotlin.test.Test
import kotlin.test.assertEquals


class GetPlatformInformationTest : BaseIntegrationTest() {
    @Test
    fun testGetPlatformInformation() {
        val platformInformation = acrolinxEndpoint.getPlatformInformation()

        assertThat(platformInformation.server::version).startsWith("2019")
        assertEquals("Acrolinx Core Platform", platformInformation.server.name)
        assertEquals(listOf("en", "fr", "de", "ja", "pt", "sv", "zh"), platformInformation.locales)
    }
}
