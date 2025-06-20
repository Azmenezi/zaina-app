package com.nbk.rise.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbk.rise.data.dtos.ProfileDto
import com.nbk.rise.data.repository.UserRepository
import com.nbk.rise.data.requests.UpdateProfileRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile(userId: UUID) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            userRepository.getProfileByUserId(userId)
                .onSuccess { profile ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profile = profile,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load profile"
                    )
                }
        }
    }

    fun updateProfile(userId: UUID, request: UpdateProfileRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true, updateError = null)
            
            userRepository.updateProfile(userId, request)
                .onSuccess { profile ->
                    _uiState.value = _uiState.value.copy(
                        isUpdating = false,
                        profile = profile,
                        updateError = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isUpdating = false,
                        updateError = exception.message ?: "Failed to update profile"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearUpdateError() {
        _uiState.value = _uiState.value.copy(updateError = null)
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val profile: ProfileDto? = null,
    val error: String? = null,
    val updateError: String? = null
) 