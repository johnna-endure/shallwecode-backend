package kr.co.shallwecode.module.user.serivce

import kr.co.shallwecode.module.user.controller.AuthInfoCreateRequest
import kr.co.shallwecode.module.user.persistence.repository.AuthenticationRepository
import kr.co.shallwecode.module.user.persistence.table.AuthenticationModel

class AuthenticationService(private val authenticationRepository: AuthenticationRepository) {

    suspend fun findAuth(loginId: String, password: String): AuthenticationModel? {
        return authenticationRepository.find(loginId, password)
    }

    suspend fun createAuthInfo(request: AuthInfoCreateRequest): AuthenticationModel? {
        return authenticationRepository.create(request)
    }
}