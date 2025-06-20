package com.nbk.rise.data.repository

import com.nbk.rise.data.dtos.ConnectionDto
import com.nbk.rise.data.dtos.ConnectionStatus
import com.nbk.rise.data.dtos.ConnectionType
import com.nbk.rise.data.requests.CreateConnectionRequest
import com.nbk.rise.data.requests.UpdateConnectionRequest
import com.nbk.rise.network.ConnectionApiService
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionRepository @Inject constructor(
    private val connectionApiService: ConnectionApiService
) {
    
    suspend fun createConnection(targetId: UUID, type: ConnectionType): Result<ConnectionDto> {
        return try {
            val response = connectionApiService.createConnection(CreateConnectionRequest(targetId, type))
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Connection creation failed"))
            } else {
                Result.failure(Exception("Failed to create connection: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateConnection(connectionId: UUID, status: ConnectionStatus): Result<ConnectionDto> {
        return try {
            val response = connectionApiService.updateConnection(connectionId, UpdateConnectionRequest(status))
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Connection update failed"))
            } else {
                Result.failure(Exception("Failed to update connection: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getPendingConnections(): Result<List<ConnectionDto>> {
        return try {
            val response = connectionApiService.getPendingConnections()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get pending connections: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAcceptedConnections(): Result<List<ConnectionDto>> {
        return try {
            val response = connectionApiService.getAcceptedConnections()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get accepted connections: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 