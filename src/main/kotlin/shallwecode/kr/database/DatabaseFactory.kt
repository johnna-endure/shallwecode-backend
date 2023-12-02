package shallwecode.kr.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import shallwecode.kr.database.table.GithubUserTable
import shallwecode.kr.database.table.LoginHistoryTable
import shallwecode.kr.database.table.OAuthGithubPrincipalTable
import shallwecode.kr.database.table.UserTable


object DatabaseFactory {
    private lateinit var database: Database

    fun init() {
        println("database init.")

        val driverClassName = "org.postgresql.Driver"
        val jdbcUrl = "jdbc:postgresql://localhost:5432/shallwecode_dev"
        val user = "shallwecode"
        val password = "1234"
        database = Database.connect(
            driver = driverClassName,
            url = jdbcUrl,
            user = user,
            password = password
        )

        // 테이블 생성을 원할 경우
        transaction(database) {
//         create table
//            SchemaUtils.drop(LoginHistoryTable, OAuthGithubPrincipalTable)
//            SchemaUtils.create(LoginHistoryTable, OAuthGithubPrincipalTable, GithubUserTable, UserTable)
        }
    }

    suspend fun <T> transactionQuery(block: suspend () -> T): T =
        newSuspendedTransaction(db = database, context = Dispatchers.IO) { block() }

}

