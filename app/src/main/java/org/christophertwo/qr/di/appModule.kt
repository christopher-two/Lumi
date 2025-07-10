package org.christophertwo.qr.di

import com.google.gson.Gson
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module
    get() = module {
        single { Gson() }
    }
