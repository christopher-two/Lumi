package org.christophertwo.qr.core.common

import org.koin.core.qualifier.named

object DiQualifiers {
    val SESSION_DATASTORE = named("SessionDataStore")
    val SETTINGS_DATASTORE = named("SettingsDataStore")
    val USER_PREFERENCES_DATASTORE = named("UserPreferencesDataStore")
}