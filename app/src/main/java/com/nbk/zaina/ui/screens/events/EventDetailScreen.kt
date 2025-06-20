package com.nbk.rise.ui.screens.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nbk.rise.data.dtos.EventRsvpDto
import com.nbk.rise.data.dtos.RsvpStatus
import com.nbk.rise.ui.theme.*
import com.nbk.rise.viewmodels.EventViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    onNavigateBack: () -> Unit,
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val eventUiState by eventViewModel.uiState
    val eventUUID = remember { UUID.fromString(eventId) }
    
    LaunchedEffect(eventUUID) {
        eventViewModel.loadEventById(eventUUID)
        eventViewModel.loadEventAttendees(eventUUID)
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { 
                Text(
                    text = eventUiState.selectedEvent?.title ?: "Event Details",
                    maxLines = 1
                ) 
            },
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
        
        if (eventUiState.isLoadingDetail) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        } else {
            eventUiState.selectedEvent?.let { event ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LuxuryGray),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Event Header Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = SurfaceElevated
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                // Title and Badge
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = event.title,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryColor,
                                        modifier = Modifier.weight(1f)
                                    )
                                    
                                    AssistChip(
                                        onClick = { },
                                        label = { 
                                            Text(
                                                text = if (event.isPublic) "Public" else "Private",
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
                                        )
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Event Info Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                                ) {
                                    // Date & Time
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CalendarToday,
                                                contentDescription = "Date",
                                                modifier = Modifier.size(20.dp),
                                                tint = AccentColor
                                            )
                                            Text(
                                                text = "Date & Time",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = TextSecondary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = formatDetailEventDate(event.date),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    
                                    // Location
                                    if (event.location != null) {
                                        Column {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.LocationOn,
                                                    contentDescription = "Location",
                                                    modifier = Modifier.size(20.dp),
                                                    tint = AccentColor
                                                )
                                                Text(
                                                    text = "Location",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = TextSecondary,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = event.location,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // RSVP Section
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = SurfaceElevated
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Will you attend this event?",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryColor
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Going Button
                                    Button(
                                        onClick = {
                                            eventViewModel.rsvpToEvent(eventUUID, RsvpStatus.GOING)
                                        },
                                        modifier = Modifier.weight(1f),
                                        enabled = !eventUiState.isRsvpLoading,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (event.rsvpStatus == RsvpStatus.GOING) 
                                                SuccessGreen else PrimaryColor,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        if (eventUiState.isRsvpLoading && event.rsvpStatus != RsvpStatus.GOING) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                color = Color.White
                                            )
                                        } else {
                                            Text(
                                                text = if (event.rsvpStatus == RsvpStatus.GOING) "✓ Going" else "Going",
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                    
                                    // Interested Button
                                    OutlinedButton(
                                        onClick = {
                                            eventViewModel.rsvpToEvent(eventUUID, RsvpStatus.INTERESTED)
                                        },
                                        modifier = Modifier.weight(1f),
                                        enabled = !eventUiState.isRsvpLoading,
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (event.rsvpStatus == RsvpStatus.INTERESTED) 
                                                InfoBlue.copy(alpha = 0.1f) else Color.Transparent,
                                            contentColor = if (event.rsvpStatus == RsvpStatus.INTERESTED) 
                                                InfoBlue else PrimaryColor
                                        )
                                    ) {
                                        if (eventUiState.isRsvpLoading && event.rsvpStatus != RsvpStatus.INTERESTED) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                color = PrimaryColor
                                            )
                                        } else {
                                            Text(
                                                text = if (event.rsvpStatus == RsvpStatus.INTERESTED) "✓ Interested" else "Interested",
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                    
                                    // Not Going Button
                                    OutlinedButton(
                                        onClick = {
                                            eventViewModel.rsvpToEvent(eventUUID, RsvpStatus.NOT_GOING)
                                        },
                                        enabled = !eventUiState.isRsvpLoading,
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (event.rsvpStatus == RsvpStatus.NOT_GOING) 
                                                ErrorRed.copy(alpha = 0.1f) else Color.Transparent,
                                            contentColor = if (event.rsvpStatus == RsvpStatus.NOT_GOING) 
                                                ErrorRed else TextSecondary
                                        )
                                    ) {
                                        if (eventUiState.isRsvpLoading && event.rsvpStatus != RsvpStatus.NOT_GOING) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                color = TextSecondary
                                            )
                                        } else {
                                            Text(
                                                text = if (event.rsvpStatus == RsvpStatus.NOT_GOING) "✓ Not Going" else "Not Going"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Description Section
                    if (event.description != null) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = SurfaceElevated
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        text = "About this event",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryColor
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = event.description,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextPrimary,
                                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                                    )
                                }
                            }
                        }
                    }
                    
                    // Attendees Section
                    if (eventUiState.eventAttendees.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = SurfaceElevated
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.People,
                                            contentDescription = "Attendees",
                                            modifier = Modifier.size(20.dp),
                                            tint = AccentColor
                                        )
                                        Text(
                                            text = "Attendees (${event.attendeeCount})",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = PrimaryColor
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(eventUiState.eventAttendees.take(10)) { attendee ->
                                            AttendeeItem(attendee = attendee)
                                        }
                                        
                                        if (eventUiState.eventAttendees.size > 10) {
                                            item {
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
                                                        Text(
                                                            text = "+${eventUiState.eventAttendees.size - 10}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            fontWeight = FontWeight.Bold,
                                                            color = PrimaryColor
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Error handling
        eventUiState.detailError?.let { error ->
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
                        onClick = { 
                            eventViewModel.loadEventById(eventUUID)
                            eventViewModel.clearDetailError()
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
        
        // RSVP Error handling
        eventUiState.rsvpError?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar or toast with RSVP error
                eventViewModel.clearRsvpError()
            }
        }
    }
}

@Composable
private fun AttendeeItem(attendee: EventRsvpDto) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Card(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = when (attendee.rsvpStatus) {
                    RsvpStatus.GOING -> SuccessGreen.copy(alpha = 0.2f)
                    RsvpStatus.INTERESTED -> InfoBlue.copy(alpha = 0.2f)
                    RsvpStatus.NOT_GOING -> ErrorRed.copy(alpha = 0.2f)
                }
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Attendee",
                    modifier = Modifier.size(24.dp),
                    tint = when (attendee.rsvpStatus) {
                        RsvpStatus.GOING -> SuccessGreen
                        RsvpStatus.INTERESTED -> InfoBlue
                        RsvpStatus.NOT_GOING -> ErrorRed
                    }
                )
            }
        }
        
        Text(
            text = attendee.userName ?: "Anonymous",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            maxLines = 1
        )
    }
}

private fun formatDetailEventDate(date: kotlinx.datetime.Instant): String {
    val localDateTime = date.toLocalDateTime(TimeZone.currentSystemDefault())
    val dayOfWeek = when (localDateTime.dayOfWeek.ordinal) {
        0 -> "Monday"
        1 -> "Tuesday"
        2 -> "Wednesday"
        3 -> "Thursday"
        4 -> "Friday"
        5 -> "Saturday"
        6 -> "Sunday"
        else -> ""
    }
    val month = when (localDateTime.monthNumber) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> ""
    }
    
    return "$dayOfWeek, $month ${localDateTime.dayOfMonth} at ${String.format("%02d:%02d", localDateTime.hour, localDateTime.minute)}"
} 