package org.christophertwo.qr.di

import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import org.christophertwo.qr.BuildConfig
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module
    get() = module {
        single { Gson() }

        single {
            GenerativeModel(
                modelName = "gemini-2.0-flash-lite-001",
                apiKey = BuildConfig.GEMINI_API_KEY,
            )
        }
    }
