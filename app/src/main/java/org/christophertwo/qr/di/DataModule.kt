package org.christophertwo.qr.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import org.christophertwo.qr.core.common.DiQualifiers
import org.christophertwo.qr.data.auth.api.GoogleAuthManager
import org.christophertwo.qr.data.auth.impl.google.GoogleAuthManagerImpl
import org.christophertwo.qr.data.session.api.SessionRepository
import org.christophertwo.qr.data.session.impl.datastore.DataStoreSessionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")

val DataModule: Module
    get() = module {
        singleOf(::GoogleAuthManagerImpl).bind<GoogleAuthManager>()
        single<FirebaseAuth> { FirebaseAuth.getInstance() }
        single<SessionRepository> {
            DataStoreSessionRepository(get(qualifier = DiQualifiers.SESSION_DATASTORE))
        }
        single(qualifier = DiQualifiers.SESSION_DATASTORE) {
            androidContext().sessionDataStore
        }
        single(qualifier = DiQualifiers.SETTINGS_DATASTORE) {
            androidContext().settingsDataStore
        }
    }