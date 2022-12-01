package kr.co.shallwecode.module.user.persistence.table

import org.jetbrains.exposed.sql.Table


object AuthenticationTable : Table() {
    val id = long("id").autoIncrement()
    val userId = long("user_id") references UserTable.id
    val from = varchar("from", 50)
    val loginId = varchar("login_id", 100).uniqueIndex("authentication_login_id_unique_index")
    val password = varchar("password", 100)

    override val primaryKey = PrimaryKey(id)
    override val tableName: String = "authentication"
}

data class AuthenticationModel(
    val id: Long,
    val userId: Long,
    val from: String,
    val loginId: String,
    val password: String
)