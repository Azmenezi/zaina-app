// FULL CODE — Only visuals changed: top padding and icon color backgrounds

package com.nbk.rise.ui.screens.resources

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nbk.rise.R
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

    val filteredResources = if (selectedCategory == "All") mockResources else mockResources.filter { it.category == selectedCategory }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.05f))
            .blur(30.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, bottom = 104.dp)
        ) {

        Spacer(modifier = Modifier.height(100.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
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
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { category ->
                            FilterChip(
                                onClick = { selectedCategory = category },
                                label = { Text(category) },
                                selected = selectedCategory == category,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color.White,
                                    selectedLabelColor = Color.Black,
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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Card(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Folder, contentDescription = "No Resources", tint = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No resources available", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = Color.White)
                        Text("Check back later for new learning materials", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            } else {
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
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredResources) { resource ->
                        ResourceItem(resource = resource, onClick = { onResourceClick(resource.id) })
                    }
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
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = when (resource.type) {
                        "PDF" -> Color(0xFFFF6B6B)
                        "Video" -> Color(0xFF4A90E2)
                        "Article" -> Color(0xFF81C784)
                        else -> Color.White.copy(alpha = 0.2f)
                    }
                )
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (resource.type) {
                            "PDF" -> Icons.Default.Article
                            "Video" -> Icons.Default.PlayArrow
                            "Article" -> Icons.Default.School
                            else -> Icons.Default.Folder
                        },
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = resource.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = resource.category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color.White.copy(alpha = 0.3f),
                            labelColor = Color.Black
                        ),
                        modifier = Modifier.height(24.dp)
                    )
                }

                Text(
                    text = resource.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            AssistChip(
                onClick = {},
                label = {
                    Text(
                        text = resource.type,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    labelColor = Color.White
                ),
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

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