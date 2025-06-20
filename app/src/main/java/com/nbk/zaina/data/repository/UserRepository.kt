package com.nbk.rise.data.repository

import com.nbk.rise.data.dtos.ProfileDto
import com.nbk.rise.data.dtos.UserDto
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.data.dtos.UserSummaryDto
import com.nbk.rise.data.requests.UpdateProfileRequest
import com.nbk.rise.network.ProfileApiService
import com.nbk.rise.network.UserApiService
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val profileApiService: ProfileApiService
) {
    
    suspend fun getCurrentUser(): Result<UserDto> {
        return try {
            val response = userApiService.getCurrentUser()
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("User not found"))
            } else {
                Result.failure(Exception("Failed to get current user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAllUsers(): Result<List<UserSummaryDto>> {
        return try {
            val response = userApiService.getAllUsers()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get users: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserById(userId: UUID): Result<UserDto> {
        return try {
            val response = userApiService.getUserById(userId)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("User not found"))
            } else {
                Result.failure(Exception("Failed to get user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUsersByRole(role: UserRole): Result<List<UserSummaryDto>> {
        return try {
            val response = userApiService.getUsersByRole(role)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get users by role: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUsersByCohort(cohortId: UUID): Result<List<UserSummaryDto>> {
        return try {
            val response = userApiService.getUsersByCohort(cohortId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get users by cohort: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getProfileByUserId(userId: UUID): Result<ProfileDto> {
        return try {
            val response = profileApiService.getProfileByUserId(userId)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Profile not found"))
            } else {
                Result.failure(Exception("Failed to get profile: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateProfile(userId: UUID, request: UpdateProfileRequest): Result<ProfileDto> {
        return try {
            val response = profileApiService.updateProfile(userId, request)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Profile update failed"))
            } else {
                Result.failure(Exception("Failed to update profile: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchProfiles(query: String): Result<List<ProfileDto>> {
        return try {
            val response = profileApiService.searchProfiles(query)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to search profiles: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 