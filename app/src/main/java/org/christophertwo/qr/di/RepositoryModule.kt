package org.christophertwo.qr.di

import org.christophertwo.qr.domain.repository.BarcodeScanningRepository
import org.christophertwo.qr.domain.repository.BarcodeScanningRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule: Module
    get() = module {
        factoryOf(::BarcodeScanningRepositoryImpl).bind(BarcodeScanningRepository::class)
    }