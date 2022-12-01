package kr.co.shallwecode.module.user.persistence.repository

import kr.co.shallwecode.module.database.Database
import kr.co.shallwecode.module.user.controller.AuthInfoCreateRequest
import kr.co.shallwecode.module.user.persistence.table.AuthenticationModel
import kr.co.shallwecode.module.user.persistence.table.AuthenticationTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class AuthenticationRepository(private val database: Database) {

    suspend fun find(loginIdParam: String, passwordParam: String): AuthenticationModel? {
        return database.dbQuery {
            AuthenticationTable.select {
                (AuthenticationTable.loginId eq loginIdParam) and (AuthenticationTable.password eq passwordParam)
            }
                .map { it.toAuthentication() }
                .singleOrNull()
        }
    }

    // create
    suspend fun create(request: AuthInfoCreateRequest): AuthenticationModel? {
        return database.dbQuery {
            AuthenticationTable.insert {
                it[userId] = request.userId
                it[from] = request.from ?: throw IllegalArgumentException("from column value is empty.")
                it[loginId] = request.loginId
                it[password] = request.password
            }.resultedValues?.singleOrNull()?.toAuthentication()
        }
    }

    private fun ResultRow.toAuthentication(): AuthenticationModel {
        return AuthenticationModel(
            id = this[AuthenticationTable.id],
            userId = this[AuthenticationTable.userId],
            from = this[AuthenticationTable.from],
            loginId = this[AuthenticationTable.loginId],
            password = this[AuthenticationTable.password]
        )
    }
}