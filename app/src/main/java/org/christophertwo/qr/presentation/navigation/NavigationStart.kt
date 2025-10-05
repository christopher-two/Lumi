package org.christophertwo.qr.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.christophertwo.qr.core.common.RoutesStart
import org.christophertwo.qr.presentation.screen.generator.QrGeneratorRoot
import org.christophertwo.qr.presentation.screen.scanner.QrScannerRoot
import org.christophertwo.qr.presentation.screen.start.StartRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationStart(
    navController: NavHostController,
    startDestination: String = RoutesStart.Start.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(RoutesStart.Start.route) {
            StartRoot(
                viewModel = koinViewModel(),
                navController = navController
            )
        }
        composable(RoutesStart.Generator.route) {
            QrGeneratorRoot(
                viewModel = koinViewModel()
            )
        }
        composable(RoutesStart.Scanner.route) {
            QrScannerRoot(
                viewModel = koinViewModel()
            )
        }
    }
}