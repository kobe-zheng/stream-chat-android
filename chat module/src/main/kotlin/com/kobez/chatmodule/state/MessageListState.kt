package com.kobez.chatmodule.state

import com.kobez.chatmodule.models.User

/**
 * Holds the state of the messages list screen.
 *
 * @param messageItems The list of [MessageListItemState]s to be shown in the list.
 * @param endOfNewMessagesReached Whether the user has reached the newest message or not.
 * @param endOfOldMessagesReached Whether the user has reached the older message or not.
 * @param isLoading Whether the initial loading is in progress or not.
 * @param isLoadingNewerMessages Whether loading of a page with newer messages is in progress or not.
 * @param isLoadingOlderMessages Whether loading of a page with older messages is in progress or not.
 * @param currentUser The current logged in [User].
 */
public data class MessageListState(
    public val messageItems: List<MessageListItemState> = emptyList(),
    public val endOfNewMessagesReached: Boolean = true,
    public val endOfOldMessagesReached: Boolean = false,
    public val isLoading: Boolean = false,
    public val isLoadingNewerMessages: Boolean = false,
    public val isLoadingOlderMessages: Boolean = false,
    public val currentUser: User? = User(),
    public val unreadCount: Int = 0,
    public val newMessageState: NewMessageState? = null,
)

internal fun MessageListState.stringify(): String {
    return "MessageListState(" +
        "messageItems.size: ${messageItems.size}, " +
        "endOfNewMessagesReached: $endOfNewMessagesReached, " +
        "endOfOldMessagesReached: $endOfOldMessagesReached, " +
        "isLoading: $isLoading, " +
        "isLoadingNewerMessages: $isLoadingNewerMessages, " +
        "isLoadingOlderMessages: $isLoadingOlderMessages, " +
        "currentUser.id: ${currentUser?.id}, " +
        "newMessageState: $newMessageState, "
}
