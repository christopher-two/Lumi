package org.christophertwo.qr.ui.screen.generator

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lightspark.composeqr.DotShape
import com.lightspark.composeqr.QrCodeColors
import com.lightspark.composeqr.QrCodeView
import org.christophertwo.qr.ui.theme.QrTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun QrGeneratorRoot(
    viewModel: QrGeneratorViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    QrGeneratorScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun QrGeneratorScreen(
    state: QrGeneratorState,
    onAction: (QrGeneratorAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        FlowColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            CardQr(
                content = state.content
            )
            Spacer(modifier = Modifier.height(10.dp))
            DataGenerator(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
fun CardQr(content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.primaryContainer,
            contentColor = colorScheme.onPrimaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = {
                QrCode(
                    content = content,
                    modifier = Modifier.padding(10.dp)
                )
            }
        )
    }
}

@Composable
fun QrCode(
    content: String,
    colors: QrCodeColors = QrCodeColors(
        background = colorScheme.primaryContainer,
        foreground = colorScheme.onPrimaryContainer,
    ),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    QrCodeView(
        data = content,
        modifier = modifier.size(300.dp),
        colors = colors,
        dotShape = DotShape.Circle,
    )
}

@Composable
fun DataGenerator(
    state: QrGeneratorState,
    onAction: (QrGeneratorAction) -> Unit
) {
    OutlinedTextField(
        value = state.content,
        onValueChange = {
            onAction(QrGeneratorAction.UpdateContent(it))
        },
        placeholder = {
            Text(text = "Ingrese el texto")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        singleLine = true,
        maxLines = 1,
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun QrGeneratorRootPreview() {
    QrTheme {
        QrGeneratorScreen(
            state = QrGeneratorState(),
            onAction = {}
        )
    }
}
