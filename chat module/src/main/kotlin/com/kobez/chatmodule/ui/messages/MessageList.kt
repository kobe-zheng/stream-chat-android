package com.kobez.chatmodule.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kobez.chatmodule.R
import com.kobez.chatmodule.state.MessageListItemState
import com.kobez.chatmodule.state.MessageListState
import com.kobez.chatmodule.state.MessagesLazyListState
import com.kobez.chatmodule.theme.ChatTheme
import com.kobez.chatmodule.ui.components.LoadingIndicator
import com.kobez.chatmodule.ui.components.messages.DefaultMessagesHelperContent
import com.kobez.chatmodule.ui.components.messages.DefaultMessagesLoadingMoreIndicator
import com.kobez.chatmodule.ui.components.messages.MessageContainer
import com.kobez.chatmodule.ui.components.messages.Messages
import com.kobez.chatmodule.util.rememberMessageListState
import com.kobez.chatmodule.viewmodels.MessageListViewModel

/**
 * Default MessageList component, that relies on [MessageListViewModel] to connect all the data
 * handling operations. It also delegates events to the ViewModel to handle, like long item
 * clicks and pagination.
 *
 * @param viewModel The ViewModel that stores all the data and business logic required to show a
 * list of messages. The user has to provide one in this case, as we require the channelId to start
 * the operations.
 * @param modifier Modifier for styling.
 * @param contentPadding Padding values to be applied to the message list surrounding the content inside.
 * @param messagesLazyListState State of the lazy list that represents the list of messages. Useful for controlling the
 * scroll state and focused message offset.
 * @param loadingContent Composable that represents the loading content, when we're loading the initial data.
 * @param emptyContent Composable that represents the empty content if there are no messages.
 * @param helperContent Composable that, by default, represents the helper content featuring scrolling behavior based
 * on the list state.
 * @param loadingMoreContent Composable that represents the loading more content, when we're loading the next page.
 * @param itemContent Composable that represents each item in a list. By default, we provide
 * the [MessageContainer] which sets up different message types. Users can override this to provide fully custom UI
 * and behavior.
 */
@Composable
public fun MessageList(
    viewModel: MessageListViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    messagesLazyListState: MessagesLazyListState =
        rememberMessageListState(parentMessageId = null),
    loadingContent: @Composable () -> Unit = { DefaultMessageListLoadingIndicator(modifier) },
    emptyContent: @Composable () -> Unit = { DefaultMessageListEmptyContent(modifier) },
    helperContent: @Composable BoxScope.() -> Unit = {
        DefaultMessagesHelperContent(
            messagesState = viewModel.currentMessagesState,
            messagesLazyListState = messagesLazyListState,
        )
    },
    loadingMoreContent: @Composable () -> Unit = { DefaultMessagesLoadingMoreIndicator() },
    itemContent: @Composable (MessageListItemState) -> Unit = { messageListItem ->
        DefaultMessageContainer(
            messageListItemState = messageListItem,
        )
    },
) {
    MessageList(
        modifier = modifier,
        contentPadding = contentPadding,
        currentState = viewModel.currentMessagesState,
        messagesLazyListState = messagesLazyListState,
        itemContent = itemContent,
        helperContent = helperContent,
        loadingMoreContent = loadingMoreContent,
        loadingContent = loadingContent,
        emptyContent = emptyContent,
    )
}

/**
 * The default message container item.
 *
 * @param messageListItemState The state of the message list item.
 */
@Composable
internal fun DefaultMessageContainer(
    messageListItemState: MessageListItemState,
) {
    MessageContainer(
        messageListItemState = messageListItemState,
    )
}

/**
 * The default message list loading indicator.
 *
 * @param modifier Modifier for styling.
 */
@Composable
internal fun DefaultMessageListLoadingIndicator(modifier: Modifier) {
    LoadingIndicator(modifier)
}

/**
 * The default empty placeholder that is displayed when there are no messages in the channel.
 *
 * @param modifier Modifier for styling.
 */
@Composable
internal fun DefaultMessageListEmptyContent(modifier: Modifier) {
    Box(
        modifier = modifier.background(color = ChatTheme.colors.appBackground),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.stream_compose_message_list_empty_messages),
            style = ChatTheme.typography.body,
            color = ChatTheme.colors.textLowEmphasis,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Clean representation of the MessageList that is decoupled from ViewModels. This components allows
 * users to connect the UI to their own data providers, as it relies on pure state.
 *
 * @param currentState The state of the component, represented by [MessageListState].
 * @param modifier Modifier for styling.
 * @param contentPadding Padding values to be applied to the message list surrounding the content inside.
 * @param messagesLazyListState State of the lazy list that represents the list of messages. Useful for controlling the
 * scroll state and focused message offset.
 * @param loadingContent Composable that represents the loading content, when we're loading the initial data.
 * @param emptyContent Composable that represents the empty content if there are no messages.
 * @param helperContent Composable that, by default, represents the helper content featuring scrolling behavior based
 * on the list state.
 * @param loadingMoreContent Composable that represents the loading more content, when we're loading the next page.
 * @param itemContent Composable that represents each item in the list, that the user can override
 * for custom UI and behavior.
 */
@Composable
public fun MessageList(
    currentState: MessageListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    messagesLazyListState: MessagesLazyListState =
        rememberMessageListState(parentMessageId = null),
    loadingContent: @Composable () -> Unit = { DefaultMessageListLoadingIndicator(modifier) },
    emptyContent: @Composable () -> Unit = { DefaultMessageListEmptyContent(modifier) },
    helperContent: @Composable BoxScope.() -> Unit = {
        DefaultMessagesHelperContent(
            messagesState = currentState,
            messagesLazyListState = messagesLazyListState,
        )
    },
    loadingMoreContent: @Composable () -> Unit = { DefaultMessagesLoadingMoreIndicator() },
    itemContent: @Composable (MessageListItemState) -> Unit = {
        DefaultMessageContainer(
            messageListItemState = it,
        )
    },
) {
    val isLoading = currentState.isLoading
    val messages = currentState.messageItems

    when {
        isLoading -> loadingContent()
        messages.isNotEmpty() -> Messages(
            modifier = modifier,
            contentPadding = contentPadding,
            messagesState = currentState,
            messagesLazyListState = messagesLazyListState,
            helperContent = helperContent,
            loadingMoreContent = loadingMoreContent,
            itemContent = itemContent,
        )
        else -> emptyContent()
    }
}
