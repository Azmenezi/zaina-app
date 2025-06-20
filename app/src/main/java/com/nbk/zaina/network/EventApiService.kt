package com.nbk.rise.network

import com.nbk.rise.data.dtos.EventDto
import com.nbk.rise.data.dtos.EventRsvpDto
import com.nbk.rise.data.requests.RsvpRequest
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface EventApiService {
    @GET("api/events")
    suspend fun getAllEvents(): Response<List<EventDto>>
    
    @GET("api/events/public")
    suspend fun getPublicEvents(): Response<List<EventDto>>
    
    @GET("api/events/upcoming")
    suspend fun getUpcomingEvents(): Response<List<EventDto>>
    
    @GET("api/events/{eventId}")
    suspend fun getEventById(@Path("eventId") eventId: UUID): Response<EventDto>
    
    @GET("api/events/user/{userId}")
    suspend fun getUserEvents(@Path("userId") userId: UUID): Response<List<EventDto>>
    
    @GET("api/events/{eventId}/attendees")
    suspend fun getEventAttendees(@Path("eventId") eventId: UUID): Response<List<EventRsvpDto>>
    
    @POST("api/events/rsvp")
    suspend fun rsvpToEvent(@Body request: RsvpRequest): Response<EventRsvpDto>
} 