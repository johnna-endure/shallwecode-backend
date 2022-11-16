package kr.co.shallwecode.module.user.persistence.repository

import kr.co.shallwecode.module.database.Database
import kr.co.shallwecode.module.user.persistence.repository.UserModel
import kr.co.shallwecode.module.user.persistence.repository.UserTable
import kr.co.shallwecode.module.user.persistence.repository.toModel
import kr.co.shallwecode.routes.user.UserCreateRequest
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

/**
 * repository에서는 table 객체 자체를 반환하도록 한다.
 */
class UserRepository(private val database: Database) {

    /*
     user 조회
     */
    suspend fun findUser(loginId: String, password: String): UserModel? {
        return database.dbQuery {
            UserTable.select {
                (UserTable.loginId eq loginId) and (UserTable.password eq password)
            }
                .map { UserTable.toModel(it) }
                .singleOrNull()
        }

    }

    // create
    suspend fun createUser(request: UserCreateRequest): UserModel? {
        return database.dbQuery {
            UserTable.insert {
                it[email] = request.email
                it[name] = request.name ?: request.email
                it[password] = request.password
                it[loginId] = request.loginId
                it[blogUrl] = request.blogUrl
                it[githubUrl] = request.githubUrl
            }.resultedValues?.singleOrNull()?.let { UserTable.toModel(it) }
        }
    }
}