package kr.co.shallwecode.module.user

import kr.co.shallwecode.module.user.persistence.repository.UserRepository
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instanceOrNull

class TempDIAware(override val di: DI) : DIAware {
    private val userRepository by instanceOrNull<UserRepository>()
}