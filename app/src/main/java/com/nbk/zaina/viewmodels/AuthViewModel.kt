package com.nbk.rise.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.data.repository.AuthRepository
import com.nbk.rise.data.repository.UserRepository
import com.nbk.rise.data.responses.JwtResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(AuthUiState())
    val uiState: State<AuthUiState> get() = _uiState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (authRepository.isLoggedIn()) {
            getCurrentUser()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.login(email, password)
                .onSuccess { jwtResponse ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        currentUser = jwtResponse,
                        error = null
                    )
                    getCurrentUser()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Login failed"
                    )
                }
        }
    }

    fun logout() {
        authRepository.logout()
        _uiState.value = AuthUiState()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser()
                .onSuccess { userDto ->
                    _uiState.value = _uiState.value.copy(
                        userDetails = userDto
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to get user details"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val currentUser: JwtResponse? = null,
    val userDetails: com.nbk.rise.data.dtos.UserDto? = null,
    val error: String? = null
) {
    val userRole: UserRole? get() = currentUser?.role ?: userDetails?.role
}
