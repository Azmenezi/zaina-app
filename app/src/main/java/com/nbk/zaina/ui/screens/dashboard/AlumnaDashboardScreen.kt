package com.nbk.rise.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nbk.rise.ui.theme.NBKGold

@Composable
fun AlumnaDashboardScreen(
    onNavigateToEvents: () -> Unit,
    onNavigateToResources: () -> Unit,
    onNavigateToDirectory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Alumna Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = NBKGold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Welcome back! Continue your leadership impact.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
} 