package kr.co.shallwecode.module.post.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.constant.JwtConstant
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
                    val userId = principal?.getClaim(JwtConstant.CLAIM_USER_ID.value, Long::class)
                        ?: return@post call.respond(HttpStatusCode.Unauthorized, "empty userId in token")

                    val request = call.receive<PostSaveRequest>()
                    try {
                        val postId = postService.create(request, userId)
                        call.respond(postId)
                    } catch (ex: Exception) {
                        logger.error("post create failed : ${ex.stackTraceToString()}")
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }

            route("/posts/{postId}", HttpMethod.Put) {
                put {
                    val postId = call.parameters["postId"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim(JwtConstant.CLAIM_USER_ID.value, Long::class)
                        ?: return@put call.respond(HttpStatusCode.Unauthorized, "empty userId in token")

                    val request = call.receive<PostSaveRequest>()
                    try {
                        postService.modify(request, userId, postId.toLong())
                        call.respond(HttpStatusCode.OK)
                    } catch (ex: Exception) {
                        logger.error("post update failed : ${ex.stackTraceToString()}")
                        call.respond(HttpStatusCode.InternalServerError)
                    }
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
data class PostSaveRequest(
    val title: String,
    val content: String,
)