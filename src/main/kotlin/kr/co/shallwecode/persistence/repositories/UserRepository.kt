package kr.co.shallwecode.persistence.repositories

import kr.co.shallwecode.persistence.tables.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class UserRepository {

    // user 조회
    suspend fun getUser(loginId: String, password: String) : UserModel {
        return UserTable.select {
            (UserTable.loginId eq loginId) and (UserTable.password eq password)
        }
            .map { UserTable.toModel(it) }
            .single()
    }

    // create
    suspend fun createUser() {

    }
}