package com.nbk.rise.network

import com.nbk.rise.data.dtos.ProfileDto
import com.nbk.rise.data.dtos.UserDto
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.data.dtos.UserSummaryDto
import com.nbk.rise.data.requests.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface UserApiService {
    @GET("api/users/me")
    suspend fun getCurrentUser(): Response<UserDto>
    
    @GET("api/users")
    suspend fun getAllUsers(): Response<List<UserSummaryDto>>
    
    @GET("api/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: UUID): Response<UserDto>
    
    @GET("api/users/role/{role}")
    suspend fun getUsersByRole(@Path("role") role: UserRole): Response<List<UserSummaryDto>>
    
    @GET("api/users/cohort/{cohortId}")
    suspend fun getUsersByCohort(@Path("cohortId") cohortId: UUID): Response<List<UserSummaryDto>>
}

interface ProfileApiService {
    @GET("api/profiles/{userId}")
    suspend fun getProfileByUserId(@Path("userId") userId: UUID): Response<ProfileDto>
    
    @PUT("api/profiles/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: UUID,
        @Body request: UpdateProfileRequest
    ): Response<ProfileDto>
    
    @GET("api/profiles/search")
    suspend fun searchProfiles(@Query("query") query: String): Response<List<ProfileDto>>
} 