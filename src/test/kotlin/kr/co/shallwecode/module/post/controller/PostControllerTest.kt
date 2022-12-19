package kr.co.shallwecode.module.post.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kr.co.shallwecode.constant.TestEnvironmentConstant.*
import kr.co.shallwecode.module.post.postModule
import kr.co.shallwecode.module.post.postRouting
import kr.co.shallwecode.module.post.service.PostService
import kr.co.shallwecode.plugins.configureSecurity
import kr.co.shallwecode.plugins.configureSerialization
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PostControllerTest {
    private lateinit var postService: PostService

    @BeforeTest
    fun beforeTest() {
        postService = mockk()
    }

    @Test
    fun post_create_failed() = testApplication {
        // given
        initDependency(this) {
            bind(overrides = true) { singleton { postService } }
        }
        val client = createClient(this)

        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // when
        coEvery { postService.create(any(), any()) } throws RuntimeException("test exception")

        //then
        val validToken = JWT.create()
            .withIssuer(JWT_ISSUER.value)
            .withClaim("userId", 1L)
            .withExpiresAt(Date(System.currentTimeMillis() + JWT_EXP.value.toLong()))
            .sign(Algorithm.HMAC256(JWT_SECRET.value))

        val response = client.post("/post") {
            contentType(ContentType.Application.Json)
            bearerAuth(validToken)
            setBody(
                PostSaveRequest(
                    title = "hello",
                    content = "world"
                )
            )
        }

        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }

    @Test
    fun post_create_success() = testApplication {
        // given
        initDependency(this) {
            bind(overrides = true) { singleton { postService } }
        }
        val client = createClient(this)

        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // when
        val expectedPostId = 1L
        coEvery { postService.create(any(), any()) } returns (expectedPostId)


        //then
        val validToken = JWT.create()
            .withIssuer(JWT_ISSUER.value)
            .withClaim("userId", 1L)
            .withExpiresAt(Date(System.currentTimeMillis() + JWT_EXP.value.toLong()))
            .sign(Algorithm.HMAC256(JWT_SECRET.value))

        val response = client.post("/post") {
            contentType(ContentType.Application.Json)
            bearerAuth(validToken)
            setBody(
                PostSaveRequest(
                    title = "hello",
                    content = "world"
                )
            )
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expectedPostId, response.body())
    }

    private fun initDependency(applicationTestBuilder: ApplicationTestBuilder, mocks: DI.MainBuilder.() -> Unit) {
        applicationTestBuilder.application {
            di {
                import(postModule)
                mocks()
            }
            postRouting()
            configureSerialization()
            configureSecurity()
        }
    }

    private fun createClient(applicationTestBuilder: ApplicationTestBuilder): HttpClient {
        return applicationTestBuilder.createClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }
}