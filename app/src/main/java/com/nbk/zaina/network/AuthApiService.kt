package com.nbk.rise.network

import com.nbk.rise.data.requests.LoginRequest
import com.nbk.rise.data.requests.RegisterRequest
import com.nbk.rise.data.responses.JwtResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<JwtResponse>
    
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<JwtResponse>
}