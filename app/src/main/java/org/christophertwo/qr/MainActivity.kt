package org.christophertwo.qr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.christophertwo.qr.presentation.screen.start.StartRoot
import org.christophertwo.qr.presentation.theme.QrTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QrTheme {
                StartRoot()
            }
        }
    }
}