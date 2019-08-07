package com.acrolinx.client.sdk.check


import kotlinx.serialization.Serializable

@Serializable
class CheckResult(val id: String, val quality: Quality, val reports: Map<String, Report>, val issues: List<Issue>) {
    fun getReport(reportType: ReportType): Report? {
        return reports[reportType.toString()]
    }

    @Serializable
    class Report(val displayName: String, val link: String)
}

@Serializable
data class Quality(val score: Int, val status: Status) {
    enum class Status { red, yellow, green }
}
