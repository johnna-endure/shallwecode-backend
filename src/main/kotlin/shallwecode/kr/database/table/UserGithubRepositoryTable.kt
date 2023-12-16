package shallwecode.kr.database.table

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserGithubRepositoryTable : Table("user_github_repository") {
    val id = long("id")
    val githubUserId = long("github_user_id") references GithubUserTable.id
    val name = varchar("name", 255)
    val fullName = varchar("full_name", 255)
    val private = bool("private").default(false)
    val htmlUrl = varchar("html_url", 255)
    val description = text("description").nullable()
    val fork = bool("fork")
    val homepage = varchar("homepage", 255).nullable()
    val language = varchar("language", 50).nullable()
    val forkCount = integer("fork_count").default(0)
    val stargazersCount = integer("stargazers_count").default(0)
    val watchersCount = integer("watchers_count").default(0)
    val size = long("size").default(0)
    val defaultBranch = varchar("default_branch", 100)
    val openIssuesCount = integer("open_issues_count").default(0)
    val topics = varchar("topics", 255).default("")
    val hasIssues = bool("has_issues")
    val hasProjects = bool("has_projects")
    val hasWiki = bool("has_wiki")
    val hasPages = bool("has_pages")
    val hasDownloads = bool("has_downloads")
    val disabled = bool("disabled")
    val visibility = varchar("visibility", 100)
    val pushedAt = datetime("pushed_at")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

@Serializable
data class UserGithubRepositoryModel(
    val id: Long,
    val githubUserId: Long? = null,
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    val `private`: Boolean,
    @SerialName("html_url")
    val htmlUrl: String,
    val description: String? = null,
    val fork: Boolean,
    val homepage: String? = null,
    val language: String? = null,
    @SerialName("fork_count")
    val forkCount: Int = 0,
    @SerialName("stargazers_count")
    val stargazersCount: Int = 0,
    @SerialName("watchers_count")
    val watchersCount: Int,
    val size: Long = 0,
    @SerialName("default_branch")
    val defaultBranch: String,
    @SerialName("open_issues_count")
    val openIssueCount: Int,
    val topics: List<String>,
    @SerialName("has_issues")
    val hasIssues: Boolean,
    @SerialName("has_projects")
    val hasProjects: Boolean,
    @SerialName("has_wiki")
    val hasWiki: Boolean,
    @SerialName("has_pages")
    val hasPages: Boolean,
    @SerialName("has_downloads")
    val hasDownloads: Boolean,
    val disabled: Boolean,
    val visibility: String,
    @Contextual
    @SerialName("pushed_at")
    val pushedAt: LocalDateTime,
    @Contextual
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    @Contextual
    @SerialName("updated_at")
    val updatedAt: LocalDateTime
)
