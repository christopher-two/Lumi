package org.christophertwo.qr.ui.screen.generator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun QrGeneratorRoot(
    viewModel: QrGeneratorViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    QrGeneratorScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun QrGeneratorScreen(
    state: QrGeneratorState,
    onAction: (QrGeneratorAction) -> Unit,
) {

}