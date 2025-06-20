package com.nbk.rise.ui.screens.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nbk.rise.data.dtos.ConversationSummaryDto
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.MessageViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    onConversationClick: (String) -> Unit,
    messageViewModel: MessageViewModel = hiltViewModel()
) {
    val messageUiState by messageViewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        // TODO: Load conversations when API is ready
        // messageViewModel.loadConversations()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LuxuryGray)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = PrimaryColor
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Messages",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Stay connected with your network",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (messageUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        } else if (messageUiState.conversations.isEmpty()) {
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
                                imageVector = Icons.Default.Message,
                                contentDescription = "No Messages",
                                modifier = Modifier.size(32.dp),
                                tint = PrimaryColor
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "No conversations yet",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor
                    )
                    
                    Text(
                        text = "Start connecting with other members from the Directory",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            // Conversations List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messageUiState.conversations) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        onClick = { onConversationClick(conversation.otherUserId.toString()) }
                    )
                }
            }
        }
        
        // Error handling
        messageUiState.error?.let { error ->
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
private fun ConversationItem(
    conversation: ConversationSummaryDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = SurfaceElevated
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.size(56.dp),
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
                            modifier = Modifier.size(24.dp),
                            tint = PrimaryColor
                        )
                    }
                }
                
                // Unread indicator
                if (conversation.unreadCount > 0) {
                    Badge(
                        modifier = Modifier.align(Alignment.TopEnd),
                        containerColor = ErrorRed
                    ) {
                        Text(
                            text = if (conversation.unreadCount > 99) "99+" else conversation.unreadCount.toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Message Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = conversation.otherUserName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColor
                )
                
                if (conversation.lastMessage != null) {
                    Text(
                        text = conversation.lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (conversation.unreadCount > 0) TextPrimary else TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Timestamp
            conversation.lastMessageTime?.let { timestamp ->
                Text(
                    text = formatMessageTime(timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
    }
}

private fun formatMessageTime(timestamp: kotlinx.datetime.Instant): String {
    val localDateTime = timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
    val now = kotlinx.datetime.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    
    return when {
        localDateTime.date == now.date -> {
            // Same day - show time
            String.format("%02d:%02d", localDateTime.hour, localDateTime.minute)
        }
        localDateTime.date.dayOfYear == now.date.dayOfYear - 1 -> {
            // Yesterday
            "Yesterday"
        }
        else -> {
            // Older - show date
            "${localDateTime.dayOfMonth}/${localDateTime.monthNumber}"
        }
    }
} 