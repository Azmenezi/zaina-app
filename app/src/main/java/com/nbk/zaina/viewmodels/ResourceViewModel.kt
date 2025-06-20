package com.nbk.rise.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbk.rise.data.dtos.ResourceDto
import com.nbk.rise.data.dtos.ResourceType
import com.nbk.rise.data.repository.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ResourceViewModel @Inject constructor(
    private val resourceRepository: ResourceRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(ResourceUiState())
    val uiState: State<ResourceUiState> get() = _uiState

    fun loadAllResources() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            resourceRepository.getAllResources()
                .onSuccess { resources ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        resources = resources,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load resources"
                    )
                }
        }
    }

    fun loadResourceById(resourceId: UUID) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingDetail = true, detailError = null)
            
            resourceRepository.getResourceById(resourceId)
                .onSuccess { resource ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingDetail = false,
                        selectedResource = resource,
                        detailError = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingDetail = false,
                        detailError = exception.message ?: "Failed to load resource details"
                    )
                }
        }
    }

    fun loadResourcesByType(type: ResourceType) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            resourceRepository.getResourcesByType(type)
                .onSuccess { resources ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        resources = resources,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load resources by type"
                    )
                }
        }
    }

    fun loadResourcesByModule(module: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            resourceRepository.getResourcesByModule(module)
                .onSuccess { resources ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        resources = resources,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load resources by module"
                    )
                }
        }
    }

    fun searchResources(query: String) {
        if (query.isBlank()) {
            loadAllResources()
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            resourceRepository.searchResources(query)
                .onSuccess { resources ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        resources = resources,
                        searchResults = resources,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to search resources"
                    )
                }
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(searchResults = emptyList())
        loadAllResources()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearDetailError() {
        _uiState.value = _uiState.value.copy(detailError = null)
    }
}

data class ResourceUiState(
    val isLoading: Boolean = false,
    val isLoadingDetail: Boolean = false,
    val resources: List<ResourceDto> = emptyList(),
    val searchResults: List<ResourceDto> = emptyList(),
    val selectedResource: ResourceDto? = null,
    val error: String? = null,
    val detailError: String? = null
) 