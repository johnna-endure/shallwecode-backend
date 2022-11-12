package kr.co.shallwecode.persistence.tables

import org.jetbrains.exposed.sql.Table

object User : Table(){
    val id = integer("id").autoIncrement()
    val email = varchar("email", 100)
    val name = varchar("name", 100)
    val loginId = varchar("login_id", 100)
    val password = varchar("password", 100)
    val blogUrl = varchar("blog_url", 100)
    val githubUrl = varchar("github_url", 100)

    override val primaryKey = PrimaryKey(id)
}