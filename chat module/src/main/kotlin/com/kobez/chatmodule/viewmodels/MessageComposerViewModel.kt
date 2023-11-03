package com.kobez.chatmodule.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kobez.chatmodule.models.Message
import com.kobez.chatmodule.state.MessageComposerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel responsible for handling the composing and sending of messages.
 *
 * It relays all its core actions to a shared data source, as a central place for all the Composer logic.
 * Additionally, all the core data that can be reused across our SDKs is available through shared data sources, while
 * implementation-specific data is stored in respective in the [ViewModel].
 *
// * @param messageComposerController The controller used to relay all the actions and fetch all the state.
 */
public class MessageComposerViewModel : ViewModel() {

    private val _messageComposerState: MutableStateFlow<MessageComposerState> = MutableStateFlow(MessageComposerState())

    /**
     * The full UI state that has all the required data.
     */
    public val messageComposerState: StateFlow<MessageComposerState> = _messageComposerState


    private val _input: MutableStateFlow<String> = MutableStateFlow("")
    /**
     * UI state of the current composer input.
     */
    public val input: MutableStateFlow<String> = _input

    /**
     * Called when the input changes and the internal state needs to be updated.
     *
     * @param value Current state value.
     */
    public fun setMessageInput(value: String) {
        this._input.update {
            value
        }
        this._messageComposerState.update {
            it.copy(
                inputValue = value
            )
        }
    }

    /**
     * Sends a given message using our Stream API. Based on the internal state, we either edit an existing message,
     * or we send a new message, using our API.
     *
     * It also dismisses any current message actions.
     *
     * @param message The message to send.
     */
    public fun sendMessage(message: Message) {
        Log.d("SENT MESSAGE", message.toString())
        clearData()
    }

    /**
     * Builds a new [Message] to send to our API. Based on the internal state, we use the current action's message and
     * apply the given changes.
     *
     * If we're not editing a message, we'll fill in the required data for the message.
     *
     * @param message Message text.
     *
     * @return [Message] object, with all the data required to send it to the API.
     */
    public fun buildNewMessage(
        message: String,
    ): Message {
        val trimmedMessage = message.trim()
        return Message(
            text = trimmedMessage,
        )
    }

    /**
     * Clears the input and the current state of the composer.
     */
    public fun clearData() {
        this._input.update {
            ""
        }
        this._messageComposerState.update {
            it.copy(
                inputValue = ""
            )
        }
    }
}
