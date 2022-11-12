package kr.co.shallwecode.persistence

import kotlinx.coroutines.Dispatchers
import kr.co.shallwecode.persistence.tables.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

// TODO 추상화 필요 개발용, 운영용
object DatabaseFactory {
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:~/shallwecode/db"
        val database = Database.connect(jdbcURL, driverClassName)

        // ddl auto create (개발용)
        transaction(database) {
            SchemaUtils.create(User)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}