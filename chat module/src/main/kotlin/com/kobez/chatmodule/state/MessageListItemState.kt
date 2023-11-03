package com.kobez.chatmodule.state

import com.kobez.chatmodule.models.Message
import com.kobez.chatmodule.models.User
import java.util.Date

/**
 * Represents a list item inside a message list.
 */
public sealed class MessageListItemState

/**
 * Represents a message item inside the messages list.
 *
 * @param message The [Message] to show in the list.
 * @param isMine Whether the message is sent by the current user or not.
 */
public data class MessageItemState(
    public val message: Message = Message(),
    public val isMine: Boolean = false,
    public val currentUser: User? = null,
) : MessageListItemState()

/**
 * Represents a date separator inside the message list.
 *
 * @param date The date to show on the separator.
 */
public data class DateSeparatorItemState(
    val date: Date,
) : MessageListItemState()


/**
 * Represents a typing indicator item inside a message list.
 *
 */
public data object TypingItemState : MessageListItemState()

/**
 * Represents an empty thread placeholder item inside thread messages list.
 */
public data object EmptyThreadPlaceholderItemState : MessageListItemState()
