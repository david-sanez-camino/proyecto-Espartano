package com.espartano.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.espartano.app.ui.navigation.EspartanoNavHost
import com.espartano.app.ui.theme.EspartanoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EspartanoTheme {
                EspartanoNavHost()
            }
        }
    }
}
