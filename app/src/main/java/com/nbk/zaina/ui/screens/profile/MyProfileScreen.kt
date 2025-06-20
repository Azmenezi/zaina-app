package com.nbk.rise.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nbk.rise.data.requests.UpdateProfileRequest
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.AuthViewModel
import com.nbk.rise.viewmodels.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val authUiState by authViewModel.uiState
    val profileUiState by profileViewModel.uiState.collectAsState()
    
    var name by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf(listOf<String>()) }
    var newSkill by remember { mutableStateOf("") }
    var linkedinUrl by remember { mutableStateOf("") }
    
    // Load current profile when user is available
    LaunchedEffect(authUiState.userDetails?.id) {
        authUiState.userDetails?.id?.let { userId ->
            profileViewModel.loadProfile(userId)
        }
    }
    
    // Populate form when profile loads
    LaunchedEffect(profileUiState.profile) {
        val profile = profileUiState.profile
        if (profile != null) {
            name = profile.name
            position = profile.position ?: ""
            company = profile.company ?: ""
            bio = profile.bio ?: ""
            skills = profile.skills
            linkedinUrl = profile.linkedinUrl ?: ""
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LuxuryGray)
    ) {
        // Header - matching dashboard pattern
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = PrimaryColor
            ),
            shape = RoundedCornerShape(
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Update your profile information",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Profile Photo Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceElevated
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (profileUiState.profile?.imageUrl != null) {
                                AsyncImage(
                                    model = profileUiState.profile?.imageUrl,
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
                                            imageVector = Icons.Default.Camera,
                                            contentDescription = "Add Photo",
                                            modifier = Modifier.size(32.dp),
                                            tint = PrimaryColor
                                        )
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedButton(
                            onClick = { /* TODO: Implement photo picker */ },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryColor
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Camera,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Change Photo")
                        }
                    }
                }
            }
            
            item {
                // Basic Information
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceElevated
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Basic Information",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryColor
                        )
                        
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name *") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor
                            )
                        )
                        
                        OutlinedTextField(
                            value = position,
                            onValueChange = { position = it },
                            label = { Text("Position") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor
                            )
                        )
                        
                        OutlinedTextField(
                            value = company,
                            onValueChange = { company = it },
                            label = { Text("Company") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor
                            )
                        )
                        
                        OutlinedTextField(
                            value = linkedinUrl,
                            onValueChange = { linkedinUrl = it },
                            label = { Text("LinkedIn URL") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor
                            )
                        )
                    }
                }
            }
            
            item {
                // Skills Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceElevated
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Skills",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryColor
                        )
                        
                        // Add new skill
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newSkill,
                                onValueChange = { newSkill = it },
                                label = { Text("Add Skill") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryColor,
                                    focusedLabelColor = PrimaryColor
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (newSkill.isNotBlank() && newSkill !in skills) {
                                        skills = skills + newSkill
                                        newSkill = ""
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Skill",
                                    tint = PrimaryColor
                                )
                            }
                        }
                        
                        // Skills chips
                        if (skills.isNotEmpty()) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(skills) { skill ->
                                    InputChip(
                                        onClick = { },
                                        label = { Text(skill) },
                                        selected = false,
                                        trailingIcon = {
                                            IconButton(
                                                onClick = {
                                                    skills = skills - skill
                                                },
                                                modifier = Modifier.size(18.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Remove",
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        },
                                        colors = InputChipDefaults.inputChipColors(
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
            
            item {
                // Bio Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceElevated
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "About Me",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryColor
                        )
                        
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("Bio") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            maxLines = 5,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor
                            )
                        )
                    }
                }
            }
            
            item {
                // Save Button
                Button(
                    onClick = {
                        authUiState.userDetails?.id?.let { userId ->
                            val request = UpdateProfileRequest(
                                name = name,
                                position = position.ifBlank { null },
                                company = company.ifBlank { null },
                                skills = skills,
                                bio = bio.ifBlank { null },
                                imageUrl = profileUiState.profile?.imageUrl,
                                linkedinUrl = linkedinUrl.ifBlank { null }
                            )
                            profileViewModel.updateProfile(userId, request)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = name.isNotBlank() && !profileUiState.isUpdating,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        contentColor = Color.White
                    )
                ) {
                    if (profileUiState.isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Save Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            // Error handling
            profileUiState.updateError?.let { error ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
    }
} 