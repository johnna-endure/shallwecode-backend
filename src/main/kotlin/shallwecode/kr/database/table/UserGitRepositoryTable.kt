package shallwecode.kr.database.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object UserGitRepositoryTable : Table("user_github_repository") {
    val id = long("id")
    val githubUserId = long("github_user_id") references GithubUserTable.id
    val name = varchar("name", 255)
    val fullName = varchar("full_name", 255)
    val private = bool("private").default(false)
    val htmlUrl = varchar("html_url", 255)
    val description = text("description")
    val fork = bool("fork")
    val homepage = varchar("homepage", 255)
    val language = varchar("language", 50).nullable()
    val forkCount = integer("fork_count").default(0)
    val stargazersCount = integer("stargazers_count").default(0)
    val watchersCount = integer("watchers_count").default(0)
    val size = long("size").default(0)
    val defaultBranch = varchar("default_branch", 100)
    val openIssueCount = integer("open_issue_count").default(0)
    val topics = varchar("topics", 255).default("")
    val hasIssue = bool("has_issue")
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
