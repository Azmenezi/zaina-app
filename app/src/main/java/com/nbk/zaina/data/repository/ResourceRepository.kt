package com.nbk.rise.data.repository

import com.nbk.rise.data.dtos.ResourceDto
import com.nbk.rise.data.dtos.ResourceType
import com.nbk.rise.network.ResourceApiService
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceRepository @Inject constructor(
    private val resourceApiService: ResourceApiService
) {
    
    suspend fun getAllResources(): Result<List<ResourceDto>> {
        return try {
            val response = resourceApiService.getAllResources()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get resources: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getResourceById(resourceId: UUID): Result<ResourceDto> {
        return try {
            val response = resourceApiService.getResourceById(resourceId)
            if (response.isSuccessful) {
                response.body()?.let { 
                    Result.success(it) 
                } ?: Result.failure(Exception("Resource not found"))
            } else {
                Result.failure(Exception("Failed to get resource: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getResourcesByType(type: ResourceType): Result<List<ResourceDto>> {
        return try {
            val response = resourceApiService.getResourcesByType(type)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get resources by type: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getResourcesByModule(module: String): Result<List<ResourceDto>> {
        return try {
            val response = resourceApiService.getResourcesByModule(module)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get resources by module: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchResources(query: String): Result<List<ResourceDto>> {
        return try {
            val response = resourceApiService.searchResources(query)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to search resources: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 