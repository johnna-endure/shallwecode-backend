package shallwecode.kr.user.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 사용자 마스터 테이블
 */
object UserTable : Table() {
    val id = long("id").autoIncrement()
    val email = varchar("email", 100).uniqueIndex("user_email_unique_index")
    val name = varchar("name", 50)
    val deleted = bool("deleted").default(false)
    val created = datetime("created").default(LocalDateTime.now())
    val updated = datetime("updated").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
    override val tableName = "user"
}
