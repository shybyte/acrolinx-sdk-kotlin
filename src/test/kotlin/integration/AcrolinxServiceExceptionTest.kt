package integration

import com.acrolinx.client.sdk.AccessToken
import com.acrolinx.client.sdk.exceptions.AcrolinxServiceException
import integration.common.BaseIntegrationTest
import kotlin.test.Test
import kotlin.test.assertEquals


class AcrolinxServiceExceptionTest : BaseIntegrationTest() {
    @Test(expected = AcrolinxServiceException::class)
    fun testInvalidAuthException() {
        try {
            acrolinxEndpoint.getCapabilities(AccessToken("invalid"))
        } catch (serviceException: AcrolinxServiceException) {
            assertEquals(AcrolinxServiceException.Type.auth.toString(), serviceException.error.type)
            throw serviceException
        }
    }
}
