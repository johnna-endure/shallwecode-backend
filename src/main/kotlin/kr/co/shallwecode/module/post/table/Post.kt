package kr.co.shallwecode.module.post.table

import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.post.controller.PostSaveRequest
import kr.co.shallwecode.module.user.table.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.format.DateTimeFormatter

object Post : Table() {
    val id = long("id").autoIncrement()
    val userId = long("id") references User.id
    val title = varchar("title", 100)
    val content = text("content")
    val deleted = bool("deleted").default(false)
    val created = datetime("created").defaultExpression(CurrentDateTime())
    val updated = datetime("updated").defaultExpression(CurrentDateTime())
}

fun create(request: PostSaveRequest, userId: Long): Long {
    return Post.insert {
        it[Post.userId] = userId
        it[title] = request.title
        it[content] = request.content
    }.resultedValues?.singleOrNull()?.toPost()?.id ?: throw RuntimeException("post create failed")
}

fun Post.modify(request: PostSaveRequest, userIdParam: Long, postIdParam: Long): Int {
    return Post.update({
        (userId eq userIdParam) and (id eq postIdParam)
    }) {
        it[title] = request.title
        it[content] = request.content
    }
}

fun Post.softDelete(postId: Long, userIdParam: Long) {
    Post.update({ (id eq postId) and (userId eq userIdParam) }) {
        it[deleted] = true
    }
}

@Serializable
data class PostModel(
    val id: Long,
    val userId: Long,
    val title: String,
    val content: String,
    val created: String,
    val updated: String,
)

private fun ResultRow.toPost(): PostModel {
    return PostModel(
        id = this[Post.id],
        userId = this[Post.userId],
        title = this[Post.title],
        content = this[Post.content],
        created = this[Post.created].format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        updated = this[Post.updated].format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    )
}
