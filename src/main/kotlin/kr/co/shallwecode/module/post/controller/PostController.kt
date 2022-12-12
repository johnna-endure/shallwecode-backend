package kr.co.shallwecode.module.post.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.post.service.PostService
import kr.co.shallwecode.plugins.AuthenticateName
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class PostController(application: Application) : AbstractDIController(application) {
    private val postService: PostService by instance()
    private val logger = application.environment.log

    override fun Route.getRoutes() {

        route("/posts", HttpMethod.Get) {

        }

        route("/posts/{postId}", HttpMethod.Get) {

        }

        authenticate(AuthenticateName.AUTH_JWT.name) {
            route("/post") {
                post {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", Long::class)
                        ?: return@post call.respond(HttpStatusCode.Unauthorized, "empty userId in token")

                    val request = call.receive<PostCreateRequest>()
                    try {
                        val postId = postService.create(request, userId)
                        call.respond(postId)
                    } catch (ex: Exception) {
                        logger.debug("login failed : ${ex.stackTraceToString()}")
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }

            route("/posts/{postId}", HttpMethod.Put) {
                put {

                }

                delete {

                }
            }


            route("/post/{postId}/like", HttpMethod.Post) {

            }
        }
    }
}

@Serializable
data class PostCreateRequest(
    val title: String,
    val content: String,
)