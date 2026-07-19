package com.caio.risesystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.caio.risesystem.navigation.RiseNavigation
import com.caio.risesystem.ui.theme.RiseSystemTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            RiseSystemTheme(
                darkTheme = true
            ) {
                RiseNavigation()
            }
        }
    }
}