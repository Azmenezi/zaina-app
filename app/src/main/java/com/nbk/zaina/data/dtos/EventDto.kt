package com.nbk.rise.data.dtos

import kotlinx.datetime.Instant
import java.util.UUID

data class EventDto(
    val id: UUID,
    val title: String,
    val description: String? = null,
    val date: Instant,
    val location: String? = null,
    val isPublic: Boolean,
    val rsvpStatus: RsvpStatus? = null,
    val attendeeCount: Int
)

data class EventRsvpDto(
    val eventId: UUID,
    val userId: UUID,
    val userName: String? = null,
    val rsvpStatus: RsvpStatus
)

enum class RsvpStatus {
    GOING, NOT_GOING, INTERESTED
} 