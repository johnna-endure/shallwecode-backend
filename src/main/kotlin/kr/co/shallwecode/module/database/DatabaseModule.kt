package kr.co.shallwecode.module.database

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.eagerSingleton

val dbModule = DI.Module(name = "database") {
    bind { eagerSingleton { Database() } }
}