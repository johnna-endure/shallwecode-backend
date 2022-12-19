package kr.co.shallwecode.module.post.service

import kr.co.shallwecode.module.database.Database
import kr.co.shallwecode.module.post.controller.PostSaveRequest
import kr.co.shallwecode.module.post.table.Post
import kr.co.shallwecode.module.post.table.modify


class PostService(private val database: Database) {

    suspend fun create(request: PostSaveRequest, userId: Long): Long {
        return database.dbQuery { create(request, userId) }
    }

    suspend fun modify(request: PostSaveRequest, userId: Long, postId: Long) {
        return database.dbQuery { Post.modify(request, userId, postId) }
    }
}