package com.nbk.rise.network

import com.nbk.rise.data.dtos.ResourceDto
import com.nbk.rise.data.dtos.ResourceType
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface ResourceApiService {
    @GET("api/resources")
    suspend fun getAllResources(): Response<List<ResourceDto>>
    
    @GET("api/resources/{resourceId}")
    suspend fun getResourceById(@Path("resourceId") resourceId: UUID): Response<ResourceDto>
    
    @GET("api/resources/type/{type}")
    suspend fun getResourcesByType(@Path("type") type: ResourceType): Response<List<ResourceDto>>
    
    @GET("api/resources/module/{module}")
    suspend fun getResourcesByModule(@Path("module") module: String): Response<List<ResourceDto>>
    
    @GET("api/resources/search")
    suspend fun searchResources(@Query("query") query: String): Response<List<ResourceDto>>
} 