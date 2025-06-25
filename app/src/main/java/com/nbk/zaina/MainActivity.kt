package com.nbk.rise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.nbk.rise.navigation.AppNavigation
import com.nbk.rise.ui.theme.RiseTheme
import com.nbk.rise.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            RiseTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()

                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(navController = navController, authViewModel = authViewModel)
                }
            }
        }

        window.navigationBarColor = Color.Black.toArgb()
        window.statusBarColor = Color.Transparent.toArgb()

        WindowCompat.getInsetsController(window, window.decorView)?.apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = false
        }
        window.decorView.setOnApplyWindowInsetsListener { v, insets ->
            v.onApplyWindowInsets(insets)
        }
    }
}


