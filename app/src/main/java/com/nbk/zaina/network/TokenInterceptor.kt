package com.nbk.rise.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor() : Interceptor {
    
    @Volatile
    private var token: String? = null
    
    fun setToken(newToken: String?) {
        token = newToken
    }
    
    fun getToken(): String? = token
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        
        val requestBuilder = original.newBuilder()
            .header("Content-Type", "application/json")
        
        // Add authorization header if token exists
        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }
        
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}