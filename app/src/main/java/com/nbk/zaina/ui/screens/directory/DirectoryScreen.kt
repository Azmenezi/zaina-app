package com.nbk.rise.ui.screens.directory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.nbk.rise.data.dtos.ProfileDto
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.data.dtos.UserSummaryDto
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectoryScreen(
    onProfileClick: (String) -> Unit,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val userUiState by userViewModel.uiState.collectAsStateWithLifecycle()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    
    LaunchedEffect(Unit) {
        userViewModel.loadAllUsers()
    }
    
    // Filter users based on search and role
    val filteredUsers = remember(userUiState.users, searchQuery, selectedRole) {
        userUiState.users.filter { user ->
            val matchesSearch = if (searchQuery.isBlank()) true else {
                user.name?.contains(searchQuery, ignoreCase = true) == true ||
                user.company?.contains(searchQuery, ignoreCase = true) == true ||
                user.position?.contains(searchQuery, ignoreCase = true) == true
            }
            val matchesRole = selectedRole == null || user.role == selectedRole
            matchesSearch && matchesRole
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LuxuryGray)
    ) {
        // Header - matching other screens
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
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Directory",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Connect with fellow RISE members",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
                
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { 
                        Text(
                            "Search by name, company, or position...",
                            color = Color.White.copy(alpha = 0.7f)
                        ) 
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                // Role Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            onClick = { selectedRole = null },
                            label = { Text("All") },
                            selected = selectedRole == null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.White,
                                selectedLabelColor = PrimaryColor,
                                containerColor = Color.White.copy(alpha = 0.2f),
                                labelColor = Color.White
                            )
                        )
                    }
                    
                    items(UserRole.values()) { role ->
                        FilterChip(
                            onClick = { selectedRole = if (selectedRole == role) null else role },
                            label = { 
                                Text(role.name.lowercase().replaceFirstChar { it.uppercase() }) 
                            },
                            selected = selectedRole == role,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.White,
                                selectedLabelColor = PrimaryColor,
                                containerColor = Color.White.copy(alpha = 0.2f),
                                labelColor = Color.White
                            )
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (userUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        } else if (filteredUsers.isEmpty()) {
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
                                imageVector = Icons.Default.Person,
                                contentDescription = "No Members",
                                modifier = Modifier.size(32.dp),
                                tint = PrimaryColor
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = if (searchQuery.isNotEmpty() || selectedRole != null) 
                            "No members found" 
                        else 
                            "No members yet",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor
                    )
                    
                    Text(
                        text = if (searchQuery.isNotEmpty() || selectedRole != null) 
                            "Try adjusting your search or filters" 
                        else 
                            "Members will appear here as they join",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            // Results Count and Clear Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredUsers.size} ${if (filteredUsers.size == 1) "member" else "members"}",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary
                )
                
                if (searchQuery.isNotEmpty() || selectedRole != null) {
                    TextButton(
                        onClick = {
                            searchQuery = ""
                            selectedRole = null
                        }
                    ) {
                        Text("Clear filters", color = PrimaryColor)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Members List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredUsers) { user ->
                    DirectoryMemberItem(
                        user = user,
                        onClick = { onProfileClick(user.id.toString()) }
                    )
                }
            }
        }
        
        // Error handling
        userUiState.error?.let { error ->
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
private fun DirectoryMemberItem(
    user: UserSummaryDto,
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
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Member Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name and Role Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = user.name ?: "Anonymous",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // Role Badge
                    AssistChip(
                        onClick = { },
                        label = { 
                            Text(
                                text = user.role.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            ) 
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = when (user.role) {
                                UserRole.MENTOR -> NBKGold.copy(alpha = 0.2f)
                                UserRole.ALUMNA -> AccentColor.copy(alpha = 0.3f)
                                UserRole.PARTICIPANT -> PrimaryColor.copy(alpha = 0.2f)
                                UserRole.APPLICANT -> SecondaryColor.copy(alpha = 0.2f)
                            },
                            labelColor = when (user.role) {
                                UserRole.MENTOR -> NBKGold
                                UserRole.ALUMNA -> AccentDark
                                UserRole.PARTICIPANT -> PrimaryColor
                                UserRole.APPLICANT -> SecondaryColor
                            }
                        ),
                        modifier = Modifier.height(24.dp)
                    )
                }
                
                // Position and Company
                if (user.position != null || user.company != null) {
                    Text(
                        text = listOfNotNull(user.position, user.company).joinToString(" at "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileDetailScreen(userId: String, onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Profile Detail: $userId",
            style = MaterialTheme.typography.headlineMedium
        )
    }
} 