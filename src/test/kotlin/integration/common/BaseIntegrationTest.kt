package integration.common

import com.acrolinx.client.sdk.AcrolinxEndpoint
import org.junit.Assume.assumeTrue
import kotlin.test.BeforeTest

abstract class BaseIntegrationTest {
    protected lateinit var acrolinxUrl: String
    protected lateinit var acrolinxEndpoint: AcrolinxEndpoint

    @BeforeTest
    fun beforeTestBase() {
        assumeTrue(ACROLINX_URL != null)
        acrolinxUrl = ACROLINX_URL!!
        acrolinxEndpoint = createTestAcrolinxEndpoint()
    }
}
