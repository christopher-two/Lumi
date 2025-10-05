package org.christophertwo.qr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.firstOrNull
import org.christophertwo.qr.core.common.RoutesStart
import org.christophertwo.qr.core.ui.theme.QrTheme
import org.christophertwo.qr.data.session.api.SessionRepository
import org.christophertwo.qr.presentation.navigation.NavigationStart
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val sessionRepository = koinInject<SessionRepository>()
            var isSessionUser by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                isSessionUser = sessionRepository.isUserLoggedIn().firstOrNull() == true
            }

            QrTheme {
                when {
                    isSessionUser -> NavigationStart(navController = navController, startDestination = RoutesStart.Generator.route)
                    else -> NavigationStart(navController = navController)
                }
            }
        }
    }
}