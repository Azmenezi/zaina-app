package com.nbk.rise.data.dtos

import kotlinx.datetime.LocalDate
import java.util.UUID

data class CohortDto(
    val id: UUID,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val participantCount: Int,
    val status: CohortStatus
)

enum class CohortStatus {
    UPCOMING, ACTIVE, COMPLETED
} 