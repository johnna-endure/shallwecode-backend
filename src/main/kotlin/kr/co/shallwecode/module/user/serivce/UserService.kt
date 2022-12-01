package kr.co.shallwecode.module.user.serivce

import kr.co.shallwecode.module.user.controller.UserCreateRequest
import kr.co.shallwecode.module.user.persistence.repository.UserRepository
import kr.co.shallwecode.module.user.persistence.table.UserModel

class UserService(private val userRepository: UserRepository) {

    suspend fun createUser(request: UserCreateRequest): UserModel? {
        return userRepository.create(request)
    }

    suspend fun find(userId: Long): UserModel? {
        return userRepository.find(userId)
    }
}