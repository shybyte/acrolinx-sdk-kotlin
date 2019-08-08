package integration

import integration.common.ACROLINX_API_SSO_TOKEN
import integration.common.ACROLINX_API_USERNAME
import integration.common.BaseIntegrationTest
import org.junit.Assume.assumeTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class SignInSsoTest : BaseIntegrationTest() {
    @BeforeTest
    fun beforeTest() {
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN !== null)
    }

    @Test
    fun testSignIn() {
        val signInSuccess = acrolinxEndpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN!!, ACROLINX_API_USERNAME!!)
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.user.username)
    }
}
