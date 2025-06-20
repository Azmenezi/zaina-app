package com.nbk.rise.network

import com.nbk.rise.data.dtos.ConnectionDto
import com.nbk.rise.data.requests.CreateConnectionRequest
import com.nbk.rise.data.requests.UpdateConnectionRequest
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface ConnectionApiService {
    @POST("api/connections")
    suspend fun createConnection(@Body request: CreateConnectionRequest): Response<ConnectionDto>
    
    @PUT("api/connections/{connectionId}")
    suspend fun updateConnection(
        @Path("connectionId") connectionId: UUID,
        @Body request: UpdateConnectionRequest
    ): Response<ConnectionDto>
    
    @GET("api/connections/pending")
    suspend fun getPendingConnections(): Response<List<ConnectionDto>>
    
    @GET("api/connections/accepted")
    suspend fun getAcceptedConnections(): Response<List<ConnectionDto>>
} 