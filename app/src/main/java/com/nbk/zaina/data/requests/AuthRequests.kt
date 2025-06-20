package com.nbk.rise.data.requests

import java.util.UUID

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val position: String? = null,
    val company: String? = null,
    val bio: String? = null,
    val cohortId: UUID? = null
)

data class RsvpRequest(
    val eventId: UUID,
    val status: com.nbk.rise.data.dtos.RsvpStatus
) 