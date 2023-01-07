package kr.co.shallwecode.module.common.request

data class PageRequest(val page: Int, val size: Int, val maxSize: Int = 50) {
    init {
        require(page < 0 || size < 0 || size >= maxSize)
    }
}