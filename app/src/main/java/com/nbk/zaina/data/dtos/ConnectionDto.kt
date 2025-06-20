package com.nbk.rise.data.dtos

import kotlinx.datetime.Instant
import java.util.UUID

data class ConnectionDto(
    val id: UUID,
    val requesterId: UUID,
    val targetId: UUID,
    val type: ConnectionType,
    val status: ConnectionStatus,
    val requestedAt: Instant,
    val respondedAt: Instant? = null,
    val requesterName: String? = null,
    val targetName: String? = null
)

enum class ConnectionType {
    CONNECT, MENTORSHIP
}

enum class ConnectionStatus {
    PENDING, ACCEPTED, DECLINED
} 