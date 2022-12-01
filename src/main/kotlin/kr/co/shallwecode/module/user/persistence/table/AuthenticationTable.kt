package kr.co.shallwecode.module.user.persistence.table

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


object AuthenticationTable : Table() {
    val id = long("id").autoIncrement()
    val userId = long("user_id") references UserTable.id
    val from = varchar("from", 50)
    val loginId = varchar("login_id", 100)
    val password = varchar("password", 100)

    override val primaryKey = PrimaryKey(firstColumn = id, name = "pk_authentication")
    override val tableName: String = "authentication"
}

@Serializable
data class AuthenticationModel(
    val id: Long,
    val userId: Long,
    val from: String,
    val loginId: String,
    val password: String
)