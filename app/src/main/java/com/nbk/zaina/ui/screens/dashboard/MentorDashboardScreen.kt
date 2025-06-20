package com.nbk.rise.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nbk.rise.data.dtos.ConnectionDto
import com.nbk.rise.data.dtos.ConnectionStatus
import com.nbk.rise.data.dtos.ConnectionType
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.ConnectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorDashboardScreen(
    onNavigateToEvents: () -> Unit,
    onNavigateToResources: () -> Unit,
    onNavigateToDirectory: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    connectionViewModel: ConnectionViewModel = hiltViewModel()
) {
    val connectionUiState by connectionViewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        connectionViewModel.loadPendingConnections()
        connectionViewModel.loadAcceptedConnections()
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LuxuryWhite),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Welcome Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = PrimaryColor
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = NBKGold.copy(alpha = 0.2f)
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Mentor",
                                    modifier = Modifier.size(24.dp),
                                    tint = NBKGold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                text = "Welcome, Mentor!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Guide and inspire the next generation",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
        
        item {
            // Quick Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mentorship Requests
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceElevated
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${connectionUiState.pendingConnections.count { it.type == ConnectionType.MENTORSHIP }}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = NBKGold
                        )
                        Text(
                            text = "Mentorship\nRequests",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
                
                // Active Mentees
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceElevated
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${connectionUiState.acceptedConnections.count { it.type == ConnectionType.MENTORSHIP }}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = AccentDark
                        )
                        Text(
                            text = "Active\nMentees",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
                
                // Total Connections
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceElevated
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${connectionUiState.acceptedConnections.size}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor
                        )
                        Text(
                            text = "Total\nConnections",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
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
                color = PrimaryColor
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Notifications
                ActionCard(
                    title = "Notifications",
                    subtitle = "View requests",
                    icon = Icons.Default.Notifications,
                    color = ErrorRed,
                    onClick = onNavigateToNotifications,
                    modifier = Modifier.weight(1f)
                )
                
                // Messages
                ActionCard(
                    title = "Messages",
                    subtitle = "Chat with mentees",
                    icon = Icons.Default.Message,
                    color = InfoBlue,
                    onClick = onNavigateToMessages,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Directory
                ActionCard(
                    title = "Directory",
                    subtitle = "Find members",
                    icon = Icons.Default.Groups,
                    color = AccentDark,
                    onClick = onNavigateToDirectory,
                    modifier = Modifier.weight(1f)
                )
                
                // Events
                ActionCard(
                    title = "Events",
                    subtitle = "Upcoming events",
                    icon = Icons.Default.School,
                    color = NBKGold,
                    onClick = onNavigateToEvents,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Recent Mentorship Requests
        if (connectionUiState.pendingConnections.any { it.type == ConnectionType.MENTORSHIP }) {
            item {
                Text(
                    text = "Recent Mentorship Requests",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColor
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceElevated
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        connectionUiState.pendingConnections
                            .filter { it.type == ConnectionType.MENTORSHIP }
                            .take(3)
                            .forEach { connection ->
                                MentorshipRequestItem(
                                    connection = connection,
                                    onAccept = {
                                        connectionViewModel.updateConnection(
                                            connection.id,
                                            ConnectionStatus.ACCEPTED
                                        )
                                    },
                                    onDecline = {
                                        connectionViewModel.updateConnection(
                                            connection.id,
                                            ConnectionStatus.DECLINED
                                        )
                                    }
                                )
                                
                                if (connection != connectionUiState.pendingConnections
                                    .filter { it.type == ConnectionType.MENTORSHIP }
                                    .take(3)
                                    .last()
                                ) {
                                    Divider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        color = SurfaceDim
                                    )
                                }
                            }
                    }
                }
            }
        }
        
        // Resource Access
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToResources() },
                colors = CardDefaults.cardColors(
                    containerColor = AccentLight.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Resources",
                        modifier = Modifier.size(32.dp),
                        tint = AccentDark
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Mentorship Resources",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryColor
                        )
                        Text(
                            text = "Access guides and materials for effective mentoring",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = color.copy(alpha = 0.1f)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(24.dp),
                        tint = color
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryColor
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun MentorshipRequestItem(
    connection: ConnectionDto,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = AccentLight
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(20.dp),
                    tint = PrimaryColor
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = connection.requesterName ?: "Anonymous",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = PrimaryColor
            )
            Text(
                text = "Wants mentorship",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onDecline,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Decline",
                    modifier = Modifier.size(16.dp),
                    tint = ErrorRed
                )
            }
            
            IconButton(
                onClick = onAccept,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Accept",
                    modifier = Modifier.size(16.dp),
                    tint = SuccessGreen
                )
            }
        }
    }
} 