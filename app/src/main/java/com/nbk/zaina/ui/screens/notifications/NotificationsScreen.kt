package com.nbk.rise.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConnectWithoutContact
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onProfileClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    connectionViewModel: ConnectionViewModel = hiltViewModel()
) {
    val connectionUiState by connectionViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        connectionViewModel.loadPendingConnections()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LuxuryGray)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Notifications",
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = PrimaryColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (connectionUiState.isLoadingPending) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        } else if (connectionUiState.pendingConnections.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Card(
                        modifier = Modifier.size(80.dp),
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
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "No Notifications",
                                modifier = Modifier.size(32.dp),
                                tint = PrimaryColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No new notifications",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor
                    )

                    Text(
                        text = "Connection requests and updates will appear here",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            // Notifications List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(connectionUiState.pendingConnections) { connection ->
                    ConnectionRequestItem(
                        connection = connection,
                        onAccept = {
                            connectionViewModel.updateConnection(connection.id, ConnectionStatus.ACCEPTED)
                        },
                        onDecline = {
                            connectionViewModel.updateConnection(connection.id, ConnectionStatus.DECLINED)
                        },
                        onProfileClick = { onProfileClick(connection.requesterId.toString()) },
                        isUpdating = connectionUiState.isUpdating
                    )
                }
            }
        }

        // Error handling
        connectionUiState.pendingError?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ErrorRed.copy(alpha = 0.1f)
                )
            ) {
                Text(
                    text = error,
                    color = ErrorRed,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ConnectionRequestItem(
    connection: ConnectionDto,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onProfileClick: () -> Unit,
    isUpdating: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Connection Type Icon
                Card(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = when (connection.type) {
                            ConnectionType.CONNECT -> AccentLight
                            ConnectionType.MENTORSHIP -> NBKGold.copy(alpha = 0.2f)
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (connection.type) {
                                ConnectionType.CONNECT -> Icons.Default.ConnectWithoutContact
                                ConnectionType.MENTORSHIP -> Icons.Default.School
                            },
                            contentDescription = connection.type.name,
                            modifier = Modifier.size(20.dp),
                            tint = when (connection.type) {
                                ConnectionType.CONNECT -> PrimaryColor
                                ConnectionType.MENTORSHIP -> NBKGold
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = when (connection.type) {
                            ConnectionType.CONNECT -> "Connection Request"
                            ConnectionType.MENTORSHIP -> "Mentorship Request"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor
                    )

                    Text(
                        text = "${connection.requesterName ?: "Someone"} wants to ${
                            when (connection.type) {
                                ConnectionType.CONNECT -> "connect with you"
                                ConnectionType.MENTORSHIP -> "request mentorship from you"
                            }
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = formatRequestTime(connection.requestedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // View Profile Button
                OutlinedButton(
                    onClick = onProfileClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrimaryColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Profile")
                }

                // Decline Button
                OutlinedButton(
                    onClick = onDecline,
                    enabled = !isUpdating,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ErrorRed
                    )
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = ErrorRed
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Decline",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Accept Button
                Button(
                    onClick = onAccept,
                    enabled = !isUpdating,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SuccessGreen,
                        contentColor = Color.White
                    )
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Accept",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun formatRequestTime(timestamp: kotlinx.datetime.Instant): String {
    val localDateTime = timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
    val now = kotlinx.datetime.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val daysDiff = now.date.toEpochDays() - localDateTime.date.toEpochDays()

    return when {
        daysDiff == 0 -> "Today"
        daysDiff == 1 -> "Yesterday"
        daysDiff < 7 -> "$daysDiff days ago"
        else -> "${localDateTime.dayOfMonth}/${localDateTime.monthNumber}/${localDateTime.year}"
    }
}