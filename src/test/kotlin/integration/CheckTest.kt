package integration

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.matchesPredicate
import com.acrolinx.client.sdk.ProgressListener
import com.acrolinx.client.sdk.check.CheckOptions
import com.acrolinx.client.sdk.check.CheckRequest
import com.acrolinx.client.sdk.check.ContentEncoding
import com.acrolinx.client.sdk.check.DocumentDescriptorRequest
import com.acrolinx.client.sdk.platform.GuidanceProfile
import integration.common.HasApiTokenTest
import io.mockk.mockk
import io.mockk.verify
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

private val LONG_TEST_TEXT = "This text is fine.\n".repeat(1000)

class CheckTest : HasApiTokenTest() {
    private lateinit var guidanceProfile: GuidanceProfile
    private val progressListener = mockk<ProgressListener>(relaxed = true)

    @BeforeTest
    fun beforeTest() {
        guidanceProfile = acrolinxEndpoint.getCapabilities(apiToken)
            .checkingCapabilities.guidanceProfiles.find { it.language.id == "en" }!!
    }

    @Test
    fun testCheck() {
        val checkResponse = acrolinxEndpoint.check(
            apiToken,
            CheckRequest(
                content = "This textt has ann erroor.",
                contentEncoding = ContentEncoding.none,
                checkOptions = CheckOptions(guidanceProfileId = guidanceProfile.id),
                document = DocumentDescriptorRequest("filename.txt")
            )
        )

        assertThat(checkResponse.data.id).isNotNull().isNotEmpty()
        assertThat(checkResponse.links.result).matchesPredicate { it.startsWith(acrolinxUrl) }
        assertThat(checkResponse.links.result).matchesPredicate { it.startsWith(acrolinxUrl) }
    }

    @Test
    fun testCheckAndGetResult() {
        val checkResult = acrolinxEndpoint.checkAndGetResult(
            apiToken,
            CheckRequest(
                content = "This textt has ann erroor.",
                contentEncoding = ContentEncoding.none,
                checkOptions = CheckOptions(guidanceProfileId = guidanceProfile.id),
                document = DocumentDescriptorRequest("filename.txt")
            ),
            progressListener
        )

        verify { progressListener.onProgress(match { it.percent in 0.0..100.0 }) }
        assertThat(checkResult.quality.score).matchesPredicate { it in 50..60 }
    }

    @Test(expected = CancellationException::class)
    fun cancelCheck() {
        val executorService = Executors.newFixedThreadPool(1)
        val checkFuture = executorService.submit {
            acrolinxEndpoint.checkAndGetResult(
                apiToken,
                CheckRequest(
                    content = LONG_TEST_TEXT,
                    contentEncoding = ContentEncoding.none,
                    checkOptions = CheckOptions(guidanceProfileId = guidanceProfile.id),
                    document = DocumentDescriptorRequest("filename.txt")
                ),
                progressListener
            )
        }

        Thread.sleep(100)

        val cancelSuccess = checkFuture.cancel(true)
        assertTrue(cancelSuccess, "cancelSuccess")

        assertTrue(checkFuture.isCancelled, "isCancelled")
        assertTrue(checkFuture.isDone, "isDone")

        Thread.sleep(500)  // Give some time to cancel on server.

        checkFuture.get()
    }
}
