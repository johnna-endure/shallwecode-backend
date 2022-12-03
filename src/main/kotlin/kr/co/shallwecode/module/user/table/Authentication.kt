package kr.co.shallwecode.module.user.table

import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.user.controller.AuthInfoCreateRequest
import org.jetbrains.exposed.sql.*


object Authentication : Table() {
    val id = long("id").autoIncrement()
    val userId = long("user_id") references User.id
    val from = varchar("from", 50)
    val loginId = varchar("login_id", 100)
    val password = varchar("password", 100)

    override val primaryKey = PrimaryKey(firstColumn = id, name = "pk_authentication")
    override val tableName: String = "authentication"
}


fun Authentication.find(loginIdParam: String, passwordParam: String): AuthenticationModel? {
    return Authentication.select {
        (loginId eq loginIdParam) and (password eq passwordParam)
    }
        .map { it.toAuthentication() }
        .singleOrNull()
}

fun Authentication.create(request: AuthInfoCreateRequest): AuthenticationModel {
    return Authentication.insert {
        it[userId] = request.userId
        it[from] = request.from ?: throw IllegalArgumentException("from column value is empty.")
        it[loginId] = request.loginId
        it[password] = request.password
    }.resultedValues?.singleOrNull()?.toAuthentication() ?: throw RuntimeException("authentication create failed")
}

@Serializable
data class AuthenticationModel(
    val id: Long,
    val userId: Long,
    val from: String,
    val loginId: String,
    val password: String
)

private fun ResultRow.toAuthentication(): AuthenticationModel {
    return AuthenticationModel(
        id = this[Authentication.id],
        userId = this[Authentication.userId],
        from = this[Authentication.from],
        loginId = this[Authentication.loginId],
        password = this[Authentication.password]
    )
}