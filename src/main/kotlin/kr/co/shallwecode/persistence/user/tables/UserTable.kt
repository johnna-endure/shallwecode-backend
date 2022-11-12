package kr.co.shallwecode.persistence.user.tables
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object UserTable : Table(){
    val id = integer("id").autoIncrement()
    val email = varchar("email", 100)
    val name = varchar("name", 100)
    val loginId = varchar("login_id", 100).uniqueIndex("user_loginId_unique_index")
    val password = varchar("password", 100)
    val blogUrl = varchar("blog_url", 100).nullable()
    val githubUrl = varchar("github_url", 100).nullable()

    override val primaryKey = PrimaryKey(id)
}

fun UserTable.toModel(row: ResultRow) : UserModel {
    return UserModel(
        id = row[id],
        email = row[email],
        name = row[name],
        loginId = row[loginId],
        blogUrl = row[blogUrl],
        githubUrl = row[githubUrl]
    )
}


@Serializable
data class UserModel(
    val id: Int,
    val email: String,
    val name: String,
    val loginId: String,
    val blogUrl: String?,
    val githubUrl: String?
)
