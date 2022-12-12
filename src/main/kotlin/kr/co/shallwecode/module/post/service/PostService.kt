package kr.co.shallwecode.module.post.service

import kr.co.shallwecode.module.database.Database
import kr.co.shallwecode.module.post.controller.PostCreateRequest
import kr.co.shallwecode.module.post.table.Post
import kr.co.shallwecode.module.post.table.create


class PostService(private val database: Database) {

    suspend fun create(request: PostCreateRequest, userId: Long): Long {
        return Post.create(request, userId)
    }
}