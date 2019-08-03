package manual

import com.acrolinx.client.sdk.InteractiveCallback
import integration.common.createTestAcrolinxEndpoint
import java.net.URL

object SignInInteractiveExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val endpoint = createTestAcrolinxEndpoint()

        val signInSuccess = endpoint.signInInteractive(object : InteractiveCallback<URL> {
            override fun run(url: URL) {
                println("Please open the following URL: $url")
            }
        })

        println("accessToken = " + signInSuccess.accessToken.token)
        println("username = " + signInSuccess.user.username)
    }
}
