package integration

import assertk.assertThat
import assertk.assertions.isNotEmpty
import integration.common.HasApiTokenTest
import kotlin.test.Test

class ContentAnalysisDashboardTest : HasApiTokenTest() {
    @Test
    fun testCapabilities() {
        val contentAnalysisDashboardLink = acrolinxEndpoint.getContentAnalysisDashboard(apiToken, "arbitraryId");
        assertThat(contentAnalysisDashboardLink).isNotEmpty()
    }
}
