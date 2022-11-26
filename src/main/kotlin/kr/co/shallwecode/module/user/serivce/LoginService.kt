package kr.co.shallwecode.module.user.serivce

import kr.co.shallwecode.module.user.controller.LoginRequest
import kr.co.shallwecode.module.user.persistence.table.UserModel
import kr.co.shallwecode.module.user.persistence.repository.UserRepository

class LoginService(private val userRepository: UserRepository) {

    suspend fun login(request: LoginRequest): UserModel? {
        return userRepository.findUser(request.loginId, request.password)
    }

}