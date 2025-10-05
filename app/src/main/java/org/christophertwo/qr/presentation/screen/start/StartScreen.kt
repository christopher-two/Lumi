package org.christophertwo.qr.presentation.screen.start

import android.content.Context
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Google
import org.christophertwo.qr.R
import org.christophertwo.qr.core.common.CreatedByOverride
import org.christophertwo.qr.core.common.RoutesStart
import org.christophertwo.qr.core.ui.AnimatedTextCarousel
import org.christophertwo.qr.core.ui.ButtonPrimary
import org.christophertwo.qr.core.ui.backgroundAnimated

@Composable
fun StartRoot(
    viewModel: StartViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    StartScreen(
        state = state,
        navController = navController,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun StartScreen(
    state: StartState,
    navController: NavController,
    onAction: (StartAction) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result -> onAction(StartAction.LoginWithGoogle(result)) }
    )

    LaunchedEffect(key1 = state.isLoggedIn) {
        if (state.isLoggedIn) {
            navController.navigate(RoutesStart.Generator.route) { // Asegúrate que "home_screen" sea tu ruta de destino
                popUpTo(RoutesStart.Start.route) { // Asegúrate que "start_screen" sea la ruta actual
                    inclusive = true
                }
            }
        }
    }

    val renderEffect = RenderEffect.createBlurEffect(
        100f, 100f, Shader.TileMode.DECAL
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = 1.1f,
                scaleY = 1.1f,
                renderEffect = renderEffect.asComposeRenderEffect()
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = backgroundAnimated(
                        colorPrimary = colorScheme.secondaryContainer,
                        colorSecondary = colorScheme.background
                    )
                )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Spacer(modifier = Modifier.weight(0.2f))
            Icon(
                painter = painterResource(R.drawable.ic_logo_two),
                contentDescription = "Lyra Logo",
                modifier = Modifier.size(50.dp),
                tint = colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            AnimatedTextCarousel(
                phrases = listOf(
                    "Genera tus códigos QR al instante",
                    "Comparte información de forma segura",
                    "Escanea y crea QR fácilmente",
                    "Tu acceso rápido a lo que importa"
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            CreatedByOverride()
            Spacer(modifier = Modifier.height(16.dp))
            Buttons(
                context = LocalContext.current, // Pass context to Buttons if it needs it for other things
                onGoogleSignInClick = {
                    state.googleSignInClient?.signInIntent.let { input ->
                        launcher.launch(input = input ?: Intent())
                    }
                }
            )
        }
    )
}

@Composable
internal fun Buttons(
    context: Context,
    onGoogleSignInClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(
                color = colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp // Asegúrate de que ambas esquinas inferiores sean 0 si es lo deseado
                )
            ),
        contentAlignment = Alignment.Center,
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.weight(1f))
                ButtonPrimary(
                    // Cambiado para usar el string de Google Sign-In
                    text = "Continuar con Google",
                    onClick = onGoogleSignInClick, // Acción para Google Sign-In
                    modifier = Modifier.fillMaxWidth(.8f),
                    icon = FontAwesomeIcons.Brands.Google,
                    containerColor = colorScheme.primaryContainer,
                    contentColor = colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    )
}