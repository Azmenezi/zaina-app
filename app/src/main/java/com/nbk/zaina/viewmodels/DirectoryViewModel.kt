package com.nbk.rise.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbk.rise.data.dtos.ProfileDto
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.data.dtos.UserSummaryDto
import com.nbk.rise.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DirectoryViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(DirectoryUiState())
    val uiState: State<DirectoryUiState> get() = _uiState

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

    fun loadProfileById(userId: UUID) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingProfile = true, profileError = null)
            
            userRepository.getProfileByUserId(userId)
                .onSuccess { profile ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingProfile = false,
                        selectedProfile = profile,
                        profileError = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingProfile = false,
                        profileError = exception.message ?: "Failed to load profile"
                    )
                }
        }
    }

    fun searchProfiles(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true, searchError = null)
            
            userRepository.searchProfiles(query)
                .onSuccess { profiles ->
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        searchResults = profiles,
                        searchError = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        searchError = exception.message ?: "Failed to search profiles"
                    )
                }
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchResults = emptyList(),
            searchError = null
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearProfileError() {
        _uiState.value = _uiState.value.copy(profileError = null)
    }

    fun clearSearchError() {
        _uiState.value = _uiState.value.copy(searchError = null)
    }
}

data class DirectoryUiState(
    val isLoading: Boolean = false,
    val isLoadingProfile: Boolean = false,
    val isSearching: Boolean = false,
    val users: List<UserSummaryDto> = emptyList(),
    val selectedProfile: ProfileDto? = null,
    val searchResults: List<ProfileDto> = emptyList(),
    val error: String? = null,
    val profileError: String? = null,
    val searchError: String? = null
) 