package com.nbk.rise.data.repository

import com.nbk.rise.data.dtos.EventDto
import com.nbk.rise.data.dtos.EventRsvpDto
import com.nbk.rise.data.dtos.RsvpStatus
import com.nbk.rise.data.requests.RsvpRequest
import com.nbk.rise.network.EventApiService
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventApiService: EventApiService
) {
    
    suspend fun getAllEvents(): Result<List<EventDto>> {
        return try {
            val response = eventApiService.getAllEvents()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get events: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getPublicEvents(): Result<List<EventDto>> {
        return try {
            val response = eventApiService.getPublicEvents()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get public events: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUpcomingEvents(): Result<List<EventDto>> {
        return try {
            val response = eventApiService.getUpcomingEvents()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get upcoming events: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getEventById(eventId: UUID): Result<EventDto> {
        return try {
            val response = eventApiService.getEventById(eventId)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Event not found"))
            } else {
                Result.failure(Exception("Failed to get event: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserEvents(userId: UUID): Result<List<EventDto>> {
        return try {
            val response = eventApiService.getUserEvents(userId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get user events: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun rsvpToEvent(eventId: UUID, status: RsvpStatus): Result<EventRsvpDto> {
        return try {
            val response = eventApiService.rsvpToEvent(RsvpRequest(eventId, status))
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("RSVP failed"))
            } else {
                Result.failure(Exception("Failed to RSVP: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getEventAttendees(eventId: UUID): Result<List<EventRsvpDto>> {
        return try {
            val response = eventApiService.getEventAttendees(eventId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get event attendees: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 