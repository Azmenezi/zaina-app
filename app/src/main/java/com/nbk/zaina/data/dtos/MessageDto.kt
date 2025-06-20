package com.nbk.rise.data.dtos

import kotlinx.datetime.Instant
import java.util.UUID

data class MessageDto(
    val id: UUID,
    val senderId: UUID,
    val receiverId: UUID,
    val content: String,
    val sentAt: Instant,
    val isRead: Boolean
)

data class ConversationDto(
    val messages: List<MessageDto>,
    val otherUserId: UUID,
    val otherUserName: String? = null
)

data class ConversationSummaryDto(
    val otherUserId: UUID,
    val otherUserName: String,
    val lastMessage: String? = null,
    val lastMessageTime: Instant? = null,
    val unreadCount: Int = 0
) 