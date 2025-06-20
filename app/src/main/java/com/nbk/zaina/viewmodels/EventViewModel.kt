package com.nbk.rise.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbk.rise.data.dtos.EventDto
import com.nbk.rise.data.dtos.EventRsvpDto
import com.nbk.rise.data.dtos.RsvpStatus
import com.nbk.rise.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(EventUiState())
    val uiState: State<EventUiState> get() = _uiState

    fun loadEvents(isPublicOnly: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = if (isPublicOnly) {
                eventRepository.getPublicEvents()
            } else {
                eventRepository.getAllEvents()
            }
            
            result.onSuccess { events ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    events = events,
                    error = null
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Failed to load events"
                )
            }
        }
    }

    fun loadUpcomingEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            eventRepository.getUpcomingEvents()
                .onSuccess { events ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        upcomingEvents = events,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load upcoming events"
                    )
                }
        }
    }

    fun loadEventById(eventId: UUID) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingDetail = true, detailError = null)
            
            eventRepository.getEventById(eventId)
                .onSuccess { event ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingDetail = false,
                        selectedEvent = event,
                        detailError = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingDetail = false,
                        detailError = exception.message ?: "Failed to load event details"
                    )
                }
        }
    }

    fun rsvpToEvent(eventId: UUID, status: RsvpStatus) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRsvpLoading = true, rsvpError = null)
            
            eventRepository.rsvpToEvent(eventId, status)
                .onSuccess { rsvp ->
                    _uiState.value = _uiState.value.copy(
                        isRsvpLoading = false,
                        rsvpError = null
                    )
                    // Refresh event details to show updated RSVP status
                    loadEventById(eventId)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRsvpLoading = false,
                        rsvpError = exception.message ?: "Failed to RSVP to event"
                    )
                }
        }
    }

    fun loadEventAttendees(eventId: UUID) {
        viewModelScope.launch {
            eventRepository.getEventAttendees(eventId)
                .onSuccess { attendees ->
                    _uiState.value = _uiState.value.copy(
                        eventAttendees = attendees
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to load event attendees"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearDetailError() {
        _uiState.value = _uiState.value.copy(detailError = null)
    }

    fun clearRsvpError() {
        _uiState.value = _uiState.value.copy(rsvpError = null)
    }
}

data class EventUiState(
    val isLoading: Boolean = false,
    val isLoadingDetail: Boolean = false,
    val isRsvpLoading: Boolean = false,
    val events: List<EventDto> = emptyList(),
    val upcomingEvents: List<EventDto> = emptyList(),
    val selectedEvent: EventDto? = null,
    val eventAttendees: List<EventRsvpDto> = emptyList(),
    val error: String? = null,
    val detailError: String? = null,
    val rsvpError: String? = null
) 