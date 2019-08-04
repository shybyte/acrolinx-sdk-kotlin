package integration

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.matchesPredicate
import check.CheckOptions
import check.CheckRequest
import check.ContentEncoding
import check.DocumentDescriptorRequest
import integration.common.HasApiTokenTest
import platform.GuidanceProfile
import kotlin.test.BeforeTest
import kotlin.test.Test


class CheckTest : HasApiTokenTest() {
    private lateinit var guidanceProfile: GuidanceProfile

    @BeforeTest
    fun beforeTest() {
        guidanceProfile = acrolinxEndpoint.getCapabilities(apiToken)
            .checkingCapabilities.guidanceProfiles.find { it.language.id == "en" }!!
    }

    @Test
    fun testGetPlatformInformation() {
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
}
