package kr.co.shallwecode.module.user.persistence.table

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val id = long("id").autoIncrement()
    val email = varchar("email", 100)
    val name = varchar("name", 100)
    val loginId = varchar("login_id", 100).uniqueIndex("user_loginId_unique_index")
    val password = varchar("password", 100)
    val blogUrl = varchar("blog_url", 100).nullable()
    val githubUrl = varchar("github_url", 100).nullable()

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class UserModel(
    val id: Long,
    val email: String,
    val name: String,
    val loginId: String,
    var blogUrl: String? = null,
    var githubUrl: String? = null
)
