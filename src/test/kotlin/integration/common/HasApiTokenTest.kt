package integration.common

import com.acrolinx.client.sdk.AccessToken
import org.junit.Assume.assumeTrue
import kotlin.test.BeforeTest

abstract class HasApiTokenTest : BaseIntegrationTest() {
    protected lateinit var apiToken: AccessToken

    @BeforeTest
    fun beforeHasApiTokenTest() {
        assumeTrue(ACROLINX_API_TOKEN !== null)
        apiToken = ACROLINX_API_TOKEN!!
    }
}
