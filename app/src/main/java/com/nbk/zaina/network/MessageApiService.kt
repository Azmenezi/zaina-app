package com.nbk.rise.network

import com.nbk.rise.data.dtos.ConversationDto
import com.nbk.rise.data.dtos.MessageDto
import com.nbk.rise.data.requests.SendMessageRequest
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface MessageApiService {
    @POST("api/messages")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<MessageDto>
    
    @GET("api/messages/thread/{otherUserId}")
    suspend fun getConversation(@Path("otherUserId") otherUserId: UUID): Response<ConversationDto>
    
    @PUT("api/messages/{messageId}/read")
    suspend fun markMessageAsRead(@Path("messageId") messageId: UUID): Response<MessageDto>
} 