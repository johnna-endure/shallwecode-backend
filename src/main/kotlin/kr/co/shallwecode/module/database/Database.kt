package kr.co.shallwecode.module.database

import kotlinx.coroutines.Dispatchers
import kr.co.shallwecode.module.user.persistence.table.Authentication
import kr.co.shallwecode.module.user.persistence.table.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

// TODO 추상화 필요 개발용, 운영용
class Database {
    init {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:~/testdb"
        val database = Database.connect(jdbcURL, driverClassName, "test", "test")

        // ddl auto create (개발용)
        transaction(database) {
            SchemaUtils.drop(Authentication)
            SchemaUtils.drop(User)

            SchemaUtils.create(User)
            SchemaUtils.create(Authentication)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}