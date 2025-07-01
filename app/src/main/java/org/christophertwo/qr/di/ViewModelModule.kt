package org.christophertwo.qr.di

import org.christophertwo.qr.ui.screen.generator.QrGeneratorViewModel
import org.christophertwo.qr.ui.screen.scanner.QrScannerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule: Module
    get() = module {
        viewModelOf(::QrGeneratorViewModel)
        viewModelOf(::QrScannerViewModel)
    }