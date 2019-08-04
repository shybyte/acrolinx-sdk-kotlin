package integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.acrolinx.client.sdk.InteractiveCallback
import integration.common.ACROLINX_API_TOKEN
import integration.common.ACROLINX_API_USERNAME
import integration.common.BaseIntegrationTest
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail


class SignInInteractiveTest : BaseIntegrationTest() {
    private val interactiveCallback = mockk<InteractiveCallback>(relaxed = true)

    @BeforeTest
    fun beforeTest() {
        org.junit.Assume.assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_TOKEN !== null)
    }

    @Test
    fun testSignInWithPolling() {
        try {
            val signInSuccess = acrolinxEndpoint.signInInteractive(interactiveCallback, timeoutMs = 10).get()
            fail("Should throw exception but was $signInSuccess")
        } catch (e: ExecutionException) {
            verify { interactiveCallback.onInteractiveUrl(match { it.startsWith(acrolinxUrl) }) }
        }
    }

    @Test
    fun testSignInWithPollingWithValidAuthToken() {
        val signInSuccess =
            acrolinxEndpoint.signInInteractive(interactiveCallback, timeoutMs = 10, accessToken = ACROLINX_API_TOKEN)
                .get()

        verify { interactiveCallback wasNot Called }

        assertThat(signInSuccess.user.username).isEqualTo(ACROLINX_API_USERNAME)
        assertThat(signInSuccess.accessToken.token).isNotEmpty()
        assertThat(signInSuccess.authorizedUsing).isEqualTo("ACROLINX_TOKEN")
        assertThat(signInSuccess.integration.properties["ca.filter"]).isNotNull()
    }

    @Test(expected = CancellationException::class)
    fun cancelSignInInteractive() {
        val signInSuccessFuture = acrolinxEndpoint.signInInteractive(interactiveCallback, timeoutMs = 2000)
        Thread.sleep(100)

        val cancelSuccess = signInSuccessFuture.cancel(true)
        assertTrue(cancelSuccess, "cancelSuccess")

        assertTrue(signInSuccessFuture.isCancelled, "isCancelled")
        assertTrue(signInSuccessFuture.isDone, "isDone")

        signInSuccessFuture.get()
    }
}
