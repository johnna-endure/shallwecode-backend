package kr.co.shallwecode.persistence.user.dao

import kr.co.shallwecode.persistence.DatabaseFactory
import kr.co.shallwecode.persistence.user.tables.UserModel
import kr.co.shallwecode.persistence.user.tables.UserTable
import kr.co.shallwecode.persistence.user.tables.toModel
import kr.co.shallwecode.routes.user.UserCreateRequest
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserDao {

    // user 조회
    suspend fun findUser(loginId: String, password: String) : UserModel? {
        return DatabaseFactory.dbQuery { UserTable.select {
            (UserTable.loginId eq loginId) and (UserTable.password eq password)
        }
            .map { UserTable.toModel(it) }
            .singleOrNull() }

    }

    // create
    suspend  fun createUser(request: UserCreateRequest) : UserModel? {
        return DatabaseFactory.dbQuery {UserTable.insert {
            it[email] = request.email
            it[name] = request.name ?: request.email
            it[password] = request.password
            it[loginId] = request.loginId
            it[blogUrl] = request.blogUrl
            it[githubUrl] = request.githubUrl
        }.resultedValues?.singleOrNull()?.let { UserTable.toModel(it) }}
    }
}