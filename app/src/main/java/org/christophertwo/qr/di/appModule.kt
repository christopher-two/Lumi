package org.christophertwo.qr.di

import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module
    get() = module {
        single {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            }
        }
    }
