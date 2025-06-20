package com.nbk.rise.data.dtos

import java.util.UUID

data class ResourceDto(
    val id: UUID,
    val title: String,
    val description: String? = null,
    val type: ResourceType,
    val url: String,
    val targetRoles: List<UserRole>,
    val module: String? = null
)

enum class ResourceType {
    PDF, VIDEO, LINK
} 