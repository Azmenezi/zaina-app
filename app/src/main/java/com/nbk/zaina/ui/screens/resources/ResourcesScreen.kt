package com.nbk.rise.ui.screens.resources

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nbk.rise.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesScreen(onResourceClick: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf("All") }
    
    val categories = listOf("All", "Leadership", "Skills", "Networking", "Career")
    val mockResources = listOf(
        MockResource("1", "Leadership in the Digital Age", "Learn modern leadership techniques", "Leadership", "PDF"),
        MockResource("2", "Effective Communication", "Master your communication skills", "Skills", "Video"),
        MockResource("3", "Networking Strategies", "Build meaningful professional connections", "Networking", "Article"),
        MockResource("4", "Career Development Guide", "Plan your career progression", "Career", "PDF"),
        MockResource("5", "Public Speaking Mastery", "Overcome fear and speak with confidence", "Skills", "Video"),
        MockResource("6", "Strategic Thinking Workshop", "Develop strategic leadership mindset", "Leadership", "Article")
    )
    
    val filteredResources = if (selectedCategory == "All") {
        mockResources
    } else {
        mockResources.filter { it.category == selectedCategory }
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
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Resources",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Access learning materials and development tools",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
                
                // Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            selected = selectedCategory == category,
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
        
        if (filteredResources.isEmpty()) {
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
                                imageVector = Icons.Default.Folder,
                                contentDescription = "No Resources",
                                modifier = Modifier.size(32.dp),
                                tint = PrimaryColor
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "No resources available",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor
                    )
                    
                    Text(
                        text = "Check back later for new learning materials",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            // Resources Count
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredResources.size} ${if (filteredResources.size == 1) "resource" else "resources"}",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Resources List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredResources) { resource ->
                    ResourceItem(
                        resource = resource,
                        onClick = { onResourceClick(resource.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ResourceItem(
    resource: MockResource,
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
            // Resource Type Icon
            Card(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = when (resource.type) {
                        "PDF" -> ErrorRed.copy(alpha = 0.2f)
                        "Video" -> InfoBlue.copy(alpha = 0.2f)
                        "Article" -> SuccessGreen.copy(alpha = 0.2f)
                        else -> AccentLight
                    }
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (resource.type) {
                            "PDF" -> Icons.Default.Article
                            "Video" -> Icons.Default.PlayArrow
                            "Article" -> Icons.Default.School
                            else -> Icons.Default.Folder
                        },
                        contentDescription = resource.type,
                        modifier = Modifier.size(24.dp),
                        tint = when (resource.type) {
                            "PDF" -> ErrorRed
                            "Video" -> InfoBlue
                            "Article" -> SuccessGreen
                            else -> PrimaryColor
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Resource Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title and Category Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = resource.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // Category Badge
                    AssistChip(
                        onClick = { },
                        label = { 
                            Text(
                                text = resource.category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            ) 
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = AccentColor.copy(alpha = 0.3f),
                            labelColor = AccentDark
                        ),
                        modifier = Modifier.height(24.dp)
                    )
                }
                
                // Description
                Text(
                    text = resource.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Type Badge
            AssistChip(
                onClick = { },
                label = { 
                    Text(
                        text = resource.type,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    ) 
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = when (resource.type) {
                        "PDF" -> ErrorRed.copy(alpha = 0.2f)
                        "Video" -> InfoBlue.copy(alpha = 0.2f)
                        "Article" -> SuccessGreen.copy(alpha = 0.2f)
                        else -> SecondaryColor.copy(alpha = 0.2f)
                    },
                    labelColor = when (resource.type) {
                        "PDF" -> ErrorRed
                        "Video" -> InfoBlue
                        "Article" -> SuccessGreen
                        else -> SecondaryColor
                    }
                ),
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

// Mock data class
private data class MockResource(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val type: String
)

@Composable
fun ResourceDetailScreen(resourceId: String, onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Resource Detail: $resourceId",
            style = MaterialTheme.typography.headlineMedium
        )
    }
} 