package check

import kotlinx.serialization.Serializable

@Serializable
class CheckResponse(val data: CheckResponseData, val links: CheckResponseLinks) {
    @Serializable
    class CheckResponseLinks(val result: String, val cancel: String)

    @Serializable
    class CheckResponseData(val id: String)
}
