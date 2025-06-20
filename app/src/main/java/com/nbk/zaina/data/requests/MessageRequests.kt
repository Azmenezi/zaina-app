package com.nbk.rise.data.requests

import com.nbk.rise.data.dtos.ConnectionStatus
import com.nbk.rise.data.dtos.ConnectionType
import java.util.UUID

data class SendMessageRequest(
    val receiverId: UUID,
    val content: String
)

data class UpdateProfileRequest(
    val name: String,
    val position: String? = null,
    val company: String? = null,
    val skills: List<String>,
    val bio: String? = null,
    val imageUrl: String? = null,
    val linkedinUrl: String? = null
)

data class CreateConnectionRequest(
    val targetId: UUID,
    val type: ConnectionType
)

data class UpdateConnectionRequest(
    val status: ConnectionStatus
) 