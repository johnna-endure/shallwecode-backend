package kr.co.shallwecode.module.user.serivce

import kr.co.shallwecode.module.user.controller.AuthInfoCreateRequest
import kr.co.shallwecode.module.user.controller.LoginRequest
import kr.co.shallwecode.module.user.persistence.repository.AuthenticationRepository
import kr.co.shallwecode.module.user.persistence.table.AuthenticationModel

class AuthenticationService(private val authenticationRepository: AuthenticationRepository) {

    suspend fun login(request: LoginRequest): AuthenticationModel? {
        return authenticationRepository.find(request.loginId, request.password)
    }
    
    suspend fun createAuthInfo(request: AuthInfoCreateRequest): AuthenticationModel? {
        return authenticationRepository.create(request)
    }
}