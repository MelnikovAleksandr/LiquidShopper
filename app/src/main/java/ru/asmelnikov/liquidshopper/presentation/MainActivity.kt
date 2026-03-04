package ru.asmelnikov.liquidshopper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.asmelnikov.liquidshopper.presentation.mainstate.rememberAppState
import ru.asmelnikov.liquidshopper.presentation.navigation.NavGraph
import ru.asmelnikov.liquidshopper.presentation.theme.LiquidShopperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            val appState = rememberAppState()
            LiquidShopperTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets.ime,
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    snackbarHost = {
                        SnackbarHost(appState.snackbarState)
                    }
                ) { paddingValues ->
                    SharedTransitionLayout {
                        NavGraph(
                            appState = appState,
                            paddingValues = paddingValues,
                            showSnackbar = { message, duration, label, action ->
                                appState.showSnackbar(
                                    message = message,
                                    duration = duration,
                                    actionLabel = label,
                                    actionPerformed = action
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}