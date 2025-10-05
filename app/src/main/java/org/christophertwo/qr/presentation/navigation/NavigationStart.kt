package org.christophertwo.qr.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.christophertwo.qr.presentation.screen.generator.QrGeneratorRoot
import org.christophertwo.qr.presentation.screen.scanner.QrScannerRoot
import org.christophertwo.qr.core.RoutesStart

@Composable
fun NavigationStart(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = RoutesStart.Generator.route
    ) {
        composable(RoutesStart.Generator.route) {
            QrGeneratorRoot()
        }
        composable(RoutesStart.Scanner.route) {
            QrScannerRoot()
        }
    }
}