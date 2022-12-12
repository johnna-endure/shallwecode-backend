package kr.co.shallwecode.module.user.controller

import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.user.table.UserModel

@Serializable
data class LoginResponse(val user: UserModel, val token: String)

@Serializable
data class UserCreateResponse(val userId: Long)



