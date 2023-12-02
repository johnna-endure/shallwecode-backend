package shallwecode.kr.database.table

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 사용자 마스터 테이블
 */
object UserTable : Table() {
    val id = long("id").autoIncrement()
    val email = varchar("email", 100).uniqueIndex("user_email_uindex")
    val name = varchar("name", 50)
    val githubUserId =
        long("github_user_id").references(ref = GithubUserTable.id, fkName = "user_github_user_id_fk").nullable()
    val deleted = bool("deleted").default(false)
    val created = datetime("created").default(LocalDateTime.now())
    val updated = datetime("updated").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
    override val tableName = "user"
    fun save(
        emailParam: String,
        nameParam: String,
        githubUserIdParam: Long?
    ): UserModel? {
        return UserTable.insert {
            it[email] = emailParam
            it[name] = nameParam
            it[githubUserId] = githubUserIdParam
            it[deleted] = false
        }.resultedValues?.singleOrNull()?.let { rowToModel(it) } ?: throw RuntimeException("create failed.")
    }

    private fun rowToModel(row: ResultRow): UserModel {
        return UserModel(
            id = row[id],
            email = row[email],
            name = row[name],
            githubUserId = row[githubUserId],
            deleted = row[deleted],
            created = row[created],
            updated = row[updated]
        )
    }
}

data class UserModel(
    val id: Long,
    val email: String,
    val name: String,
    val githubUserId: Long?,
    val deleted: Boolean,
    val created: LocalDateTime,
    val updated: LocalDateTime,
)
