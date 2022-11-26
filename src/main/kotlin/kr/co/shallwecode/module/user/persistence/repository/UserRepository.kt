package kr.co.shallwecode.module.user.persistence.repository

import kr.co.shallwecode.module.database.Database
import kr.co.shallwecode.module.user.controller.UserCreateRequest
import kr.co.shallwecode.module.user.persistence.table.UserModel
import kr.co.shallwecode.module.user.persistence.table.UserTable
import kr.co.shallwecode.module.user.persistence.table.UserTable.blogUrl
import kr.co.shallwecode.module.user.persistence.table.UserTable.email
import kr.co.shallwecode.module.user.persistence.table.UserTable.githubUrl
import kr.co.shallwecode.module.user.persistence.table.UserTable.id
import kr.co.shallwecode.module.user.persistence.table.UserTable.loginId
import kr.co.shallwecode.module.user.persistence.table.UserTable.name

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select


class UserRepository(private val database: Database) {

    /*
     user 조회
     */
    suspend fun findUser(loginIdParam: String, password: String): UserModel? {
        return database.dbQuery {
            UserTable.select {
                (loginId eq loginIdParam) and (UserTable.password eq password)
            }
                .map { it.toUser() }
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
            }.resultedValues?.singleOrNull()?.let { it.toUser() }
        }
    }

    private fun ResultRow.toUser(): UserModel {
        return UserModel(
            id = this[id],
            email = this[email],
            name = this[name],
            loginId = this[loginId],
            blogUrl = this[blogUrl],
            githubUrl = this[githubUrl]
        )
    }
}