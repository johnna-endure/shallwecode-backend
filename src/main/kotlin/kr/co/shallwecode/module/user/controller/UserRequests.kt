package kr.co.shallwecode.module.user.controller

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateRequest(
    val password: String,
    val email: String,
    val loginId: String,
    val name: String? = null,
    val blogUrl: String? = null,
    val githubUrl: String? = null
)

@Serializable
data class LoginRequest(
    val loginId: String,
    val password: String
)