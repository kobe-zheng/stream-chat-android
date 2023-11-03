package com.kobez.chatmodule.viewmodels

import com.kobez.chatmodule.state.ConnectionState
import com.kobez.chatmodule.state.MessageItemState
import com.kobez.chatmodule.state.MessageListItemState
import com.kobez.chatmodule.state.MessageListState
import com.kobez.chatmodule.state.TypingItemState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kobez.chatmodule.models.Message
import com.kobez.chatmodule.util.asState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map


/**
 * ViewModel responsible for handling all the business logic & state for the list of messages.
 */
public class MessageListViewModel : ViewModel() {

    /**
     * State handler for the UI, which holds all the information the UI needs to render messages.
     *
     */
    public val currentMessagesState: MessageListState
        get() = messagesState

    /**
     * Current state of the message list.
     */
    private val _messageListState: MutableStateFlow<MessageListState> =
        MutableStateFlow(MessageListState(
            isLoading = false,
            messageItems = listOf(TypingItemState)
        ))
    private val messageListState: StateFlow<MessageListState> = _messageListState

    /**
     * State of the screen.
     */
    private val messagesState: MessageListState by messageListState
        .map { it.copy(messageItems = it.messageItems.reversed()) }
        .asState(viewModelScope, MessageListState())

    /**
     * Sends a message by adding it to list of messages.
     */
    public fun sendMessage(message: Message) {
        val items = mutableListOf<MessageListItemState>().apply {
            addAll(_messageListState.value.messageItems)
        }

        items.add(MessageItemState(message = message.copy(id = message.text), isMine = true))
        items.add(MessageItemState(message = message.copy(text = message.text + " from bot", id = message.text + " from bot"), isMine = false))

        _messageListState.value = _messageListState.value.copy(
            messageItems = items
        )
    }

    /**
     * Current state of the message list.
     */
    private val _connectionState: MutableStateFlow<ConnectionState> =
        MutableStateFlow(ConnectionState.Connected)
    /**
     * Gives us information about the online state of the device.
     */
    public val connectionState: StateFlow<ConnectionState> = _connectionState

    public companion object {
        public const val DEFAULT_MESSAGE_LIMIT: Int = 30
    }
}
