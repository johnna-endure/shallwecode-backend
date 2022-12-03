package kr.co.shallwecode.module.user.serivce

import io.ktor.server.plugins.*
import kr.co.shallwecode.module.database.Database
import kr.co.shallwecode.module.user.constant.AuthenticationSource
import kr.co.shallwecode.module.user.controller.AuthInfoCreateRequest
import kr.co.shallwecode.module.user.controller.UserRegisterRequest
import kr.co.shallwecode.module.user.table.*

class UserService(
    private val database: Database
) {
    suspend fun login(loginIdParam: String, passwordParam: String): UserModel {
        return database.dbQuery {
            val authInfo = Authentication.find(loginIdParam, passwordParam)
            val userId = authInfo?.userId ?: throw NotFoundException("authInfo is null")
            return@dbQuery User.find(userId) ?: throw NotFoundException("not found user info")
        }
    }

    suspend fun register(request: UserRegisterRequest): Long {
        return database.dbQuery {
            val user = User.create(request)
            Authentication.create(
                AuthInfoCreateRequest(
                    userId = user.id,
                    from = AuthenticationSource.SHALL_WE_CODE.name,
                    loginId = request.loginId,
                    password = request.password
                )
            )
            return@dbQuery user.id
        }
    }
}