package org.christophertwo.qr.ui.screen.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.christophertwo.qr.ui.components.QrAnimationLiquid
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
    onAction: (StartAction) -> Unit,
) {
    val color = colorScheme.primaryContainer
    Scaffold(
        content = { padding ->
            FlowColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                itemHorizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = {
                    QrAnimationLiquid()
                }
            )
        },
    )
}