package com.nbk.rise.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbk.rise.data.dtos.ConversationDto
import com.nbk.rise.data.dtos.ConversationSummaryDto
import com.nbk.rise.data.dtos.MessageDto
import com.nbk.rise.data.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    private val _conversationState = MutableStateFlow(ConversationUiState())
    val conversationState: StateFlow<ConversationUiState> = _conversationState.asStateFlow()

    fun loadConversation(otherUserId: UUID) {
        viewModelScope.launch {
            _conversationState.value = _conversationState.value.copy(isLoading = true, error = null)
            
            messageRepository.getConversation(otherUserId)
                .onSuccess { conversation ->
                    _conversationState.value = _conversationState.value.copy(
                        isLoading = false,
                        conversation = conversation,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _conversationState.value = _conversationState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load conversation"
                    )
                }
        }
    }

    fun sendMessage(receiverId: UUID, content: String, currentUserId: UUID) {
        // Optimistic UI - add message immediately
        val optimisticMessage = MessageDto(
            id = UUID.randomUUID(),
            senderId = currentUserId,
            receiverId = receiverId,
            content = content,
            sentAt = Clock.System.now(),
            isRead = false
        )
        
        val currentConversation = _conversationState.value.conversation
        if (currentConversation != null) {
            val updatedMessages = currentConversation.messages + optimisticMessage
            _conversationState.value = _conversationState.value.copy(
                conversation = currentConversation.copy(messages = updatedMessages),
                sendingMessage = true
            )
        }

        viewModelScope.launch {
            messageRepository.sendMessage(receiverId, content)
                .onSuccess { message ->
                    // Replace optimistic message with real one
                    val currentConv = _conversationState.value.conversation
                    if (currentConv != null) {
                        val updatedMessages = currentConv.messages.map { 
                            if (it.id == optimisticMessage.id) message else it 
                        }
                        _conversationState.value = _conversationState.value.copy(
                            conversation = currentConv.copy(messages = updatedMessages),
                            sendingMessage = false,
                            sendError = null
                        )
                    }
                }
                .onFailure { exception ->
                    // Remove optimistic message on failure
                    val currentConv = _conversationState.value.conversation
                    if (currentConv != null) {
                        val updatedMessages = currentConv.messages.filter { it.id != optimisticMessage.id }
                        _conversationState.value = _conversationState.value.copy(
                            conversation = currentConv.copy(messages = updatedMessages),
                            sendingMessage = false,
                            sendError = exception.message ?: "Failed to send message"
                        )
                    }
                }
        }
    }

    fun markMessageAsRead(messageId: UUID) {
        viewModelScope.launch {
            messageRepository.markMessageAsRead(messageId)
                .onSuccess { 
                    // Update local state
                    val currentConv = _conversationState.value.conversation
                    if (currentConv != null) {
                        val updatedMessages = currentConv.messages.map { message ->
                            if (message.id == messageId) message.copy(isRead = true) else message
                        }
                        _conversationState.value = _conversationState.value.copy(
                            conversation = currentConv.copy(messages = updatedMessages)
                        )
                    }
                }
                .onFailure { 
                    // Silently fail for read receipts
                }
        }
    }

    fun clearConversationError() {
        _conversationState.value = _conversationState.value.copy(error = null)
    }

    fun clearSendError() {
        _conversationState.value = _conversationState.value.copy(sendError = null)
    }
}

data class MessageUiState(
    val conversations: List<ConversationSummaryDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ConversationUiState(
    val isLoading: Boolean = false,
    val sendingMessage: Boolean = false,
    val conversation: ConversationDto? = null,
    val error: String? = null,
    val sendError: String? = null
) 