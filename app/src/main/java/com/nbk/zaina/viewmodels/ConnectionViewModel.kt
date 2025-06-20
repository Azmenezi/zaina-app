package com.nbk.rise.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbk.rise.data.dtos.ConnectionDto
import com.nbk.rise.data.dtos.ConnectionStatus
import com.nbk.rise.data.dtos.ConnectionType
import com.nbk.rise.data.repository.ConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val connectionRepository: ConnectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    fun createConnection(targetId: UUID, type: ConnectionType) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true, createError = null)
            
            connectionRepository.createConnection(targetId, type)
                .onSuccess { connection ->
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        createError = null
                    )
                    // Refresh pending connections
                    loadPendingConnections()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        createError = exception.message ?: "Failed to create connection"
                    )
                }
        }
    }

    fun updateConnection(connectionId: UUID, status: ConnectionStatus) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true, updateError = null)
            
            connectionRepository.updateConnection(connectionId, status)
                .onSuccess { connection ->
                    _uiState.value = _uiState.value.copy(
                        isUpdating = false,
                        updateError = null
                    )
                    // Refresh connections
                    loadPendingConnections()
                    loadAcceptedConnections()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isUpdating = false,
                        updateError = exception.message ?: "Failed to update connection"
                    )
                }
        }
    }

    fun loadPendingConnections() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingPending = true, pendingError = null)
            
            connectionRepository.getPendingConnections()
                .onSuccess { connections ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingPending = false,
                        pendingConnections = connections,
                        pendingError = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingPending = false,
                        pendingError = exception.message ?: "Failed to load pending connections"
                    )
                }
        }
    }

    fun loadAcceptedConnections() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingAccepted = true, acceptedError = null)
            
            connectionRepository.getAcceptedConnections()
                .onSuccess { connections ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingAccepted = false,
                        acceptedConnections = connections,
                        acceptedError = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingAccepted = false,
                        acceptedError = exception.message ?: "Failed to load accepted connections"
                    )
                }
        }
    }

    fun clearCreateError() {
        _uiState.value = _uiState.value.copy(createError = null)
    }

    fun clearUpdateError() {
        _uiState.value = _uiState.value.copy(updateError = null)
    }

    fun clearPendingError() {
        _uiState.value = _uiState.value.copy(pendingError = null)
    }

    fun clearAcceptedError() {
        _uiState.value = _uiState.value.copy(acceptedError = null)
    }
}

data class ConnectionUiState(
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isLoadingPending: Boolean = false,
    val isLoadingAccepted: Boolean = false,
    val pendingConnections: List<ConnectionDto> = emptyList(),
    val acceptedConnections: List<ConnectionDto> = emptyList(),
    val createError: String? = null,
    val updateError: String? = null,
    val pendingError: String? = null,
    val acceptedError: String? = null
) 