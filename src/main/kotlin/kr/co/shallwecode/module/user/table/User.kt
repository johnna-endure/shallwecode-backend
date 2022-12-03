package kr.co.shallwecode.module.user.table

import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.user.controller.UserRegisterRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

object User : Table() {
    val id = long("id").autoIncrement()
    val email = varchar("email", 100)
    val name = varchar("name", 100)
    val blogUrl = varchar("blog_url", 100).nullable()
    val githubUrl = varchar("github_url", 100).nullable()

    override val primaryKey = PrimaryKey(firstColumn = id, name = "pk_user")
    override val tableName: String = "user"
}

fun User.find(userId: Long): UserModel? {
    return User.select {
        id eq userId
    }
        .map { it.toUser() }
        .singleOrNull()
}

fun User.create(request: UserRegisterRequest): UserModel {
    return User.insert {
        it[email] = request.email
        it[name] = request.name ?: request.email
        it[blogUrl] = request.blogUrl
        it[githubUrl] = request.githubUrl
    }.resultedValues?.singleOrNull()?.toUser() ?: throw RuntimeException("user create failed.")
}

@Serializable
data class UserModel(
    val id: Long,
    val email: String,
    val name: String,
    var blogUrl: String? = null,
    var githubUrl: String? = null
)

private fun ResultRow.toUser(): UserModel {
    return UserModel(
        id = this[User.id],
        email = this[User.email],
        name = this[User.name],
        blogUrl = this[User.blogUrl],
        githubUrl = this[User.githubUrl]
    )
}

