package com.nbk.rise.data.repository

import com.nbk.rise.data.dtos.ConversationDto
import com.nbk.rise.data.dtos.MessageDto
import com.nbk.rise.data.requests.SendMessageRequest
import com.nbk.rise.network.MessageApiService
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageApiService: MessageApiService
) {
    
    suspend fun sendMessage(receiverId: UUID, content: String): Result<MessageDto> {
        return try {
            val response = messageApiService.sendMessage(SendMessageRequest(receiverId, content))
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Message sending failed"))
            } else {
                Result.failure(Exception("Failed to send message: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getConversation(otherUserId: UUID): Result<ConversationDto> {
        return try {
            val response = messageApiService.getConversation(otherUserId)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Conversation not found"))
            } else {
                Result.failure(Exception("Failed to get conversation: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun markMessageAsRead(messageId: UUID): Result<MessageDto> {
        return try {
            val response = messageApiService.markMessageAsRead(messageId)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Failed to mark as read"))
            } else {
                Result.failure(Exception("Failed to mark message as read: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 