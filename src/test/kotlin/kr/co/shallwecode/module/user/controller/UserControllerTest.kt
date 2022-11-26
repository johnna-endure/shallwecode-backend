package kr.co.shallwecode.module.user.controller

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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

//    @MockK
//    lateinit var userRepository: UserRepository

    //    @InjectMockKs
    @MockK
    lateinit var loginService: LoginService

    @BeforeTest
    fun beforeTest() {
        MockKAnnotations.init(this)
        coEvery { loginService.login(any()) } returns (UserModel(
            id = 2,
            email = "hello",
            name = "world",
            loginId = "loginId"
        ))

        println("beforeTest")
    }

    @Test
    fun loginTest() = testApplication {

        application {
            di {
                import(userModule, true)
                bind<LoginService>(overrides = true) { singleton { loginService } }
            }
            userRouting()
            configureSerialization()
        }


        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest("loginId", "1234"))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}