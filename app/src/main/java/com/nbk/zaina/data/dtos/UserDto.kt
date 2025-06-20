package com.nbk.rise.data.dtos

import java.util.UUID

data class UserDto(
    val id: UUID,
    val email: String,
    val role: UserRole,
    val cohortId: UUID? = null,
    val profile: ProfileDto? = null
)

data class UserSummaryDto(
    val id: UUID,
    val email: String,
    val role: UserRole,
    val name: String? = null,
    val position: String? = null,
    val company: String? = null
)

enum class UserRole {
    APPLICANT, PARTICIPANT, ALUMNA, MENTOR
} 