package kr.co.shallwecode.module.user.controller

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kr.co.shallwecode.module.user.persistence.table.UserModel
import kr.co.shallwecode.module.user.serivce.AuthenticationService
import kr.co.shallwecode.module.user.serivce.UserService
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

    private lateinit var authenticationService: AuthenticationService

    private lateinit var userService: UserService

    @BeforeTest
    fun beforeTest() {
        authenticationService = mockk()
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
                bind(overrides = true) { singleton { authenticationService } }
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
            id = 2,
            email = "hello",
            name = "world",
            loginId = "loginId"
        )


        // when
        coEvery { authenticationService.login(any()) } returns (user)

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
    fun login_failed_not_matched_credentials() = testApplication {
        // given
        application {
            di {
                import(userModule)
                bind(overrides = true) { singleton { authenticationService } }
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

        // when
        coEvery { authenticationService.login(any()) } returns (null)


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
        coEvery { userService.createUser(any()) } returns (null)


        val response = client.post("/user") {
            contentType(ContentType.Application.Json)
            setBody(
                UserCreateRequest(
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

        val expectedBody = UserModel(
            id = 2,
            email = "hello",
            name = "world",
            loginId = "loginId"
        )

        // when
        coEvery { userService.createUser(any()) } returns (expectedBody)


        val response = client.post("/user") {
            contentType(ContentType.Application.Json)
            setBody(
                UserCreateRequest(
                    password = "password",
                    email = "email",
                    loginId = "loginId"
                )
            )
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expectedBody, response.body())
    }

}

