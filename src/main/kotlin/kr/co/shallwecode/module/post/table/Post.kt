package kr.co.shallwecode.module.post.table

import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.user.table.User
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Post : Table() {
    val id = long("id").autoIncrement()
    val userId = long("id") references User.id
    val content = text("content")
    val deleted = bool("deleted").default(false)
    val created = datetime("created").defaultExpression(CurrentDateTime())
    val updated = datetime("updated").defaultExpression(CurrentDateTime())
}


@Serializable
data class PostModel(
    val id: Long,
    val userId: Long,
    val content: String,
    val deleted: Boolean,
    val created: String,
    val updated: String,
)