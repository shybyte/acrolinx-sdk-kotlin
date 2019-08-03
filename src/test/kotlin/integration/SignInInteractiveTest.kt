package integration

import assertk.assertThat
import assertk.assertions.*
import com.acrolinx.client.sdk.InteractiveCallback
import com.acrolinx.client.sdk.exceptions.SignInException
import integration.common.ACROLINX_API_TOKEN
import integration.common.ACROLINX_API_USERNAME
import integration.common.ACROLINX_URL
import integration.common.BaseIntegrationTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.fail


class SignInInteractiveTest : BaseIntegrationTest() {
    @BeforeTest
    fun beforeTest() {
        org.junit.Assume.assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_TOKEN !== null)
    }

    @Test
    fun testSignInWithPolling() {
        var interactiveUrl: String? = null
        try {
            val signInSuccess = acrolinxEndpoint.signInInteractive(object : InteractiveCallback {
                override fun onInteractiveUrl(url: String) {
                    interactiveUrl = url
                }
            }, timeoutMs = 10)
            fail("Should throw exception but fot $signInSuccess")
        } catch (e: SignInException) {
            assertThat(interactiveUrl!!).startsWith(ACROLINX_URL!!)
        }
    }

    @Test
    fun testSignInWithPollingWithValidAuthToken() {
        var interactiveUrl: String? = null
        val signInSuccess = acrolinxEndpoint.signInInteractive(object : InteractiveCallback {
            override fun onInteractiveUrl(url: String) {
                interactiveUrl = url
            }
        }, timeoutMs = 10, accessToken = ACROLINX_API_TOKEN)

        assertThat(interactiveUrl).isNull()

        assertThat(signInSuccess.user.username).isEqualTo(ACROLINX_API_USERNAME)
        assertThat(signInSuccess.accessToken.token).isNotEmpty()
        assertThat(signInSuccess.authorizedUsing).isEqualTo("ACROLINX_TOKEN")
        assertThat(signInSuccess.integration.properties["ca.filter"]).isNotNull()
    }
}
