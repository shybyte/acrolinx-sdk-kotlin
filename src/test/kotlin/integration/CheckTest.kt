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
import integration.common.HasApiTokenTest
import io.mockk.mockk
import io.mockk.verify
import platform.GuidanceProfile
import kotlin.test.BeforeTest
import kotlin.test.Test


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
}
