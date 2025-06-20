package com.nbk.rise.data.dtos

import java.util.UUID

data class ProfileDto(
    val userId: UUID,
    val name: String,
    val position: String? = null,
    val company: String? = null,
    val skills: List<String>,
    val bio: String? = null,
    val imageUrl: String? = null,
    val linkedinUrl: String? = null
) 