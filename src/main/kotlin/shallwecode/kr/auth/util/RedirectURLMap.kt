package shallwecode.kr.auth.util

import java.util.concurrent.ConcurrentHashMap

class RedirectURLMap {
    private val map = ConcurrentHashMap<String, String>()

    fun save(state: String, url: String?) {
        if (url != null) {
            map[state] = url
        }
    }

    fun getAndRemove(state: String): String? {
        val ret = map[state]
        if (map.contains(state)) {
            map.remove(state)
        }
        return ret
    }
}
