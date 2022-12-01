package kr.co.shallwecode.module.user.persistence.repository

import kr.co.shallwecode.module.database.Database
import kr.co.shallwecode.module.user.controller.UserCreateRequest
import kr.co.shallwecode.module.user.persistence.table.UserModel
import kr.co.shallwecode.module.user.persistence.table.UserTable
import kr.co.shallwecode.module.user.persistence.table.UserTable.blogUrl
import kr.co.shallwecode.module.user.persistence.table.UserTable.email
import kr.co.shallwecode.module.user.persistence.table.UserTable.githubUrl
import kr.co.shallwecode.module.user.persistence.table.UserTable.id
import kr.co.shallwecode.module.user.persistence.table.UserTable.name
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select


class UserRepository(private val database: Database) {

    /*
     user 조회
     */
    suspend fun find(idParam: Long): UserModel? {
        return database.dbQuery {
            UserTable.select {
                (id eq idParam)
            }
                .map { it.toUser() }
                .singleOrNull()
        }

    }

    // create
    suspend fun create(request: UserCreateRequest): UserModel? {
        return database.dbQuery {
            UserTable.insert {
                it[email] = request.email
                it[name] = request.name ?: request.email
                it[blogUrl] = request.blogUrl
                it[githubUrl] = request.githubUrl
            }.resultedValues?.singleOrNull()?.toUser()
        }
    }

    private fun ResultRow.toUser(): UserModel {
        return UserModel(
            id = this[id],
            email = this[email],
            name = this[name],
            blogUrl = this[blogUrl],
            githubUrl = this[githubUrl]
        )
    }
}