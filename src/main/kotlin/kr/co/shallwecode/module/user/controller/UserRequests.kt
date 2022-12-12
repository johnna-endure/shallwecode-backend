package kr.co.shallwecode.module.user.controller

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val loginId: String,
    val password: String
)

@Serializable
data class UserRegisterRequest(
    val password: String,
    val email: String,
    val loginId: String,
    val name: String? = null,
    val blogUrl: String? = null,
    val githubUrl: String? = null
)

@Serializable
data class AuthInfoCreateRequest(
    val userId: Long,
    var from: String? = null,
    val loginId: String,
    val password: String
)
