package shallwecode.kr.database.table

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Table.Dual.nullable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 사용자 마스터 테이블
 */
object UserTable : Table("user") {

    val id = long("id").autoIncrement()
    val email = varchar("email", 100).uniqueIndex()
    val name = varchar("name", 50)
    val githubUserId = long("github_user_id").references(GithubUserTable.id).nullable()
    val deleted = bool("deleted").default(false)
    val created = datetime("created").default(LocalDateTime.now())
    val updated = datetime("updated").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
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

    fun findByGithubUserId(githubUserIdParam: Long): UserModel? {
        return UserTable.select { githubUserId eq githubUserIdParam }.singleOrNull()?.let { rowToModel(it) }
    }

    /*
    select l.id from "user" u join login_history l on u.id = l.user_id where u.id = 2 order by l.id desc limit 1
     */
    fun findGithubLatestAccessToken(idParam: Long): String? {
        return UserTable.join(LoginHistoryTable, JoinType.INNER, id, LoginHistoryTable.userId)
            .join(
                OAuthGithubPrincipalTable,
                JoinType.INNER,
                LoginHistoryTable.oauthGithubPrincipalId,
                OAuthGithubPrincipalTable.id
            ).slice(OAuthGithubPrincipalTable.accessToken)
            .select { id eq idParam }
            .orderBy(LoginHistoryTable.id to SortOrder.DESC)
            .limit(1)
            .singleOrNull()?.let { it[OAuthGithubPrincipalTable.accessToken] }
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
