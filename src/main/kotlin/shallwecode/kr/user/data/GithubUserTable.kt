package shallwecode.kr.user.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 사용자 깃허브 정보
 * 깃허브에서 제공하는 user response 스키마
 */
object GithubUserTable : Table() {
    val id = long("id").uniqueIndex("github_user_unique_index")
    val userId = long("user_id") references UserTable.id
    val created = datetime("created").default(LocalDateTime.now())
    val updated = datetime("updated").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
    override val tableName = "github_user"
}
