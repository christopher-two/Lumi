package org.christophertwo.qr.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

// Importar implementaciones desde la capa data
import org.christophertwo.qr.data.impl.BarcodeScanningRepositoryImpl
import org.christophertwo.qr.data.impl.GeminiRepositoryImpl
import org.christophertwo.qr.data.repository.BarcodeScanningRepository
import org.christophertwo.qr.data.repository.GeminiRepository

val repositoryModule: Module
    get() = module {
        factoryOf(::BarcodeScanningRepositoryImpl).bind(BarcodeScanningRepository::class)
        factoryOf(::GeminiRepositoryImpl).bind(GeminiRepository::class)
    }