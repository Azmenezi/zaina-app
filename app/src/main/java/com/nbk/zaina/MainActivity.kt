package com.nbk.rise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.nbk.rise.navigation.AppNavigation
import com.nbk.rise.ui.theme.RiseTheme
import com.nbk.rise.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RiseTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()

                        AppNavigation(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}
