package integration

import assertk.assertThat
import assertk.assertions.isNull
import assertk.assertions.startsWith
import com.acrolinx.client.sdk.AcrolinxEndpoint
import com.acrolinx.client.sdk.InteractiveCallback
import com.acrolinx.client.sdk.exceptions.SignInException
import java.net.URL
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.fail


class SignInInteractiveTest {
    private lateinit var acrolinxEndpoint: AcrolinxEndpoint

    @BeforeTest
    fun beforeTest() {
        org.junit.Assume.assumeTrue(ACROLINX_URL != null)
        org.junit.Assume.assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_TOKEN !== null)
        acrolinxEndpoint = createTestAcrolinxEndpoint()
    }

    @Test
    fun testSignInWithPolling() {
        var interactiveUrl: URL? = null;
        try {
            val signInSuccess = acrolinxEndpoint.singInInteractive(object : InteractiveCallback<URL> {
                override fun run(event: URL) {
                    interactiveUrl = event
                }
            }, timeoutMs = 10);
            fail("Should throw exception but fot $signInSuccess")
        } catch (e: SignInException) {
            assertThat(interactiveUrl.toString()).startsWith(ACROLINX_URL!!)
        }
    }

    @Test
    fun testSignInWithPollingWithValidAuthToken() {
        var interactiveUrl: URL? = null;
        val signInSuccess = acrolinxEndpoint.singInInteractive(object : InteractiveCallback<URL> {
            override fun run(event: URL) {
                interactiveUrl = event
            }
        }, timeoutMs = 10, accessToken = ACROLINX_API_TOKEN);

        assertThat(signInSuccess.user.username).equals(ACROLINX_API_USERNAME)
        assertThat(interactiveUrl).isNull()
    }
}
