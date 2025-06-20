package com.nbk.rise.data.responses

import com.nbk.rise.data.dtos.UserRole
import java.util.UUID

data class JwtResponse(
    val token: String,
    val type: String,
    val id: UUID,
    val email: String,
    val role: UserRole,
    val name: String? = null
) 