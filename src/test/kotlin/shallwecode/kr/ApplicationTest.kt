package shallwecode.kr

import io.ktor.server.testing.*
import kotlin.test.*
import shallwecode.kr.database.table.GithubUserModel
import java.time.LocalDateTime
import kotlin.reflect.full.memberProperties

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
//        application {
//            configureRouting()
//        }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//            assertEquals("Hello World!", bodyAsText())
//        }
    }

    @Test
    fun printKeys() {
        val model = GithubUserModel(
            id = 1L,
            login = "test",
            avatarUrl = "",
            htmlUrl = "",
            email = "",
            followers = 0,
            collaborators = 0,
            createdAt = LocalDateTime.now(),
            following = 0,
            privateGists = 0,
            publicGists = 0,
            publicRepos = 0,
            ownedPrivateRepos = 0,
            updatedAt = LocalDateTime.now(),
            totalPrivateRepos = 0
        )
        val keys = GithubUserModel::class.memberProperties

        for (key in keys) {
            println("${key.name} : ${key.get(model)}")
        }
    }
}
