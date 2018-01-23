import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.application.receive
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.post
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.util.ValuesMap
import khttp.post
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CORS
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.routing.Routing

fun Application.main() {

    install(CORS){
        anyHost()
    }
    install(DefaultHeaders)
    install(Routing) {

        get("") {
            call.respondText("Hello World", ContentType.Text.JavaScript)
        }
        post("/invite_post/") {
            val vm = call.request.receive<ValuesMap>()
            val slackName = "blrkotlin"
            val slackToken = "YOUR_TOKEN"
            val url = "https://$slackName.slack.com/api/users.admin.invite"

            val payload = mapOf("token" to slackToken, "email" to vm.get("email"))
            val r = post(url, data = payload)
            println(r.jsonObject)

            if (r.jsonObject.getBoolean("ok")) {
                call.respondText("{\"status\":1}", ContentType.Text.JavaScript)
            } else {
                if (r.jsonObject.getString("error") == "already_invited") {
                    call.respondText("{\"status\":2}", ContentType.Text.JavaScript)
                } else {
                    call.respondText("{\"status\":3}", ContentType.Text.JavaScript)
                }
            }
        }
    }

}
