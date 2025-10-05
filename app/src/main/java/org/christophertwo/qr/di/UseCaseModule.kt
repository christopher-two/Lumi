package org.christophertwo.qr.di

import org.christophertwo.qr.domain.usecase.ScanBarcodeUseCase
import org.christophertwo.qr.domain.usecase.SignInWithGoogleUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule: Module
    get() = module {
        factoryOf(::ScanBarcodeUseCase)
        factoryOf(::SignInWithGoogleUseCase)
    }