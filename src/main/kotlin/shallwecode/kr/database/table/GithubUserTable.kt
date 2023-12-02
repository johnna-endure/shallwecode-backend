package shallwecode.kr.database.table

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * 사용자 깃허브 정보
 * 깃허브에서 제공하는 user response 스키마
 */
object GithubUserTable : Table("github_user") {
    val id = long("id")
    val login = varchar("login", 100)
    val avatarUrl = varchar("avatar_url", 200)
    val htmlUrl = varchar("html_url", 200)
    val name = varchar("name", 50).nullable()
    val company = varchar("company", 50).nullable()
    val blog = varchar("blog", 100).nullable()
    val location = varchar("location", 100).nullable()
    val email = varchar("email", 100)
    val publicRepos = integer("public_repos").default(0)
    val publicGists = integer("public_gists").default(0)
    val privateGists = integer("private_gists").default(0)
    val totalPrivateRepos = integer("total_private_repos").default(0)
    val ownedPrivateRepos = integer("owned_private_repos").default(0)
    val followers = integer("followers").default(0)
    val following = integer("following").default(0)
    val collaborators = integer("collaborators").default(0)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)

    fun save(userInfo: GithubUserModel): GithubUserModel? {
        return insert {
            it[id] = userInfo.id
            it[login] = userInfo.login
            it[avatarUrl] = userInfo.avatarUrl
            it[htmlUrl] = userInfo.htmlUrl
            it[name] = userInfo.name
            it[company] = userInfo.company
            it[blog] = userInfo.blog
            it[location] = userInfo.location
            it[email] = userInfo.email
            it[publicRepos] = userInfo.publicRepos
            it[publicGists] = userInfo.publicGists
            it[privateGists] = userInfo.privateGists
            it[totalPrivateRepos] = userInfo.totalPrivateRepos
            it[ownedPrivateRepos] = userInfo.ownedPrivateRepos
            it[followers] = userInfo.followers
            it[following] = userInfo.following
            it[collaborators] = userInfo.collaborators
            it[createdAt] = userInfo.createdAt
            it[updatedAt] = userInfo.updatedAt
        }.resultedValues?.singleOrNull()?.let { rowToModel(it) } ?: throw RuntimeException("create failed.")
    }

    fun existById(id: Long): Boolean {
        return findById(id) != null
    }

    /**
     * @param idParam 깃허브 사용자 아이디
     */
    fun findById(idParam: Long): GithubUserModel? {
        return GithubUserTable.select {
            id eq idParam
        }.singleOrNull()?.let {
            rowToModel(it)
        }
    }

    private fun rowToModel(row: ResultRow): GithubUserModel {
        return GithubUserModel(
            id = row[id],
            login = row[login],
            avatarUrl = row[avatarUrl],
            htmlUrl = row[htmlUrl],
            name = row[name],
            company = row[company],
            blog = row[blog],
            location = row[location],
            email = row[email],
            publicRepos = row[publicRepos],
            publicGists = row[publicGists],
            privateGists = row[privateGists],
            totalPrivateRepos = row[totalPrivateRepos],
            ownedPrivateRepos = row[ownedPrivateRepos],
            followers = row[followers],
            following = row[following],
            collaborators = row[collaborators],
            createdAt = row[createdAt],
            updatedAt = row[updatedAt],
        )
    }
}

@Serializable
data class GithubUserModel(
    val id: Long,
    val login: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val name: String? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val email: String,
    @SerialName("public_repos")
    val publicRepos: Int,
    @SerialName("public_gists")
    val publicGists: Int,
    @SerialName("private_gists")
    val privateGists: Int,
    @SerialName("total_private_repos")
    val totalPrivateRepos: Int,
    @SerialName("owned_private_repos")
    val ownedPrivateRepos: Int,
    val followers: Int,
    val following: Int,
    val collaborators: Int,
    @Contextual
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    @Contextual
    @SerialName("updated_at")
    val updatedAt: LocalDateTime
)
