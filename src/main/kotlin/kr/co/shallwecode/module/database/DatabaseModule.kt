package kr.co.shallwecode.module.database

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val dbModule = DI.Module(name = "database") {
    bind { singleton { Database() } }
}