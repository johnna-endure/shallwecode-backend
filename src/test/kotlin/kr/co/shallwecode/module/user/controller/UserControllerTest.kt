package kr.co.shallwecode.module.user.controller

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kr.co.shallwecode.module.user.persistence.table.UserModel
import kr.co.shallwecode.module.user.serivce.LoginService
import kr.co.shallwecode.module.user.userModule
import kr.co.shallwecode.module.user.userRouting
import kr.co.shallwecode.plugins.configureSerialization
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class UserControllerTest {

    private lateinit var loginService: LoginService

    @BeforeTest
    fun beforeTest() {
        loginService = mockk()
    }

    @Test
    fun loginTest() = testApplication {
        // given
        application {
            di {
                import(userModule)
                bind(overrides = true) { singleton { loginService } }
            }
            userRouting()
            configureSerialization()
        }
        val client = createClient(this)

        val expectedBody = UserModel(
            id = 2,
            email = "hello",
            name = "world",
            loginId = "loginId"
        )

        // when
        coEvery { loginService.login(any()) } returns (expectedBody)

        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest("loginId", "1234"))
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(response.body(), expectedBody)
    }

    private fun createClient(
        applicationTestBuilder: ApplicationTestBuilder

    ): HttpClient {
        return applicationTestBuilder.createClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }
}

