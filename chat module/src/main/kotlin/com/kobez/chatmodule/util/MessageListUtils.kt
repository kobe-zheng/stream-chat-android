package com.kobez.chatmodule.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.kobez.chatmodule.state.MessagesLazyListState

/**
 * Creates a [MessagesLazyListState] that is remembered across compositions.
 * Changes to the provided initial values will not result in the state being recreated or changed in any way if it has
 * already been created.
 *
 * @see rememberLazyListState
 *
 *  @param initialFirstVisibleItemIndex the initial value for [LazyListState.firstVisibleItemIndex]
 *  inside [MessagesLazyListState].
 *  @param initialFirstVisibleItemScrollOffset the initial value for [LazyListState.firstVisibleItemScrollOffset]
 *  inside [MessagesLazyListState].
 */
@Composable
public fun rememberMessagesLazyListState(
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0,
    messageOffsetHandler: MessagesLazyListState.MessageOffsetHandler = MessagesLazyListState.defaultOffsetHandler,
): MessagesLazyListState {
    return rememberSaveable(saver = MessagesLazyListState.Saver) {
        MessagesLazyListState(
            lazyListState = LazyListState(
                firstVisibleItemIndex = initialFirstVisibleItemIndex,
                firstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset,
            ),
            messageOffsetHandler = messageOffsetHandler,
        )
    }
}

/**
 * Provides a [MessagesLazyListState] that's tied to a given message list. This is the default behavior, where we keep
 * the base scroll position of the list persisted at all times, while the thread scroll state is always new, whenever we
 * enter a thread.
 *
 * In case you want to customize the behavior, provide the [MessagesLazyListState] based on your logic and conditions.
 *
 * @param initialFirstVisibleItemIndex The first visible item index that's required for the base
 * [MessagesLazyListState.lazyListState].
 * @param initialFirstVisibleItemScrollOffset The offset of the first visible item, required for the
 * base [MessagesLazyListState.lazyListState].
 * @param parentMessageId The ID of the parent message, if we're in a thread.
 *
 * @return [MessagesLazyListState] that keeps the scrolling position and offset of the given list and focused message.
 */
@Composable
public fun rememberMessageListState(
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0,
    parentMessageId: String? = null,
    messageOffsetHandler: MessagesLazyListState.MessageOffsetHandler = MessagesLazyListState.defaultOffsetHandler,
): MessagesLazyListState {
    val baseListState = rememberMessagesLazyListState(
        initialFirstVisibleItemIndex = initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset,
        messageOffsetHandler = messageOffsetHandler,
    )

    return if (parentMessageId != null) {
        rememberMessagesLazyListState(messageOffsetHandler = messageOffsetHandler)
    } else {
        baseListState
    }
}
