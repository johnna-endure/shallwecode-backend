package kr.co.shallwecode.module.user.controller

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kr.co.shallwecode.module.user.serivce.UserService
import kr.co.shallwecode.module.user.table.UserModel
import kr.co.shallwecode.module.user.userModule
import kr.co.shallwecode.module.user.userRouting
import kr.co.shallwecode.plugins.configureSecurity
import kr.co.shallwecode.plugins.configureSerialization
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class UserControllerTest {
    private lateinit var userService: UserService

    @BeforeTest
    fun beforeTest() {
        userService = mockk()
    }


    /**
    로그인에 성공하는 경우
     */
    @Test
    fun login_success() = testApplication {
        // given
        application {
            di {
                import(userModule)
                bind(overrides = true) { singleton { userService } }
            }
            configureSecurity()
            userRouting()
            configureSerialization()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val user = UserModel(
            id = 1L,
            email = "hello",
            name = "world",
        )

        // when
        coEvery { userService.login(any(), any()) } returns (user)

        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest("loginId", "1234"))
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(response.body<LoginResponse>().user, user)
        assertNotNull(response.body<LoginResponse>().token)
    }

    /**
     * 인증정보가 맞지 않아 로그인에 실패하는 경우
     */
    @Test
    fun login_failed_auth_is_null() = testApplication {
        // given
        application {
            di {
                import(userModule)
            }

            userRouting()
            configureSecurity()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        coEvery { userService.login(any(), any()) } throws RuntimeException()

        // when
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest("loginId", "1234"))
        }

        // then
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }


    /**
     * 회원 가입에 성공하는 경우
     */
    @Test
    fun register_failed() = testApplication {
        // given
        application {
            di {
                import(userModule)
                bind(overrides = true) { singleton { userService } }
            }
            userRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        coEvery { userService.register(any()) } throws RuntimeException("test exception")


        val response = client.post("/user") {
            contentType(ContentType.Application.Json)
            setBody(
                UserRegisterRequest(
                    password = "password",
                    email = "email",
                    loginId = "loginId"
                )
            )
        }

        // then
        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }

    /**
     * 회원 가입에 성공하는 경우
     */
    @Test
    fun register_success() = testApplication {
        // given
        application {
            di {
                import(userModule)
                bind(overrides = true) { singleton { userService } }
            }
            userRouting()
            configureSerialization()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // when
        coEvery { userService.register(any()) } returns (2L)

        val response = client.post("/user") {
            contentType(ContentType.Application.Json)
            setBody(
                UserRegisterRequest(
                    password = "password",
                    email = "email",
                    loginId = "loginId"
                )
            )
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(UserCreateResponse(2L), response.body())
    }

}

