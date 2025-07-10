package org.christophertwo.qr.ui.screen.start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.christophertwo.qr.R
import org.christophertwo.qr.ui.navigation.NavigationStart
import org.christophertwo.qr.ui.theme.QrTheme
import org.christophertwo.qr.utils.RoutesStart
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartRoot(
    viewModel: StartViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    StartScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun StartScreen(
    state: StartState,
    onAction: (StartAction) -> Unit
) {
    val navController = rememberNavController()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            NavigationStart(navController = navController)
            CapsuleNavigation(
                navController = navController,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
            )
        }
    )
}

@Composable
fun ApiKeyGeminiPopup() {

}

@Composable
fun CapsuleNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val currentDestination by navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(
        initialValue = navController.currentDestination
    )
    val icons: Map<String, Int> = mapOf(
        RoutesStart.Scanner.route to R.drawable.outline_qr_code_scanner_24,
        RoutesStart.Generator.route to R.drawable.outline_qr_code_2_24,
    )

    Box(
        modifier = modifier
            .padding(
                top = 16.dp,
                end = 16.dp,
                start = 16.dp
            )
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(26.dp)),
        contentAlignment = Alignment.Center,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f))
                    .blur(radius = 20.dp)
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                icons.forEach { (route, icon) ->
                    CapsuleNavigationIcon(
                        route = route,
                        icon = icon,
                        name = route,
                        navController = navController,
                        isSelect = currentDestination == route,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    )
}

@Composable
fun CapsuleNavigationIcon(
    route: String,
    icon: Int,
    name: String,
    navController: NavHostController,
    isSelect: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .clickable { navController.navigate(route) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (isSelect) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
        Text(
            text = name,
            color = if (isSelect) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    apiLevel = 35
)
@Composable
private fun StartPreview() {
    QrTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        StartScreen(
            state = StartState(),
            onAction = {}
        )
    }
}