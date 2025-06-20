package com.nbk.rise.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nbk.rise.data.dtos.ConnectionType
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.AuthViewModel
import com.nbk.rise.viewmodels.ConnectionViewModel
import com.nbk.rise.viewmodels.ProfileViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
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
    val connectionUiState by connectionViewModel.uiState.collectAsStateWithLifecycle()
    
    val userUUID = remember { UUID.fromString(userId) }
    val currentUserId = authUiState.userDetails?.id
    
    LaunchedEffect(userUUID) {
        profileViewModel.loadProfile(userUUID)
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { Text("Profile") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = PrimaryColor,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )
        
        if (profileUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        } else {
            profileUiState.profile?.let { profile ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LuxuryGray)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Profile Header
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = SurfaceElevated
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Profile Photo
                                Box(
                                    modifier = Modifier.size(120.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (profile.imageUrl != null) {
                                        AsyncImage(
                                            model = profile.imageUrl,
                                            contentDescription = "Profile Photo",
                                            modifier = Modifier
                                                .size(120.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Card(
                                            modifier = Modifier.size(120.dp),
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
                                                    modifier = Modifier.size(48.dp),
                                                    tint = PrimaryColor
                                                )
                                            }
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Name and Title
                                Text(
                                    text = profile.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryColor
                                )
                                
                                if (profile.position != null || profile.company != null) {
                                    Text(
                                        text = listOfNotNull(profile.position, profile.company).joinToString(" at "),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = SecondaryColor
                                    )
                                }
                                
                                // Role Badge
                                authUiState.userDetails?.role?.let { role ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    AssistChip(
                                        onClick = { },
                                        label = { 
                                            Text(
                                                text = role.name.lowercase().replaceFirstChar { it.uppercase() },
                                                fontWeight = FontWeight.Medium
                                            ) 
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = when (role) {
                                                UserRole.MENTOR -> NBKGold.copy(alpha = 0.2f)
                                                UserRole.ALUMNA -> AccentColor.copy(alpha = 0.3f)
                                                UserRole.PARTICIPANT -> PrimaryColor.copy(alpha = 0.2f)
                                                UserRole.APPLICANT -> SecondaryColor.copy(alpha = 0.2f)
                                            },
                                            labelColor = when (role) {
                                                UserRole.MENTOR -> NBKGold
                                                UserRole.ALUMNA -> AccentDark
                                                UserRole.PARTICIPANT -> PrimaryColor
                                                UserRole.APPLICANT -> SecondaryColor
                                            }
                                        )
                                    )
                                }
                            }
                        }
                    }
                    
                    // Action Buttons
                    if (currentUserId != userUUID) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Connect Button
                                Button(
                                    onClick = {
                                        connectionViewModel.createConnection(userUUID, ConnectionType.CONNECT)
                                    },
                                    modifier = Modifier.weight(1f),
                                    enabled = !connectionUiState.isCreating,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryColor,
                                        contentColor = Color.White
                                    )
                                ) {
                                    if (connectionUiState.isCreating) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Connecting...")
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Connect")
                                    }
                                }
                                
                                // Message Button
                                OutlinedButton(
                                    onClick = {
                                        onNavigateToChat(userId)
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = PrimaryColor
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Message,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Message")
                                }
                            }
                            
                            // Request Mentorship Button (for mentors)
                            if (authUiState.userDetails?.role == UserRole.MENTOR) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        connectionViewModel.createConnection(userUUID, ConnectionType.MENTORSHIP)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !connectionUiState.isCreating,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = NBKGold,
                                        contentColor = Color.White
                                    )
                                ) {
                                    if (connectionUiState.isCreating) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Requesting...")
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.School,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Request Mentorship")
                                    }
                                }
                            }
                        }
                    }
                    
                    // Bio Section
                    if (!profile.bio.isNullOrBlank()) {
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
                                    Text(
                                        text = "About",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryColor
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = profile.bio,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                    
                    // Skills Section
                    if (profile.skills.isNotEmpty()) {
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
                                    Text(
                                        text = "Skills",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryColor
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(profile.skills) { skill ->
                                            AssistChip(
                                                onClick = { },
                                                label = { Text(skill) },
                                                colors = AssistChipDefaults.assistChipColors(
                                                    containerColor = AccentLight,
                                                    labelColor = PrimaryColor
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // LinkedIn Section
                    if (!profile.linkedinUrl.isNullOrBlank()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = SurfaceElevated
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Link,
                                        contentDescription = "LinkedIn",
                                        tint = PrimaryColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "View LinkedIn Profile",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = PrimaryColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Error handling
        profileUiState.error?.let { error ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = error,
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { profileViewModel.loadProfile(userUUID) }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }

        // Connection success/error feedback
        connectionUiState.createError?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar or toast with error
                // For now, we'll just clear the error after showing
                connectionViewModel.clearCreateError()
            }
        }
    }
} 