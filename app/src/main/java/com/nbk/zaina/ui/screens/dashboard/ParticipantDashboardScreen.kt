package com.nbk.rise.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantDashboardScreen(
    onNavigateToEvents: () -> Unit,
    onNavigateToResources: () -> Unit,
    onNavigateToDirectory: () -> Unit,
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val eventUiState by eventViewModel.uiState
    
    LaunchedEffect(Unit) {
        eventViewModel.loadUpcomingEvents()
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LuxuryGray),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Welcome Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = NBKGreen
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(NBKGreen, NBKGreenDark)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Welcome to RISE",
                            style = MaterialTheme.typography.headlineSmall,
                            color = LuxuryWhite,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Continue your leadership journey",
                            style = MaterialTheme.typography.bodyLarge,
                            color = NBKGoldLight
                        )
                    }
                }
            }
        }
        
        item {
            // Quick Actions
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Events",
                    icon = Icons.Default.Event,
                    onClick = onNavigateToEvents,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    title = "Resources",
                    icon = Icons.Default.Folder,
                    onClick = onNavigateToResources,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    title = "Directory",
                    icon = Icons.Default.People,
                    onClick = onNavigateToDirectory,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            // Current Module
            Text(
                text = "Current Module",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceElevated
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.size(48.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = NBKGoldLight.copy(alpha = 0.1f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = NBKGold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Leadership Fundamentals",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Module 2 of 6 • 75% Complete",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
                
                LinearProgressIndicator(
                    progress = 0.75f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    color = NBKGreen,
                    trackColor = SurfaceDim
                )
            }
        }
        
        item {
            // Upcoming Events
            Text(
                text = "Upcoming Events",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (eventUiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NBKGreen)
                }
            } else if (eventUiState.upcomingEvents.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceCard
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No upcoming events",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextMuted
                        )
                    }
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(eventUiState.upcomingEvents.size) { index ->
                        val event = eventUiState.upcomingEvents[index]
                        EventCard(
                            title = event.title,
                            date = event.date.toString().take(10), // Simple date formatting
                            location = event.location ?: "Online",
                            onClick = onNavigateToEvents
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = NBKGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun EventCard(
    title: String,
    date: String,
    location: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 2
            )
            
            Column {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = NBKGreen,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
    }
} 