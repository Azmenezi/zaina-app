package com.nbk.rise.data.repository

import com.nbk.rise.data.requests.LoginRequest
import com.nbk.rise.data.requests.RegisterRequest
import com.nbk.rise.data.responses.JwtResponse
import com.nbk.rise.network.AuthApiService
import com.nbk.rise.network.TokenInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val tokenInterceptor: TokenInterceptor
) {
    
    suspend fun login(email: String, password: String): Result<JwtResponse> {
        return try {
            val response = authApiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val jwtResponse = response.body()!!
                tokenInterceptor.setToken(jwtResponse.token)
                Result.success(jwtResponse)
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun register(request: RegisterRequest): Result<JwtResponse> {
        return try {
            val response = authApiService.register(request)
            if (response.isSuccessful) {
                val jwtResponse = response.body()!!
                tokenInterceptor.setToken(jwtResponse.token)
                Result.success(jwtResponse)
            } else {
                Result.failure(Exception("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun logout() {
        tokenInterceptor.setToken(null)
    }
    
    fun isLoggedIn(): Boolean {
        return tokenInterceptor.getToken() != null
    }
} 