package integration

import com.acrolinx.client.sdk.AcrolinxEndpoint
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class SignInSssoTest {
    private lateinit var acrolinxEndpoint: AcrolinxEndpoint

    @BeforeTest
    fun beforeTest() {
        org.junit.Assume.assumeTrue(ACROLINX_URL != null)
        org.junit.Assume.assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN !== null)
        acrolinxEndpoint = createTestAcrolinxEndpoint()
    }

    @Test
    fun testSignIn() {
        val signInSuccess = acrolinxEndpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN!!, ACROLINX_API_USERNAME!!)
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.user.username);
    }
}
