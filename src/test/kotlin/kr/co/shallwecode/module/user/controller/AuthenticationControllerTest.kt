package kr.co.shallwecode.module.user.controller

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kr.co.shallwecode.module.user.constant.AuthenticationSource
import kr.co.shallwecode.module.user.persistence.table.AuthenticationModel
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


class AuthenticationControllerTest {


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

        val authInfo = AuthenticationModel(
            id = 1L,
            userId = 1L,
            from = AuthenticationSource.SHALL_WE_CODE.name,
            loginId = "testId",
            password = "testPassword"
        )


        // when
        coEvery { authenticationService.findAuth(any(), any()) } returns (authInfo)
        coEvery { userService.find(1L) } returns (user)

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
        coEvery { authenticationService.findAuth(any(), any()) } returns (null)


        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest("loginId", "1234"))
        }

        // then
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

}