package com.nbk.rise.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbk.rise.data.dtos.UserDto
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.data.dtos.UserSummaryDto
import com.nbk.rise.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun loadAllUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            userRepository.getAllUsers()
                .onSuccess { users ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        users = users,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load users"
                    )
                }
        }
    }

    fun loadUsersByRole(role: UserRole) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            userRepository.getUsersByRole(role)
                .onSuccess { users ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        users = users,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load users by role"
                    )
                }
        }
    }

    fun loadUsersByCohort(cohortId: UUID) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            userRepository.getUsersByCohort(cohortId)
                .onSuccess { users ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        users = users,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load users by cohort"
                    )
                }
        }
    }

    fun loadUser(userId: UUID) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingUser = true, userError = null)
            
            userRepository.getUserById(userId)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingUser = false,
                        selectedUser = user,
                        userError = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingUser = false,
                        userError = exception.message ?: "Failed to load user"
                    )
                }
        }
    }

    fun searchProfiles(query: String) {
        if (query.isBlank()) {
            loadAllUsers()
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            userRepository.searchProfiles(query)
                .onSuccess { profiles ->
                    // Convert profiles to user summaries
                    val userSummaries = profiles.map { profile ->
                        UserSummaryDto(
                            id = profile.userId,
                            email = "", // Not available in profile
                            role = UserRole.PARTICIPANT, // Default role, should be fetched separately
                            name = profile.name,
                            position = profile.position,
                            company = profile.company
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        users = userSummaries,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to search profiles"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearUserError() {
        _uiState.value = _uiState.value.copy(userError = null)
    }

    fun clearUsers() {
        _uiState.value = _uiState.value.copy(users = emptyList())
    }
}

data class UserUiState(
    val isLoading: Boolean = false,
    val isLoadingUser: Boolean = false,
    val users: List<UserSummaryDto> = emptyList(),
    val selectedUser: UserDto? = null,
    val error: String? = null,
    val userError: String? = null
) 