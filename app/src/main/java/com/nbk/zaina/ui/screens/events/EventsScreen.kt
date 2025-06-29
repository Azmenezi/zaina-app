package com.nbk.rise.ui.screens.events

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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nbk.rise.R
import com.nbk.rise.data.dtos.EventDto
import com.nbk.rise.data.dtos.RsvpStatus
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.EventViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    onEventClick: (String) -> Unit,
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val eventUiState by eventViewModel.uiState

    var showUpcomingOnly by remember { mutableStateOf(false) }
    var showPublicOnly by remember { mutableStateOf(false) }

    LaunchedEffect(showUpcomingOnly, showPublicOnly) {
        when {
            showUpcomingOnly -> eventViewModel.loadUpcomingEvents()
            else -> eventViewModel.loadEvents(showPublicOnly)
        }
    }

    LaunchedEffect(Unit) {
        eventViewModel.loadEvents()
    }

    val currentEvents = if (showUpcomingOnly) {
        eventUiState.upcomingEvents
    } else {
        eventUiState.events
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Events",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Discover upcoming RISE events and workshops",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                onClick = {
                                    showUpcomingOnly = false
                                    showPublicOnly = false
                                },
                                label = { Text("All Events") },
                                selected = !showUpcomingOnly && !showPublicOnly,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color.White,
                                    selectedLabelColor = Color.Black,
                                    containerColor = Color.White.copy(alpha = 0.2f),
                                    labelColor = Color.White
                                )
                            )
                        }

                        item {
                            FilterChip(
                                onClick = {
                                    showUpcomingOnly = true
                                    showPublicOnly = false
                                },
                                label = { Text("Upcoming") },
                                selected = showUpcomingOnly,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color.White,
                                    selectedLabelColor = Color.Black,
                                    containerColor = Color.White.copy(alpha = 0.2f),
                                    labelColor = Color.White
                                )
                            )
                        }

                        item {
                            FilterChip(
                                onClick = {
                                    showUpcomingOnly = false
                                    showPublicOnly = true
                                },
                                label = { Text("Public Only") },
                                selected = !showUpcomingOnly && showPublicOnly,
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

            if (eventUiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryColor)
                }
            } else if (currentEvents.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No events available", color = Color.White)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(currentEvents) { event ->
                        EventItem(
                            event = event,
                            onClick = { onEventClick(event.id.toString()) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun EventItem(
    event: EventDto,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Event Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColor,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Public/Private Badge
                AssistChip(
                    onClick = { },
                    label = { 
                        Text(
                            text = if (event.isPublic) "Public" else "Private",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium
                        ) 
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (event.isPublic) 
                            SuccessGreen.copy(alpha = 0.2f) 
                        else 
                            SecondaryColor.copy(alpha = 0.2f),
                        labelColor = if (event.isPublic) 
                            SuccessGreen 
                        else 
                            SecondaryColor
                    ),
                    modifier = Modifier.height(24.dp)
                )
            }
            
            // Description
            if (event.description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Event Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Date",
                        modifier = Modifier.size(16.dp),
                        tint = AccentColor
                    )
                    Text(
                        text = formatEventDate(LocalDateTime.parse(event.date) ),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                
                // Location
                if (event.location != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier.size(16.dp),
                            tint = AccentColor
                        )
                        Text(
                            text = event.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Bottom Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Attendee Count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = "Attendees",
                        modifier = Modifier.size(16.dp),
                        tint = AccentColor
                    )
                    Text(
                        text = "${event.attendeeCount} attending",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                
                // RSVP Status
                event.rsvpStatus?.let { status ->
                    AssistChip(
                        onClick = { },
                        label = { 
                            Text(
                                text = when (status) {
                                    RsvpStatus.GOING -> "Going"
                                    RsvpStatus.NOT_GOING -> "Not Going"
                                    RsvpStatus.INTERESTED -> "Interested"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            ) 
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = when (status) {
                                RsvpStatus.GOING -> SuccessGreen.copy(alpha = 0.2f)
                                RsvpStatus.NOT_GOING -> ErrorRed.copy(alpha = 0.2f)
                                RsvpStatus.INTERESTED -> InfoBlue.copy(alpha = 0.2f)
                            },
                            labelColor = when (status) {
                                RsvpStatus.GOING -> SuccessGreen
                                RsvpStatus.NOT_GOING -> ErrorRed
                                RsvpStatus.INTERESTED -> InfoBlue
                            }
                        ),
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}

private fun formatEventDate(date: LocalDateTime): String {
    val localDateTime = date
    val now = kotlinx.datetime.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    
    return when {
        localDateTime.date == now.date -> "Today ${String.format("%02d:%02d", localDateTime.hour, localDateTime.minute)}"
        localDateTime.date.dayOfYear == now.date.dayOfYear + 1 -> "Tomorrow ${String.format("%02d:%02d", localDateTime.hour, localDateTime.minute)}"
        else -> "${localDateTime.dayOfMonth}/${localDateTime.monthNumber} ${String.format("%02d:%02d", localDateTime.hour, localDateTime.minute)}"
    }
} 