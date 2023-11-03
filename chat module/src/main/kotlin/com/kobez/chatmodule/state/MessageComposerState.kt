package com.kobez.chatmodule.state

import com.kobez.chatmodule.models.User

/**
 * Represents the state within the message input.
 *
 * @param inputValue The current text value that's within the input.
 * @param currentUser The currently logged in user.
 */
public data class MessageComposerState @JvmOverloads constructor(
    val inputValue: String = "",
    val currentUser: User? = null,
    val coolDownTime: Int = 0,
)