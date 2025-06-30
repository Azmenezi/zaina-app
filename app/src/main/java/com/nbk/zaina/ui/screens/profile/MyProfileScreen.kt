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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nbk.rise.R
import com.nbk.rise.data.requests.UpdateProfileRequest
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.AuthViewModel
import com.nbk.rise.viewmodels.ProfileViewModel

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

    LaunchedEffect(authUiState.userDetails?.id) {
        authUiState.userDetails?.id?.let { profileViewModel.loadProfile(it) }
    }

    LaunchedEffect(profileUiState.profile) {
        profileUiState.profile?.let { profile ->
            name = profile.name
            position = profile.position.orEmpty()
            company = profile.company.orEmpty()
            bio = profile.bio.orEmpty()
            skills = profile.skills
            linkedinUrl = profile.linkedinUrl.orEmpty()
        }
    }

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
                // Header
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

            item {
                // Profile Image
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.08f))
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
                                    model = profileUiState.profile.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                )
                            } else {
                                Card(
                                    modifier = Modifier.size(120.dp),
                                    shape = CircleShape,
                                    colors = CardDefaults.cardColors(containerColor = AccentLight)
                                ) {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Camera, contentDescription = null, tint = PrimaryColor)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(onClick = { /* photo picker */ }) {
                            Icon(Icons.Default.Camera, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Change Photo")
                        }
                    }
                }
            }

            item {
                // Basic Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.08f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Basic Information", color = Color.White, fontWeight = FontWeight.SemiBold)

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name *") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedContainerColor = Color.Transparent
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
                // Skills
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.08f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Skills", color = Color.White, fontWeight = FontWeight.SemiBold)

                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                            IconButton(onClick = {
                                if (newSkill.isNotBlank() && newSkill !in skills) {
                                    skills = skills + newSkill
                                    newSkill = ""
                                }
                            }) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = PrimaryColor)
                            }
                        }

                        if (skills.isNotEmpty()) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(skills) { skill ->
                                    InputChip(
                                        selected = false, // or true depending on your logic
                                        onClick = { },
                                        label = { Text(skill) },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = { skills = skills - skill },
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
                // Bio
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.08f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("About Me", color = Color.White, fontWeight = FontWeight.SemiBold)

                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("Bio") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
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
                            profileViewModel.updateProfile(
                                userId,
                                UpdateProfileRequest(
                                    name = name,
                                    position = position.ifBlank { null },
                                    company = company.ifBlank { null },
                                    skills = skills,
                                    bio = bio.ifBlank { null },
                                    imageUrl = profileUiState.profile?.imageUrl,
                                    linkedinUrl = linkedinUrl.ifBlank { null }
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = name.isNotBlank() && !profileUiState.isUpdating,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Brush.horizontalGradient(
                            listOf(Color(0xFFFFA726), Color(0xFF64B5F6))
                        ).toBrushColor(),
                        contentColor = Color.White
                    )
                ) {
                    if (profileUiState.isUpdating) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text("Save Profile", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            profileUiState.updateError?.let { error ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.1f))
                    ) {
                        Text(error, color = ErrorRed, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

private fun Brush.toBrushColor(): Color {
    // This is a placeholder. If you want a gradient-colored button,
    // you'll need to draw it with Box+Brush instead.
    return Color(0xFF64B5F6)
}
