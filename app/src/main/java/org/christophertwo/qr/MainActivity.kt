package org.christophertwo.qr

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import org.christophertwo.qr.ui.screen.generator.QrGeneratorRoot
import org.christophertwo.qr.ui.screen.generator.QrGeneratorViewModel
import org.christophertwo.qr.ui.screen.start.StartRoot
import org.christophertwo.qr.ui.theme.QrTheme

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QrTheme(
                dynamicColor = false,
                darkTheme = false
            ) {
                StartRoot()
            }
        }
    }
}