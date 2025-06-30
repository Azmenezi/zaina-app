package com.nbk.rise.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nbk.rise.R
import com.nbk.rise.data.dtos.ConnectionType
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.viewmodels.AuthViewModel
import com.nbk.rise.viewmodels.ConnectionViewModel
import com.nbk.rise.viewmodels.ProfileViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.*

@Composable
fun ViewProfileScreen(
    userId: String,
    onNavigateBack: () -> Unit,
    onNavigateToChat: (String) -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    connectionViewModel: ConnectionViewModel = hiltViewModel()
) {
    val authUiState by authViewModel.uiState
    val profileUiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val userUUID = remember { UUID.fromString(userId) }
    val currentUserId = authUiState.userDetails?.id

    LaunchedEffect(userUUID) {
        profileViewModel.loadProfile(userUUID)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = R.drawable.login_bg,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TopAppBar(
                    title = { Text("Profile", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }

            if (profileUiState.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            } else {
                profileUiState.profile?.let { profile ->
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.1f))
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (profile.imageUrl != null) {
                                    AsyncImage(
                                        model = profile.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Card(
                                        modifier = Modifier.size(120.dp),
                                        shape = CircleShape,
                                        colors = CardDefaults.cardColors(Color.White.copy(0.3f))
                                    ) {
                                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFFFA6C9), modifier = Modifier.size(48.dp))
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(profile.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                                if (profile.position != null || profile.company != null) {
                                    Text(
                                        text = listOfNotNull(profile.position, profile.company).joinToString(" at "),
                                        color = Color(0xFFCCCCCC)
                                    )
                                }
                            }
                        }
                    }

                    if (currentUserId != userUUID) {
                        item {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                GradientOutlinedButton(
                                    onClick = { connectionViewModel.createConnection(userUUID, ConnectionType.CONNECT) },
                                    text = "Connect",
                                    icon = Icons.Default.Person,
                                    modifier = Modifier.weight(1f)
                                )
                                GradientOutlinedButton(
                                    onClick = { onNavigateToChat(userId) },
                                    text = "Message",
                                    icon = Icons.AutoMirrored.Filled.Message,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (authUiState.userDetails?.role == UserRole.MENTOR) {
                                Spacer(modifier = Modifier.height(8.dp))
                                GradientOutlinedButton(
                                    onClick = { connectionViewModel.createConnection(userUUID, ConnectionType.MENTORSHIP) },
                                    text = "Request Mentorship",
                                    icon = Icons.Default.School,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    if (!profile.bio.isNullOrBlank()) {
                        item {
                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.1f))) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("About", fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(profile.bio, color = Color.White.copy(0.8f))
                                }
                            }
                        }
                    }

                    if (profile.skills.isNotEmpty()) {
                        item {
                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.1f))) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Skills", fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        items(profile.skills) { skill ->
                                            Surface(
                                                shape = RoundedCornerShape(50),
                                                shadowElevation = 2.dp,
                                                color = Color.Transparent,
                                                border = BorderStroke(
                                                    1.dp,
                                                    Brush.horizontalGradient(
                                                        listOf(Color(0xFFECECEC), Color(0xFFDAD6F3))
                                                    )
                                                )
                                            ) {
                                                Text(
                                                    text = skill,
                                                    color = Color.White,
                                                    modifier = Modifier
                                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!profile.linkedinUrl.isNullOrBlank()) {
                        item {
                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.1f))) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Link, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("View LinkedIn Profile", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun GradientOutlinedButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(48.dp),
        shape = RoundedCornerShape(50),
        border = BorderStroke(
            1.dp,
            Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFFFA726), // Soft Orange
                    Color(0xFF64B5F6)  // Soft Blue
                )
            )
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White.copy(alpha = 0.05f),
            contentColor = Color.White
        )
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
