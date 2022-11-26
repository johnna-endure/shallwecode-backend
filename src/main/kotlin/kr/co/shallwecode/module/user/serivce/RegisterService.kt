package kr.co.shallwecode.module.user.serivce

import kr.co.shallwecode.module.user.controller.UserCreateRequest
import kr.co.shallwecode.module.user.persistence.repository.UserRepository
import kr.co.shallwecode.module.user.persistence.table.UserModel

class RegisterService(private val userRepository: UserRepository) {

    suspend fun register(request: UserCreateRequest): UserModel? {
        return userRepository.createUser(request)
    }
}