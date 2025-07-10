package org.christophertwo.qr

import android.app.Application
import org.christophertwo.qr.di.appModule
import org.christophertwo.qr.di.repositoryModule
import org.christophertwo.qr.di.useCaseModule
import org.christophertwo.qr.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                repositoryModule,
                useCaseModule,
                viewModelModule,
                appModule
            )
        }
    }
}