package com.nbk.rise.ui.screens.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nbk.rise.R
import com.nbk.rise.data.dtos.MessageDto
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.AuthViewModel
import com.nbk.rise.viewmodels.MessageViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

@Composable
fun ChatScreen(
    otherUserId: String,
    onNavigateBack: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    messageViewModel: MessageViewModel = hiltViewModel()
) {
    val authUiState by authViewModel.uiState
    val conversationState by messageViewModel.conversationState.collectAsStateWithLifecycle()

    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val otherUserUUID = remember { UUID.fromString(otherUserId) }
    val currentUserId = authUiState.userDetails?.id

    LaunchedEffect(otherUserUUID) {
        messageViewModel.loadConversation(otherUserUUID)
    }

    LaunchedEffect(conversationState.conversation?.messages?.size) {
        if (conversationState.conversation?.messages?.isNotEmpty() == true) {
            coroutineScope.launch {
                listState.animateScrollToItem(conversationState.conversation!!.messages.size - 1)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar (glass style)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = conversationState.conversation?.otherUserName ?: "Chat",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        if (conversationState.sendingMessage) {
                            Text(
                                text = "Sending...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            if (conversationState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                // Messages list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    conversationState.conversation?.messages?.let { messages ->
                        items(messages) { message ->
                            MessageBubble(
                                message = message,
                                isFromCurrentUser = message.senderId == currentUserId
                            )
                        }
                    }
                }

                // Message input (glass style)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Type a message...") },
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                                focusedBorderColor = Color.White,
                                cursorColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        FloatingActionButton(
                            onClick = {
                                if (messageText.isNotBlank() && currentUserId != null) {
                                    messageViewModel.sendMessage(
                                        receiverId = otherUserUUID,
                                        content = messageText.trim(),
                                        currentUserId = currentUserId
                                    )
                                    messageText = ""
                                }
                            },
                            containerColor = Color.White.copy(alpha = 0.3f),
                            contentColor = Color.White,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Error message card
            conversationState.sendError?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { messageViewModel.clearSendError() }) {
                            Text("Dismiss", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: MessageDto,
    isFromCurrentUser: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isFromCurrentUser) Color.White.copy(alpha = 0.2f)
                else Color.White.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isFromCurrentUser) 16.dp else 4.dp,
                bottomEnd = if (isFromCurrentUser) 4.dp else 16.dp
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = formatMessageTimestamp(message.sentAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    if (isFromCurrentUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (message.isRead) "✓✓" else "✓",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = if (message.isRead) 1f else 0.7f)
                        )
                    }
                }
            }
        }
    }
}

private fun formatMessageTimestamp(timestamp: kotlinx.datetime.Instant): String {
    val localDateTime = timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
    return String.format("%02d:%02d", localDateTime.hour, localDateTime.minute)
}

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ChatScreen(
//    otherUserId: String,
//    onNavigateBack: () -> Unit,
//    authViewModel: AuthViewModel = hiltViewModel(),
//    messageViewModel: MessageViewModel = hiltViewModel()
//) {
//    val authUiState by authViewModel.uiState
//    val conversationState by messageViewModel.conversationState.collectAsStateWithLifecycle()
//
//    var messageText by remember { mutableStateOf("") }
//    val listState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()
//
//    val otherUserUUID = remember { UUID.fromString(otherUserId) }
//    val currentUserId = authUiState.userDetails?.id
//
//    // Load conversation
//    LaunchedEffect(otherUserUUID) {
//        messageViewModel.loadConversation(otherUserUUID)
//    }
//
//    // Auto-scroll to bottom when new messages arrive
//    LaunchedEffect(conversationState.conversation?.messages?.size) {
//        if (conversationState.conversation?.messages?.isNotEmpty() == true) {
//            coroutineScope.launch {
//                listState.animateScrollToItem(conversationState.conversation!!.messages.size - 1)
//            }
//        }
//    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        // Top App Bar
//        TopAppBar(
//            title = {
//                Column {
//                    Text(
//                        text = conversationState.conversation?.otherUserName ?: "Chat",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    if (conversationState.sendingMessage) {
//                        Text(
//                            text = "Sending...",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = Color.White.copy(alpha = 0.7f)
//                        )
//                    }
//                }
//            },
//            navigationIcon = {
//                IconButton(onClick = onNavigateBack) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back"
//                    )
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = PrimaryColor,
//                titleContentColor = Color.White,
//                navigationIconContentColor = Color.White
//            )
//        )
//
//        if (conversationState.isLoading) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(color = PrimaryColor)
//            }
//        } else {
//            // Messages List
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxWidth()
//                    .background(LuxuryGray),
//                state = listState,
//                contentPadding = PaddingValues(16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                conversationState.conversation?.messages?.let { messages ->
//                    items(messages) { message ->
//                        MessageBubble(
//                            message = message,
//                            isFromCurrentUser = message.senderId == currentUserId
//                        )
//                    }
//                }
//            }
//
//            // Message Input
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(
//                    containerColor = SurfaceElevated
//                ),
//                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    OutlinedTextField(
//                        value = messageText,
//                        onValueChange = { messageText = it },
//                        modifier = Modifier.weight(1f),
//                        placeholder = { Text("Type a message...") },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedBorderColor = PrimaryColor,
//                            focusedLabelColor = PrimaryColor
//                        ),
//                        shape = RoundedCornerShape(24.dp)
//                    )
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    FloatingActionButton(
//                        onClick = {
//                            if (messageText.isNotBlank() && currentUserId != null) {
//                                messageViewModel.sendMessage(
//                                    receiverId = otherUserUUID,
//                                    content = messageText.trim(),
//                                    currentUserId = currentUserId
//                                )
//                                messageText = ""
//                            }
//                        },
//                        modifier = Modifier.size(48.dp),
//                        containerColor = PrimaryColor,
//                        contentColor = Color.White
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Send,
//                            contentDescription = "Send",
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//                }
//            }
//        }
//
//        // Error handling
//        conversationState.sendError?.let { error ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = ErrorRed.copy(alpha = 0.1f)
//                )
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = error,
//                        color = ErrorRed,
//                        style = MaterialTheme.typography.bodyMedium,
//                        modifier = Modifier.weight(1f)
//                    )
//
//                    TextButton(
//                        onClick = { messageViewModel.clearSendError() }
//                    ) {
//                        Text("Dismiss", color = ErrorRed)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun MessageBubble(
//    message: MessageDto,
//    isFromCurrentUser: Boolean
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
//    ) {
//        Card(
//            modifier = Modifier.widthIn(max = 280.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = if (isFromCurrentUser) PrimaryColor else SurfaceElevated
//            ),
//            shape = RoundedCornerShape(
//                topStart = 16.dp,
//                topEnd = 16.dp,
//                bottomStart = if (isFromCurrentUser) 16.dp else 4.dp,
//                bottomEnd = if (isFromCurrentUser) 4.dp else 16.dp
//            )
//        ) {
//            Column(
//                modifier = Modifier.padding(12.dp)
//            ) {
//                Text(
//                    text = message.content,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = if (isFromCurrentUser) Color.White else TextPrimary
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Row(
//                    horizontalArrangement = Arrangement.End,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(
//                        text = formatMessageTimestamp(message.sentAt),
//                        style = MaterialTheme.typography.bodySmall,
//                        color = if (isFromCurrentUser)
//                            Color.White.copy(alpha = 0.7f)
//                        else
//                            TextMuted
//                    )
//
//                    if (isFromCurrentUser) {
//                        Spacer(modifier = Modifier.width(4.dp))
//
//                        // Read status indicator
//                        Text(
//                            text = if (message.isRead) "✓✓" else "✓",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = if (message.isRead)
//                                AccentColor
//                            else
//                                Color.White.copy(alpha = 0.7f)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//private fun formatMessageTimestamp(timestamp: kotlinx.datetime.Instant): String {
//    val localDateTime = timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
//    return String.format("%02d:%02d", localDateTime.hour, localDateTime.minute)
//}